package com.shopu.service.impl;
import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.UserCreateRequest;
import com.shopu.model.dtos.requests.update.UpdateProfileRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.dtos.response.UserListResponse;
import com.shopu.model.entities.User;
import com.shopu.model.enums.Role;
import com.shopu.repository.common.UserRepository;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApiResponse<User> getUser(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null) {
            return new ApiResponse<>(user, HttpStatus.OK);
        }
        User newUser = new User(phoneNumber);
        newUser.setRole(Role.USER);
        return new ApiResponse<>(userRepository.save(newUser), HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<User> registerUser(UserCreateRequest createRequest) {
        User user = findByPhoneNumber(createRequest.getPhoneNumber());
        if(user != null){
            throw new ApplicationException("Phone number already exists");
        }

        User newUser = new User(createRequest.getPhoneNumber());
        newUser.setName(createRequest.getName());
        newUser.setEmail(createRequest.getEmail());
        newUser.setWhatsappNumber(createRequest.getWhatsappNumber());
        newUser.setRole(createRequest.getRole());
        return new ApiResponse<>(userRepository.save(newUser), HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<Boolean> updateMobileNumber(String id, String newMobileNumber) {
        User user = Optional.ofNullable(findById(id)).orElseThrow(
                () -> new ApplicationException("User not found"));

        User fetchedUser = userRepository.findByPhoneNumber(newMobileNumber);
        if(fetchedUser != null){
            return new ApiResponse<>("Phone Number already registered", HttpStatus.BAD_REQUEST);
        }else {
            user.setPhoneNumber(newMobileNumber);
            userRepository.save(user);
            return new ApiResponse<>(true, HttpStatus.OK);
        }
    }

    @Override
    public ApiResponse<User> updateProfile(String id, UpdateProfileRequest updateRequest) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            throw new ApplicationException("User not found");
        }

        user.setName(updateRequest.getName());
        user.setEmail(updateRequest.getEmail());
        user.setWhatsappNumber(updateRequest.getWhatsappNumber());
        return new ApiResponse<>(userRepository.save(user), HttpStatus.OK);
    }

    @Override
    public ApiResponse<User> fetchById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException("User not found"));
        return new ApiResponse<>(user, HttpStatus.OK);
    }

    @Override
    public ApiResponse<PagedResponse<UserListResponse>> getAllUsers(int page, int size) {
        int skip = page * size;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.skip(skip),
                Aggregation.limit(size),
                Aggregation.project("id", "phoneNumber", "name", "email", "role", "createdAt", "lastSignedAt")
        );

        List<UserListResponse> users = mongoTemplate.aggregate(
                aggregation,
                "users", // Mongo collection name
                UserListResponse.class
        ).getMappedResults();

        long total = mongoTemplate.count(new Query(), User.class);
        int totalPages = (int) Math.ceil((double) total / size);

        PagedResponse<UserListResponse> pagedResponse = new PagedResponse<>(
                users,
                page,
                totalPages,
                page >= totalPages - 1,
                page == 0
        );
        return new ApiResponse<>(pagedResponse, HttpStatus.OK);
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public boolean updateCart(String userId, String cartItemId, boolean addItem) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null) {
            throw new ApplicationException("User not found");
        }

        if(addItem){
            user.getCartItemsId().add(cartItemId);
        }else {
            user.getCartItemsId().remove(cartItemId);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public List<String> clearCart(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new ApplicationException("User not found");
        }
        List<String> cartItems = user.getCartItemsId();
        user.getCartItemsId().clear();
        userRepository.save(user);
        return cartItems;
    }

    @Override
    public boolean updateAddress(String userId, String addressId, boolean addAddress) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new ApplicationException("User not found");
        }
        if(addAddress){
            user.getAddressIds().add(addressId);
        }else {
            user.getAddressIds().remove(addressId);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateOrder(String userId, String orderId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new ApplicationException("User not found");
        }
        user.getCartItemsId().clear();
        user.getOrderIds().add(orderId);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public void updateLastSignIn(String id) {
        User user = findById(id);
        user.setLastSignedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}

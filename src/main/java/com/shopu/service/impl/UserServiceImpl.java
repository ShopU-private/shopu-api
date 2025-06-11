package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.AddressRequest;
import com.shopu.model.entities.User;
import com.shopu.model.enums.Role;
import com.shopu.repository.UserRepository;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if(user != null){
            return user;
        }else {
            User newUser = new User(phoneNumber);
            newUser.setRole(Role.USER);
            return userRepository.save(newUser);
        }
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void updateLastSignIn(String id) {
        User user = findById(id);
        user.setLastSignedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public Boolean updateCart(String userId, String cartProductId, boolean addItem) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null) {
            throw new ApplicationException("User not found");
        }
        if(addItem){
            user.getCart_items_id().add(cartProductId);
        }else {
            user.getCart_items_id().remove(cartProductId);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public ApiResponse<Boolean> updateAddress(String userId, String addressId, boolean addAddress) {
        System.out.println(userId);
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new ApplicationException("User not found");
        }
        if(addAddress){
            user.getAddress_ids().add(addressId);
        }else {
            user.getAddress_ids().remove(addressId);
        }
        userRepository.save(user);
        return new ApiResponse<>(true, HttpStatus.OK);
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
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}

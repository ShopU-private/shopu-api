package com.shopu.service;


import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.UserCreateRequest;
import com.shopu.model.dtos.requests.update.UpdateProfileRequest;
import com.shopu.model.entities.User;

import java.util.List;

public interface UserService {

    ApiResponse<User> getUser(String phoneNumber);

    ApiResponse<User> registerUser(UserCreateRequest createRequest);

    ApiResponse<User> fetchById(String id);

    ApiResponse<Boolean> updateMobileNumber(String id, String mobNo);

    ApiResponse<User> updateProfile(String id, UpdateProfileRequest updateRequest);

    User findByPhoneNumber(String phoneNumber);

    User findById(String userId);

    List<User> getAllUser();

    void updateLastSignIn(String id);

    boolean updateCart(String userId, String cartItemId, boolean addItem);

    boolean updateAddress(String userId, String addressId, boolean addAddress);

    boolean updateOrder(String userId, String orderId);

    List<String> clearCart(String userId);
}

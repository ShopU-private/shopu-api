package com.shopu.service;


import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.User;

import java.util.List;

public interface UserService {

    ApiResponse<User> getUser(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    ApiResponse<User> fetchById(String id);

    User findById(String userId);

    List<User> getAllUser();

    void updateLastSignIn(String id);

    ApiResponse<Boolean> updateCart(String userId, String cartItemId, boolean addItem);

    ApiResponse<Boolean> updateAddress(String userId, String addressId, boolean addAddress);

    ApiResponse<Boolean> updateOrder(String userId, String orderId);

    ApiResponse<Boolean> updateMobileNumber(String id, String mobNo);
}

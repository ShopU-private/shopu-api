package com.shopu.service;


import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.User;

import java.util.List;

public interface UserService {

    User getUser(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    User findById(String userId);

    List<User> getAllUser();

    void updateLastSignIn(String id);

    Boolean updateCart(String userId, String cartProductId, boolean addItem);

    ApiResponse<Boolean> updateAddress(String userId, String addressId, boolean addAddress);

    ApiResponse<Boolean> updateMobileNumber(String id, String mobNo);
}

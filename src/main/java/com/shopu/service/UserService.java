package com.shopu.service;


import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.UserCreateRequest;
import com.shopu.model.dtos.requests.update.UpdateProfileRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.dtos.response.UserListResponse;
import com.shopu.model.entities.User;

import java.util.List;

public interface UserService {

    ApiResponse<User> getUser(String phoneNumber);

    ApiResponse<User> registerUser(UserCreateRequest createRequest);

    ApiResponse<User> fetchById(String id);

    ApiResponse<Boolean> updateMobileNumber(String id, String mobNo);

    ApiResponse<User> updateProfile(String id, UpdateProfileRequest updateRequest);

    ApiResponse<User> addPrescription(String id, String prescriptionUrl);

    ApiResponse<PagedResponse<UserListResponse>> getAllUsers(int page, int size);

    User findByPhoneNumber(String phoneNumber);

    User findById(String userId);

    List<User> getAllUser();

    void updateLastSignIn(String id);

    boolean updateAddress(String userId, String addressId, boolean addAddress);

    boolean updateOrder(String userId, String orderId);

    ApiResponse<Long> getNoOfAllUser();
}

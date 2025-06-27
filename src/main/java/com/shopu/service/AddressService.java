package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.AddressRequest;
import com.shopu.model.dtos.requests.update.AddressUpdateRequest;
import com.shopu.model.entities.Address;
import com.shopu.model.entities.CartProduct;

import java.util.List;

public interface AddressService {

    ApiResponse<Address> addAddress(AddressRequest addRequest);

    ApiResponse<Address> updateAddress(AddressUpdateRequest updateRequest);

    ApiResponse<Boolean> removeAddress(String userId, String addressId);

    ApiResponse<List<Address>> fetchAddress(List<String> ids);
}

package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.AddressRequest;
import com.shopu.model.dtos.requests.update.AddressUpdateRequest;
import com.shopu.model.entities.Address;
import com.shopu.model.entities.User;
import com.shopu.repository.AddressRepository;
import com.shopu.service.AddressService;
import com.shopu.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Override
    public ApiResponse<Address> addAddress(AddressRequest addRequest) {
        Address address = new Address();
        modelMapper.map(addRequest, address);
        address.setId(null);
        Address savedAddress = addressRepository.save(address);
        userService.updateAddress(addRequest.getUserId(), savedAddress.getId(), true);
        return new ApiResponse<>(savedAddress, HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<Address> updateAddress(AddressUpdateRequest updateRequest) {
        Address address = addressRepository.findById(updateRequest.getAddressId()).orElse(null);
        if(address == null){
            throw new ApplicationException("Address not found");
        }
        modelMapper.map(updateRequest, address);
        return new ApiResponse<>(addressRepository.save(address), HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<Address>> fetchAddress(String userId) {
        User user = userService.findById(userId);
        if(user == null){
            throw new ApplicationException("User not found");
        }
        List<String> addressIds = user.getAddress_ids();

        List<Address> addresses = addressRepository.findAllById(addressIds);

        if(addresses.isEmpty()){
            return new ApiResponse<>(Collections.emptyList(), HttpStatus.OK);
        }

        return new ApiResponse<>(addresses, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Boolean> removeAddress(String userId, String addressId) {
        Address address = addressRepository.findById(addressId).orElse(null);
        if(address == null){
            throw new ApplicationException("Address not found");
        }
        userService.updateAddress(userId, address.getId(), false);
        addressRepository.delete(address);
        return new ApiResponse<>(true, HttpStatus.OK);
    }
}

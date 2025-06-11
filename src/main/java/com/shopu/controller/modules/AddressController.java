package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.AddressRequest;
import com.shopu.model.dtos.requests.update.AddressUpdateRequest;
import com.shopu.model.entities.Address;
import com.shopu.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Address>> addAddress(@RequestBody AddressRequest addRequest){
        ApiResponse<Address> response = addressService.addAddress(addRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@RequestBody AddressUpdateRequest updateRequest){
        ApiResponse<Address> response = addressService.updateAddress(updateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Boolean>> removeAddress(@RequestParam String userId, @RequestParam String addressId){
        ApiResponse<Boolean> response = addressService.removeAddress(userId, addressId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
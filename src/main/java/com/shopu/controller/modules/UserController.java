package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.User;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> fetchById(@PathVariable String id){
        ApiResponse<User> response = userService.fetchById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}/mob-update")
    public ResponseEntity<ApiResponse<Boolean>> updateMobileNumber(@PathVariable String id, @RequestParam String newMobileNumber){
        ApiResponse<Boolean> response = userService.updateMobileNumber(id, newMobileNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/fetch")
    public ApiResponse<List<User>> getAllUser(){
        return new ApiResponse<>(userService.getAllUser(), HttpStatus.OK);
    }

}

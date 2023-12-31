package com.qavi.carmaintanence.usermanagement.controllers.user;

import com.qavi.carmaintanence.usermanagement.entities.user.PasswordUpdate;
import com.qavi.carmaintanence.usermanagement.entities.user.User;
import com.qavi.carmaintanence.usermanagement.models.ResponseModel;
import com.qavi.carmaintanence.usermanagement.models.UserDataModel;
import com.qavi.carmaintanence.usermanagement.services.user.GoogleSignInService;
import com.qavi.carmaintanence.usermanagement.services.user.ProfileImageService;
import com.qavi.carmaintanence.usermanagement.services.user.UserService;
import com.qavi.carmaintanence.usermanagement.utils.ConverterModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private GoogleSignInService googleSignInService;

    @Autowired
    private ProfileImageService profileImageService;

    //Get All Users
    @GetMapping
    public ResponseEntity<List<UserDataModel>> getAllUser() {
        List<User> users = userService.getAllUsers();
        List<UserDataModel> convertedList = new ArrayList<>();
        for (User user : users) {
            convertedList.add(ConverterModels.convertUserToUserDataModel(user));
        }
        return new ResponseEntity<List<UserDataModel>>(convertedList, HttpStatus.OK);
    }

    @GetMapping("/get-customer/{businessId}")
    public ResponseEntity<List<Map<String,Object>>> findCustomer(@PathVariable Long businessId) {
        var users = userService.findCustomer(businessId);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    //Get User By Id
    @GetMapping("/{id}")
    public ResponseEntity<UserDataModel> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserDataModel userDataModel = ConverterModels.convertUserToUserDataModel(user);
        return new ResponseEntity<UserDataModel>(userDataModel, HttpStatus.OK);
    }


    // get total employees count
    @GetMapping("/get-employees/{business_id}")
    public ResponseEntity<ResponseModel> getEmployees(@PathVariable Long business_id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Employee count retrieved successfully")
                .data(userService.getEmployeeCount(business_id))
                .build();

        if (responseModel.getData() == null || (int) responseModel.getData() == 0) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("No employees found");
        }

        return ResponseEntity.status(responseModel.getStatus()).body(responseModel);
    }


    @PostMapping("signIn/")
    public  ResponseEntity<ResponseModel> googleSignIn(@RequestBody UserDataModel userDataModel){
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User created successfully")
                .data(new Object())
                .build();

        String accesstoken=googleSignInService.SignIn(userDataModel);
        responseModel.setData(accesstoken);

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }


    //Create User
    @PostMapping("/register/{type}")
    public ResponseEntity<ResponseModel> createUser(@RequestBody User user,@PathVariable String type) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User created successfully")
                .data(new Object())
                .build();

        if (userService.existsByEmail(user.getEmail())) {
            responseModel.setStatus(HttpStatus.BAD_REQUEST);
            responseModel.setMessage("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
        Long userId = userService.createUser(user, type);
        if (userId == null) {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed to create user");
        } else {
            responseModel.setData(userId); // Set the userId in the response
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PostMapping("/register/{type}/{business_id}")
    public  ResponseEntity<ResponseModel> createUserEmployeeOrCustomer(@RequestBody User user,@PathVariable String type,@PathVariable String business_id){

        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User created successfully")
                .data(new Object())
                .build();

        if (userService.existsByEmail(user.getEmail())) {
            responseModel.setStatus(HttpStatus.BAD_REQUEST);
            responseModel.setMessage("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }

        if(type.equalsIgnoreCase("employee")) {
            Long userId = userService.createEmployee(user, type,business_id);
            if (userId == null) {
                responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
                responseModel.setMessage("Failed to create user");
            } else {
                responseModel.setData(userId); // Set the userId in the response
            }
        }
        else {
            Long userId = userService.createCustomer(user,type,business_id);
            if (userId == null) {
                responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
                responseModel.setMessage("Failed to create user");
            } else {
                responseModel.setData(userId); // Set the userId in the response
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }



    //Update User
    @PutMapping("/update-user/{id}")
    public ResponseEntity<ResponseModel> updateUser(@RequestBody UserDataModel userDataModel, @PathVariable Long id) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User updated successfully")
                .data(new Object())
                .build();
        if (!userService.updateUser(userDataModel, id)) {
            responseModel.setMessage("Failed to update user");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    // Delete User

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        Boolean deletedUser = userService.deleteUser(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }


    // Change Password
    @PutMapping("/update-password/{id}")
    public ResponseEntity<ResponseModel> editPassword(@RequestBody PasswordUpdate passwordUpdate, @PathVariable Long id) {

        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Password updated successfully")
                .data(new Object())
                .build();

        if (!userService.updatePassword(passwordUpdate, id)) {
            responseModel.setMessage("Failed to update password");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Map<String, Object>> getProfileImageData(@PathVariable Long userId) {
        Map<String, Object> profileImageData = profileImageService.getProfileImgData(userId);
        return ResponseEntity.ok(profileImageData);
    }

}

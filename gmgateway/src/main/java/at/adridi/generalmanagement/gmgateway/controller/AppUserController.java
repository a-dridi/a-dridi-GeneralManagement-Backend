/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.controller;

import at.adridi.generalmanagement.gmgateway.model.AppUser;
import at.adridi.generalmanagement.gmgateway.model.LoginUser;
import at.adridi.generalmanagement.gmgateway.model.ResponseMessage;
import at.adridi.generalmanagement.gmgateway.service.UserService;
import at.adridi.generalmanagement.gmgateway.service.UserSettingsService;
import at.adridi.generalmanagement.gmgateway.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * API endpoints for registration and login of user accounts. Public API
 * endpoints.
 *
 * Login API endpoint is /api/login. Settings in util package
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class AppUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserSettingsService userSettingsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(ApiEndpoints.API_PUBLIC_REGISTRATION)
    public ResponseEntity<String> doRegistration(@RequestBody String newUserJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        AppUser newUser;
        try {
            newUser = objectMapper.readValue(newUserJson, AppUser.class);
            newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
            AppUser savedUser = this.userService.save(newUser);
            if (savedUser != null) {
                this.userSettingsService.createDefaultUserSettings(savedUser.getUserId());
                return ResponseEntity.ok(savedUser.getEmail());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR. Registration failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return status(HttpStatus.BAD_REQUEST).body("ERROR! User could not be registered! Please check the passed JSON.");
        }
    }

    /**
     * Get user email by user id
     *
     * @param userLoginCredentialsJson
     * @return
     */
    @GetMapping("/api/getUserEmail/{userId}")
    public ResponseEntity<ResponseMessage> getUserId(@PathVariable Integer userId) {
        try {
            AppUser savedUser = this.userService.getUserByUserId(userId);
            if (savedUser != null) {
                return ResponseEntity.ok(new ResponseMessage(savedUser.getEmail()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return status(HttpStatus.BAD_GATEWAY).body(new ResponseMessage(""));
        }
    }

    /**
     * Get user object by user id
     *
     * @param userId
     * @return
     */
    @GetMapping("/api/getUserObject/{userId}")
    public ResponseEntity<AppUser> getUserObjectByUserId(@PathVariable Long userId) {
        try {
            AppUser savedUser = this.userService.getUserByUserId(userId);
            if (savedUser != null) {
                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AppUser());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return status(HttpStatus.BAD_GATEWAY).body(new AppUser());
        }
    }

    /**
     * Get user id by passing login credentials.
     *
     * @param userLoginCredentialsJson
     * @return
     */
    @PostMapping("/api/getUserId")
    public ResponseEntity<Long> getUserId(@RequestBody String userLoginCredentialsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginUser loginUser;
        try {
            loginUser = objectMapper.readValue(userLoginCredentialsJson, LoginUser.class);
            loginUser.setPassword(this.passwordEncoder.encode(loginUser.getPassword()));
            AppUser savedUser = this.userService.getUserByEmail(loginUser.getEmail());
            if (savedUser != null) {
                return ResponseEntity.ok(savedUser.getUserId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0L);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return status(HttpStatus.BAD_GATEWAY).body(0L);
        }
    }

    @PutMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/update")
    public ResponseEntity<String> updateUser(@RequestBody String updatedUserJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        AppUser updatedUser;
        try {
            updatedUser = objectMapper.readValue(updatedUserJson, AppUser.class);
            AppUser savedUser = this.userService.save(updatedUser);
            if (savedUser != null) {
                return ResponseEntity.ok(savedUser.getEmail());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR. Update failed!");
            }

        } catch (IOException ex) {
            Logger.getLogger(AppUserController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR. Please check the passed JSON object!");
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        if (this.userService.deleteById(userId)) {
            return ResponseEntity.ok("Your User was deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. User does not exists!");
        }
    }
}

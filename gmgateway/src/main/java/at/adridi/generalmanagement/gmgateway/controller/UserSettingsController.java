/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.controller;

import at.adridi.generalmanagement.gmgateway.model.ResponseMessage;
import at.adridi.generalmanagement.gmgateway.model.UserSetting;
import at.adridi.generalmanagement.gmgateway.service.UserSettingsService;
import at.adridi.generalmanagement.gmgateway.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API end points for managing settings of a user (example: currency).
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/get/userSetting/bySettingsKey/{settingsKey}/{userId}")
    private ResponseEntity<UserSetting> getUserSettingBySettingsKey(@PathVariable String settingsKey, @PathVariable long userId) {
        try {
            UserSetting userSetting = this.userSettingsService.getUserSettingbySettingsKey(settingsKey, userId);
            return status(HttpStatus.OK).body(userSetting);
        } catch (Exception ex) {
            Logger.getLogger(UserSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new UserSetting());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/all/userSettings/byUserId/{userId}")
    public ResponseEntity<List<UserSetting>> getAllSettingsOfUserId(@PathVariable long userId) {
        try {
            List<UserSetting> userSettingList = this.userSettingsService.getAllSettingsOfUser(userId);
            if (!CollectionUtils.isEmpty(userSettingList)) {
                return status(HttpStatus.OK).body(userSettingList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<UserSetting>());
            }
        } catch (Exception ex) {
            Logger.getLogger(UserSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<UserSetting>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/updateSetting")
    private ResponseEntity<ResponseMessage> updateUserSetting(@RequestBody String userSettingJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserSetting updatedUserSetting;
        try {
            updatedUserSetting = objectMapper.readValue(userSettingJson, UserSetting.class);
            this.userSettingsService.updateUserSetting(updatedUserSetting.getUserSettingId(), updatedUserSetting.getSettingKey(), updatedUserSetting.getUserId(), updatedUserSetting.getSettingValue());
            return status(HttpStatus.OK).body(new ResponseMessage("OK. User setting updated."));
        } catch (Exception ex) {
            Logger.getLogger(UserSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR! User setting could not be updated!"));
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/newSetting")
    private ResponseEntity<ResponseMessage> addUserSetting(@RequestBody String userSettingJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserSetting updatedUserSetting;
        try {
            updatedUserSetting = objectMapper.readValue(userSettingJson, UserSetting.class);
            this.userSettingsService.addNewUserSetting(updatedUserSetting);
            return status(HttpStatus.OK).body(new ResponseMessage("OK. User setting saved."));
        } catch (Exception ex) {
            Logger.getLogger(UserSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR! User setting could not be saved!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_SETTINGS_USER + "/delete/usersetting/{settingsKey}/{userId}")
    private ResponseEntity<ResponseMessage> deleteUserSetting(@PathVariable String settingsKey, @PathVariable long userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (this.userSettingsService.deleteByUserSettingBySettingsKey(settingsKey, userId)) {
            return status(HttpStatus.OK).body(new ResponseMessage("OK. User setting deleted."));
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR! User setting could not be deleted!"));
        }
    }

}

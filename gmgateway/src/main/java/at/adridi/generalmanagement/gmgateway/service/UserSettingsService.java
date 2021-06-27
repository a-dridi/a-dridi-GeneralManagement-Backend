/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.service;

import at.adridi.generalmanagement.gmgateway.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.gmgateway.model.UserSetting;
import at.adridi.generalmanagement.gmgateway.repository.UserSettingsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author A.Dridi
 */
@Service
@Transactional
@NoArgsConstructor
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    /**
     * Add new user setting.
     *
     * @param newDatabaseNote
     * @return 0 if successful. 1: Passed object is null. 2: Saving failed.
     */
    public Integer addNewUserSetting(UserSetting newUserSetting) {
        if (newUserSetting == null) {
            return 1;
        }
        UserSetting savedObject = this.userSettingsRepository.save(newUserSetting);
        if (savedObject != null) {
            return 0;
        } else {
            return 2;
        }
    }

    /**
     * Update existing user setting with a new value.
     *
     * @param settingsKey
     * @param userId
     * @param newSettingsValue
     * @return true if successful
     */
    public boolean updateUserSetting(long userSettingId, String settingsKey, long userId, String newSettingsValue) {
        if (newSettingsValue == null || settingsKey.trim() == "" || userId == 0) {
            return false;
        }

        UserSetting updatedUserSetting = new UserSetting(userSettingId, settingsKey, newSettingsValue, userId);
        UserSetting savedObject = this.userSettingsRepository.save(updatedUserSetting);
        if (savedObject != null) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public UserSetting getUserSettingbySettingsKey(String settingsKey, long userId) {
        return this.userSettingsRepository.findBySettingKeyAndUserId(settingsKey, userId)
                .orElseThrow(() -> new DataValueNotFoundException("User Setting with the key " + settingsKey + " does Not Exist"));
    }

    /**
     * Get all settings of a user id.
     *
     * @param userId
     * @return
     */
    public List<UserSetting> getAllSettingsOfUser(long userId) {
        return this.userSettingsRepository.findByUserId(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have settings or does not exist"));

    }

    /**
     * Delete an existing user setting
     *
     * @param databaseNoteId
     * @return true if successful
     */
    public boolean deleteByUserSettingBySettingsKey(String settingsKey, long userId) {
        if (settingsKey == null || settingsKey.trim() == "") {
            return false;
        }

        UserSetting userSetting = this.getUserSettingbySettingsKey(settingsKey, userId);
        if (userSetting != null) {
            this.userSettingsRepository.delete(userSetting);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set up default settings values for a user id. Example currency.
     *
     * @param userId
     */
    public void createDefaultUserSettings(long userId) {
        List<UserSetting> defaultUserSettings = new ArrayList<>();

        //Default settings values:
        defaultUserSettings.add(new UserSetting("currency", "USD", userId));
        defaultUserSettings.add(new UserSetting("person1Name", "Person 1", userId));
        defaultUserSettings.add(new UserSetting("person1Ratio", "50", userId));
        defaultUserSettings.add(new UserSetting("person2Name", "Person 2", userId));
        defaultUserSettings.add(new UserSetting("person2Ratio", "50", userId));

        for (UserSetting userSetting : defaultUserSettings) {
            this.addNewUserSetting(userSetting);
        }
    }
}

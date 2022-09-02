/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Save setting for a user (user id).
 *
 * @author A.Dridi
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserSetting {

    private long userSettingId;
    private String settingKey;
    private String settingValue;
    private long userId;

    public UserSetting(String settingKey, String settingValue, long userId) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.userId = userId;
    }

}

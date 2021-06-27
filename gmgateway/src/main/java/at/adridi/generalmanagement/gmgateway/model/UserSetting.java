/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "usersettingidgenerator")
    @TableGenerator(name = "usersettingidgenerator", initialValue = 1000, allocationSize = 2000, table = "sequence_usersettingid")
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

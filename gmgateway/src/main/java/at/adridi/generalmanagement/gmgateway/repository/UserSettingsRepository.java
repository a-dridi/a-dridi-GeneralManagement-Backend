/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.repository;

import at.adridi.generalmanagement.gmgateway.model.UserSetting;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * extends JpaRepository<AppDatabaseNote, Long>
 *
 * @author A.Dridi
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSetting, Long> {

    Optional<List<UserSetting>> findByUserId(long usserId);

    Optional<UserSetting> findBySettingKeyAndUserId(String settingKey, long usserId);

}

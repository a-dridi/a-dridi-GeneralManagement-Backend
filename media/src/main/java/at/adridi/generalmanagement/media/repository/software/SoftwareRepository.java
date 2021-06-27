/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.software;

import at.adridi.generalmanagement.media.model.software.Software;
import at.adridi.generalmanagement.media.model.software.SoftwareOs;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface SoftwareRepository extends JpaRepository<Software, Long>{
    
        Optional<Software> findBySoftwareId(Long softwareId);

    Optional<ArrayList<Software>> findBySoftwareOsAndUserId(SoftwareOs softwareOs, int userId);

    @Query(value = "SELECT * FROM Software WHERE user_id=?1 AND deleted=false ORDER BY software_id DESC", nativeQuery = true)
    Optional<ArrayList<Software>> getAllSoftwareList(int userId);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Software SET deleted=false WHERE software_id=?1", nativeQuery = true)
    void restoreDeletedSoftware(Long softwareId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Software SET title = ?1, software_os_softwareos_id = ?2, manufacturer = ?3, language=?4, version=?5, notice=?6, link_value=?7 WHERE software_id=?8 and user_id=?9", nativeQuery = true)
    void updateSoftwareTableData(String title, Long softwareosId, String manufacturer, String language, String version, String notice, String linkValue, Long softwareId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Software SET software_os_softwareos_id = ?1 WHERE software_os_softwareos_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateOsOfAllSoftwareItems(long newSoftwareOsId, long oldSoftwareOsId, int userId);

}

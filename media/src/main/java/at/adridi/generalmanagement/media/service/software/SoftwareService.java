/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.software;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.software.Software;
import at.adridi.generalmanagement.media.model.software.SoftwareOs;
import at.adridi.generalmanagement.media.repository.software.SoftwareOsRepository;
import at.adridi.generalmanagement.media.repository.software.SoftwareRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of organization DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class SoftwareService {

    @Autowired
    private SoftwareRepository softwareRepository;
    @Autowired
    private SoftwareOsRepository softwareOsRepository;
    @Autowired
    private SoftwareOsService softwareOsService;

    /**
     * Save new software.
     *
     * @param newSoftware
     * @return saved organization object. Null if not successful.
     */
    @Transactional
    public Software save(Software newSoftware) {
        if (newSoftware == null) {
            return null;
        }
        return this.softwareRepository.save(newSoftware);
    }

    /**
     * Get certain Software with the passed id. Throws
     * DataValueNotFoundException if Software is not available.
     *
     * @param id
     * @return
     */
    public Software getSoftwareById(Long id) {
        return this.softwareRepository.findBySoftwareId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Software Does Not Exist"));
    }

    /**
     * Get certain Software with the passed SoftwareOs object.
     * Throws DataValueNotFoundException if Software is not available.
     *
     * @param softwareOs
     * @param userId
     * @return
     */
    public List<Software> getSoftwaresByOsAndUserId(SoftwareOs softwareOs, int userId) {
        return this.softwareRepository.findBySoftwareOsAndUserId(softwareOs, userId).orElseThrow(() -> new DataValueNotFoundException("Software Does Not Exist"));
    }

    /**
     * Get a List of all saved software items of a user
     *
     * @param userId
     * @return
     */
    public List<Software> getAllSoftware(int userId) {
        return this.softwareRepository.getAllSoftwareList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Software items or does not exist"));
    }

    
   /**
    * Update only table data (without attachment info) of an Software item
    * @param title
    * @param softwareosId
    * @param manufacturer
    * @param language
    * @param version
    * @param notice
    * @param linkValue
    * @param softwareId
    * @param userId
    * @return 
    */
    @Transactional
    public int updateSoftwareTableData(String title, Long softwareosId, String manufacturer, String language, String version, String notice, String linkValue, Long softwareId, int userId) {
        try {
            this.softwareRepository.updateSoftwareTableData(title, softwareosId, manufacturer, language, version, notice, linkValue, softwareId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update software os of all software items with
     * oldSoftwareOsId and userId to the new softare os id.
     * And delete old software os object.
     *
     * @param oldSoftwareOsId
     * @param newSoftwareOsId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateSoftwareItemsOsId(long oldSoftwareOsId, long newSoftwareOsId, int userId) {
        if ((oldSoftwareOsId > 0 && newSoftwareOsId > 0 && userId > 0)) {
            SoftwareOs oldSoftwareOs = this.softwareOsService.getSoftwareOsById(oldSoftwareOsId);
            SoftwareOs newSoftwareOs = this.softwareOsService.getSoftwareOsById(newSoftwareOsId);

            if ((oldSoftwareOs != null && !oldSoftwareOs.getOsTitle().equals("")) && (newSoftwareOs != null && !newSoftwareOs.getOsTitle().equals(""))) {
                try {
                    this.softwareRepository.updateOsOfAllSoftwareItems(newSoftwareOsId, oldSoftwareOsId, userId);
                    this.softwareOsRepository.delete(oldSoftwareOs);
                    return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Delete an existing software
     *
     * @param softwareId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long softwareId) {
        if (softwareId == null || softwareId == 0) {
            return false;
        }
        Software software = null;
        try {
            software = this.getSoftwareById(softwareId);
        } catch (DataValueNotFoundException e) {
        }

        if (software != null) {
            software.setDeleted(true);
            try {
                if (this.softwareRepository.save(software) != null) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Set deleted parameter of certain data row. This makes it available again.
     *
     * @param softwareId
     */
    @Transactional
    public void restoreDeletedSoftware(Long softwareId) {
        this.softwareRepository.restoreDeletedSoftware(softwareId);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.software;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.software.SoftwareOs;
import at.adridi.generalmanagement.media.repository.software.SoftwareOsRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of SoftwareOs DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class SoftwareOsService {

    @Autowired
    private SoftwareOsRepository softwareOsRepository;

    /**
     * Save new software os.
     *
     * @param newSoftwareOs
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public SoftwareOs save(SoftwareOs newSoftwareOs) {
        if (newSoftwareOs == null) {
            return null;
        }
        return this.softwareOsRepository.save(newSoftwareOs);
    }

    /**
     * Get certain software os with the passed id. Throws
     * DataValueNotFoundException if software os is not available.
     *
     * @param id
     * @return
     */
    public SoftwareOs getSoftwareOsById(Long id) {
        return this.softwareOsRepository.findBySoftwareosId(id).orElseThrow(() -> new DataValueNotFoundException("Software Os Does Not Exist"));
    }

    /**
     * Get certain software os with the passed category title. Throws
     * DataValueNotFoundException if software os is not available.
     *
     * @param osTitle
     * @return
     */
    public SoftwareOs getSoftwareOsByOsTitle(String osTitle) {
        return this.softwareOsRepository.findByOsTitle(osTitle).orElseThrow(() -> new DataValueNotFoundException("Software Os Does Not Exist"));
    }

    /**
     * Get a List of all saved Software Os items. 
     *
     * @return
     */
    public List<SoftwareOs> getAllSoftwareOs() {
        return this.softwareOsRepository.getAllSoftwareOsList().orElseThrow(() -> new DataValueNotFoundException("Software Os List could not be loaded!"));
    }

    /**
     * Delete an existing software os.
     *
     * @param softwareOsId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long softwareOsId) {
        if (softwareOsId == null || softwareOsId == 0) {
            return false;
        }

        SoftwareOs softwareOs = this.getSoftwareOsById(softwareOsId);

        try {
            this.softwareOsRepository.delete(softwareOs);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

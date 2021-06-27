/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.videoclip;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.videoclip.Videoclip;
import at.adridi.generalmanagement.media.model.videoclip.VideoclipLanguage;
import at.adridi.generalmanagement.media.repository.videoclip.VideoclipLanguageRepository;
import at.adridi.generalmanagement.media.repository.videoclip.VideoclipRepository;
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
public class VideoclipService {

    @Autowired
    private VideoclipRepository videoclipRepository;
    @Autowired
    private VideoclipLanguageRepository videoclipLanguageRepository;
    @Autowired
    private VideoclipLanguageService videoclipLanguageService;

    /**
     * Save new videoclip.
     *
     * @param newVideoclip
     * @return saved videoclip object. Null if not successful.
     */
    @Transactional
    public Videoclip save(Videoclip newVideoclip) {
        if (newVideoclip == null) {
            return null;
        }
        return this.videoclipRepository.save(newVideoclip);
    }

    /**
     * Get certain Videoclip with the passed id. Throws
     * DataValueNotFoundException if Videoclip is not available.
     *
     * @param id
     * @return
     */
    public Videoclip getVideoclipById(Long id) {
        return this.videoclipRepository.findByVideoclipId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Videoclip Does Not Exist"));
    }

    /**
     * Get certain Videoclip with the passed VideoclipLanguage object. Throws
     * DataValueNotFoundException if Videoclip is not available.
     *
     * @param videoclipLanguage
     * @param userId
     * @return
     */
    public List<Videoclip> getVideoclipsByLanguageAndUserId(VideoclipLanguage videoclipLanguage, int userId) {
        return this.videoclipRepository.findByVideoclipLanguageAndUserId(videoclipLanguage, userId).orElseThrow(() -> new DataValueNotFoundException("Videoclip Does Not Exist"));
    }

    /**
     * Get a List of all saved videoclip items of a user
     *
     * @param userId
     * @return
     */
    public List<Videoclip> getAllVideoclip(int userId) {
        return this.videoclipRepository.getAllVideoclipList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Videoclip items or does not exist"));
    }

    /**
     * Update only table data (without attachment info) of an Videoclip item
     *
     * @param interpret
     * @param videoTitle
     * @param videocliplanguageId
     * @param yearDate
     * @param nativeTitle
     * @param linkValue
     * @param videoclipId
     * @param userId
     * @return
     */
    @Transactional
    public int updateVideoclipTableData(String interpret, String videoTitle, Long videocliplanguageId, int yearDate, String nativeTitle, String linkValue, Long videoclipId, int userId) {
        try {
            this.videoclipRepository.updateVideoclipTableData(interpret, videoTitle, videocliplanguageId, yearDate, nativeTitle, linkValue, videoclipId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update videoclip languages of all videoclip items with
     * oldVideoclipLanguageId and userId to the new videoclip language id. And
     * delete old videoclip language object.
     *
     * @param oldVideoclipLanguageId
     * @param newVideoclipLanguageId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateVideoclipItemsVideoclipLanguageId(long oldVideoclipLanguageId, long newVideoclipLanguageId, int userId) {
        if ((oldVideoclipLanguageId > 0 && newVideoclipLanguageId > 0 && userId > 0)) {
            VideoclipLanguage oldVideoclipLanguage = this.videoclipLanguageService.getVideoclipLanguageById(oldVideoclipLanguageId);
            VideoclipLanguage newVideoclipLanguage = this.videoclipLanguageService.getVideoclipLanguageById(newVideoclipLanguageId);

            if ((oldVideoclipLanguage != null && !oldVideoclipLanguage.getLanguageTitle().equals("")) && (newVideoclipLanguage != null && !newVideoclipLanguage.getLanguageTitle().equals(""))) {
                try {
                    this.videoclipRepository.updateLanguageOfAllVideoclipItems(newVideoclipLanguageId, oldVideoclipLanguageId, userId);
                    this.videoclipLanguageRepository.delete(oldVideoclipLanguage);
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
     * Delete an existing videoclip
     *
     * @param videoclipId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long videoclipId) {
        if (videoclipId == null || videoclipId == 0) {
            return false;
        }
        Videoclip videoclip = null;
        try {
            videoclip = this.getVideoclipById(videoclipId);
        } catch (DataValueNotFoundException e) {
        }

        if (videoclip != null) {
            videoclip.setDeleted(true);
            try {
                if (this.videoclipRepository.save(videoclip) != null) {
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
     * @param videoclipId
     */
    @Transactional
    public void restoreDeletedVideoclip(Long videoclipId) {
        this.videoclipRepository.restoreDeletedVideoclip(videoclipId);
    }

}

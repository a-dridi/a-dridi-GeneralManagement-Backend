/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.videoclip;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.videoclip.VideoclipLanguage;
import at.adridi.generalmanagement.media.repository.videoclip.VideoclipLanguageRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of VideoclipLanguage DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class VideoclipLanguageService {

    @Autowired
    private VideoclipLanguageRepository videoclipLanguageRepository;

    /**
     * Save new videoclip language.
     *
     * @param newVideoclipLanguage
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public VideoclipLanguage save(VideoclipLanguage newVideoclipLanguage) {
        if (newVideoclipLanguage == null) {
            return null;
        }
        return this.videoclipLanguageRepository.save(newVideoclipLanguage);
    }

    /**
     * Get certain videoclip language with the passed id. Throws
     * DataValueNotFoundException if videoclip language is not available.
     *
     * @param id
     * @return
     */
    public VideoclipLanguage getVideoclipLanguageById(Long id) {
        return this.videoclipLanguageRepository.findByVideocliplanguageId(id).orElseThrow(() -> new DataValueNotFoundException("Videoclip Language Does Not Exist"));
    }

    /**
     * Get certain videoclip language with the passed language title. Throws
     * DataValueNotFoundException if videoclip language is not available.
     *
     * @param languageTitle
     * @return
     */
    public VideoclipLanguage getVideoclipLanguageByLanguageTitle(String languageTitle) {
        return this.videoclipLanguageRepository.findByLanguageTitle(languageTitle).orElseThrow(() -> new DataValueNotFoundException("Videoclip Language Does Not Exist"));
    }

    /**
     * Get a List of all saved videoclip language
     *
     * @return
     */
    public List<VideoclipLanguage> getAllVideoclipLanguage() {
        return this.videoclipLanguageRepository.getAllVideoclipLanguageList().orElseThrow(() -> new DataValueNotFoundException("Videoclip Language List could not be loaded!"));
    }

    /**
     * Delete an existing videoclip language
     *
     * @param videoLanguageId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long videoclipLanguageId) {
        if (videoclipLanguageId == null || videoclipLanguageId == 0) {
            return false;
        }

        VideoclipLanguage videoclipLanguage = this.getVideoclipLanguageById(videoclipLanguageId);

        try {
            this.videoclipLanguageRepository.delete(videoclipLanguage);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing videoclip language
     *
     * @param languageTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String languageTitle) {
        if (languageTitle == null || languageTitle.trim().isBlank()) {
            return false;
        }

        VideoclipLanguage videoLanguage = this.getVideoclipLanguageByLanguageTitle(languageTitle);

        try {
            this.videoclipLanguageRepository.delete(videoLanguage);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

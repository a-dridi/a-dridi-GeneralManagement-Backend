/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.video;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.video.VideoLanguage;
import at.adridi.generalmanagement.media.repository.video.VideoLanguageRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of VideoLanguage DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class VideoLanguageService {

    @Autowired
    private VideoLanguageRepository videoLanguageRepository;

    /**
     * Save new video language.
     *
     * @param newVideoLanguage
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public VideoLanguage save(VideoLanguage newVideoLanguage) {
        if (newVideoLanguage == null) {
            return null;
        }
        return this.videoLanguageRepository.save(newVideoLanguage);
    }

    /**
     * Get certain video gerne with the passed id. Throws
     * DataValueNotFoundException if video language is not available.
     *
     * @param id
     * @return
     */
    public VideoLanguage getVideoLanguageById(Long id) {
        return this.videoLanguageRepository.findByVideolanguageId(id).orElseThrow(() -> new DataValueNotFoundException("Video Language Does Not Exist"));
    }

    /**
     * Get certain video language with the passed language title. Throws
     * DataValueNotFoundException if video language is not available.
     *
     * @param categoryTitle
     * @return
     */
    public VideoLanguage getVideoLanguageByLanguageTitle(String languageTitle) {
        return this.videoLanguageRepository.findByLanguageTitle(languageTitle).orElseThrow(() -> new DataValueNotFoundException("Video Language Does Not Exist"));
    }

    /**
     * Get a List of all saved video languages
     *
     * @return
     */
    public List<VideoLanguage> getAllVideoLanguage() {
        return this.videoLanguageRepository.getAllVideoLanguageList().orElseThrow(() -> new DataValueNotFoundException("Video Language List could not be loaded!"));
    }

    /**
     * Delete an existing video language
     *
     * @param videoLanguageId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long videoLanguageId) {
        if (videoLanguageId == null || videoLanguageId == 0) {
            return false;
        }
        VideoLanguage videoLanguage = this.getVideoLanguageById(videoLanguageId);

        try {
            this.videoLanguageRepository.delete(videoLanguage);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing video language
     *
     * @param languageTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String languageTitle) {
        if (languageTitle == null || languageTitle.trim().isBlank()) {
            return false;
        }
        VideoLanguage videoLanguage = this.getVideoLanguageByLanguageTitle(languageTitle);

        try {
            this.videoLanguageRepository.delete(videoLanguage);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.video;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.video.VideoGenre;
import at.adridi.generalmanagement.media.repository.video.VideoGenreRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of VideoGenre DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class VideoGenreService {

    @Autowired
    private VideoGenreRepository videoGenreRepository;

    /**
     * Save new video genre.
     *
     * @param newVideoGenre
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public VideoGenre save(VideoGenre newVideoGenre) {
        if (newVideoGenre == null) {
            return null;
        }
        return this.videoGenreRepository.save(newVideoGenre);
    }

    /**
     * Get certain video gerne with the passed id. Throws
     * DataValueNotFoundException if video genre is not available.
     *
     * @param id
     * @return
     */
    public VideoGenre getVideoGenreById(Long id) {
        return this.videoGenreRepository.findByVideogenreId(id).orElseThrow(() -> new DataValueNotFoundException("Video Genre Does Not Exist"));
    }

    /**
     * Get certain video genre with the passed genre title. Throws
     * DataValueNotFoundException if video genre is not available.
     *
     * @param categoryTitle
     * @return
     */
    public VideoGenre getVideoGenreByGenreTitle(String categoryTitle) {
        return this.videoGenreRepository.findByGenreTitle(categoryTitle).orElseThrow(() -> new DataValueNotFoundException("Video Genre Does Not Exist"));
    }

    /**
     * Get a List of all saved video genres
     *
     * @return
     */
    public List<VideoGenre> getAllVideoGenre() {
        return this.videoGenreRepository.getAllVideoGenreList().orElseThrow(() -> new DataValueNotFoundException("Video Genre List could not be loaded!"));
    }

    /**
     * Delete an existing video genre
     *
     * @param videoGenreId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long videoGenreId) {
        if (videoGenreId == null || videoGenreId == 0) {
            return false;
        }
        VideoGenre videoGenre = this.getVideoGenreById(videoGenreId);

        try {
            this.videoGenreRepository.delete(videoGenre);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing video genre
     *
     * @param genreTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String genreTitle) {
        if (genreTitle == null || genreTitle.trim().isBlank()) {
            return false;
        }
        VideoGenre videoGenre = this.getVideoGenreByGenreTitle(genreTitle);

        try {
            this.videoGenreRepository.delete(videoGenre);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

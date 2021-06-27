/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.video;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.video.Video;
import at.adridi.generalmanagement.media.model.video.VideoGenre;
import at.adridi.generalmanagement.media.model.video.VideoLanguage;
import at.adridi.generalmanagement.media.repository.video.VideoGenreRepository;
import at.adridi.generalmanagement.media.repository.video.VideoLanguageRepository;
import at.adridi.generalmanagement.media.repository.video.VideoRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of book DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoGenreRepository videoGenreRepository;
    @Autowired
    private VideoGenreService videoGenreService;
    @Autowired
    private VideoLanguageRepository videoLanguageRepository;
    @Autowired
    private VideoLanguageService videoLanguageService;

    /**
     * Save new Video.
     *
     * @param newVideo
     * @return saved book object. Null if not successful.
     */
    @Transactional
    public Video save(Video newVideo) {
        if (newVideo == null) {
            return null;
        }
        return this.videoRepository.save(newVideo);
    }

    /**
     * Get certain Video with the passed id. Throws DataValueNotFoundException
     * if Video is not available.
     *
     * @param id
     * @return
     */
    public Video getVideoById(Long id) {
        return this.videoRepository.findByVideoId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Video Does Not Exist"));
    }

    /**
     * Get certain Video with the passed VideoGenre object. Throws
     * DataValueNotFoundException if Video is not available.
     *
     * @param videoGenre
     * @param userId
     * @return
     */
    public List<Video> getVideosByGenreAndUserId(VideoGenre videoGenre, int userId) {
        return this.videoRepository.findByVideoGenreAndUserId(videoGenre, userId).orElseThrow(() -> new DataValueNotFoundException("Video Does Not Exist"));
    }

    /**
     * Get certain Video with the passed VideoLanguage object. Throws
     * DataValueNotFoundException if Video is not available.
     *
     * @param videoGenre
     * @param userId
     * @return
     */
    public List<Video> getVideosByLanguageAndUserId(VideoLanguage videoLanguage, int userId) {
        return this.videoRepository.findByVideoLanguageAndUserId(videoLanguage, userId).orElseThrow(() -> new DataValueNotFoundException("Video Does Not Exist"));
    }

    /**
     * Get a List of all saved video items of a user
     *
     * @param userId
     * @return
     */
    public List<Video> getAllVideo(int userId) {
        return this.videoRepository.getAllVideoList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Video items or does not exist"));
    }

    /**
     * Update only table data (without attachment info) of an Video item.
     *
     * @param title
     * @param isOwnProduction
     * @param videolanguageId
     * @param isHd
     * @param videogenreId
     * @param durationLength
     * @param yearDate
     * @param isSeries
     * @param nativeTitle
     * @param linkValue
     * @param videoId
     * @param userId
     * @return
     */
    @Transactional
    public int updateVideoTableData(String title, boolean isOwnProduction, Long videolanguageId, boolean isHd, Long videogenreId, int durationLength, int yearDate, boolean isSeries, String nativeTitle, String linkValue, Long videoId, int userId) {
        try {
            this.videoRepository.updateVideoTableData(title, isOwnProduction, videolanguageId, isHd, videogenreId, durationLength, yearDate, isSeries, nativeTitle, linkValue, videoId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update video genre of all video items with oldVideoGenreId and userId to
     * the new video genre id. And delete old video genre.
     *
     * @param oldVideoGenreId
     * @param newVideoGenreId
     * @param userId
     * @return
     */
    @Transactional
    public int updateVideoItemsGenreId(long oldVideoGenreId, long newVideoGenreId, int userId) {
        if ((oldVideoGenreId > 0 && newVideoGenreId > 0 && userId > 0)) {
            VideoGenre oldVideoGenre = this.videoGenreService.getVideoGenreById(oldVideoGenreId);
            VideoGenre newVideoGenre = this.videoGenreService.getVideoGenreById(newVideoGenreId);

            if ((oldVideoGenre != null && !oldVideoGenre.getGenreTitle().equals("")) && (newVideoGenre != null && !newVideoGenre.getGenreTitle().equals(""))) {
                try {
                    this.videoRepository.updateVideoGenreOfAllVideoItems(newVideoGenreId, oldVideoGenreId, userId);
                    this.videoGenreRepository.delete(oldVideoGenre);
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
     * Update video language of all video items with oldVideoLanguageId and
     * userId to the new video language id. And delete old video language.
     *
     * @param oldVideoLanguageId
     * @param newVideoLanguageId
     * @param userId
     * @return
     */
    @Transactional
    public int updateVideoItemsLanguageId(long oldVideoLanguageId, long newVideoLanguageId, int userId) {
        if ((oldVideoLanguageId > 0 && newVideoLanguageId > 0 && userId > 0)) {
            VideoLanguage oldVideoLanguage = this.videoLanguageService.getVideoLanguageById(oldVideoLanguageId);
            VideoLanguage newVideoLanguage = this.videoLanguageService.getVideoLanguageById(newVideoLanguageId);

            if ((oldVideoLanguage != null && !oldVideoLanguage.getLanguageTitle().equals("")) && (newVideoLanguage != null && !newVideoLanguage.getLanguageTitle().equals(""))) {
                try {
                    this.videoRepository.updateVideoLanguageOfAllVideoItems(newVideoLanguageId, oldVideoLanguageId, userId);
                    this.videoLanguageRepository.delete(oldVideoLanguage);
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
     * Delete an existing video
     *
     * @param videoId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long videoId) {
        if (videoId == null || videoId == 0) {
            return false;
        }
        Video book = null;
        try {
            book = this.getVideoById(videoId);
        } catch (DataValueNotFoundException e) {
        }

        if (book != null) {
            book.setDeleted(true);
            try {
                if (this.videoRepository.save(book) != null) {
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
     * @param videoId
     */
    @Transactional
    public void restoreDeletedVideo(Long videoId) {
        this.videoRepository.restoreDeletedVideo(videoId);
    }

}

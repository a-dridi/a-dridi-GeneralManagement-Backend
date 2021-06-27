/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.video;

import at.adridi.generalmanagement.media.model.video.Video;
import at.adridi.generalmanagement.media.model.video.VideoGenre;
import at.adridi.generalmanagement.media.model.video.VideoGraph;
import at.adridi.generalmanagement.media.model.video.VideoLanguage;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author A.Dridi
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByVideoId(Long videoId);

    Optional<ArrayList<Video>> findByVideoLanguageAndUserId(VideoLanguage videoLanguage, int userId);

    Optional<ArrayList<Video>> findByVideoGenreAndUserId(VideoGenre videoGenre, int userId);

    @Query(value = "SELECT * FROM Video WHERE user_id=?1 AND deleted=false ORDER BY video_id DESC", nativeQuery = true)
    Optional<ArrayList<Video>> getAllVideoList(int userId);

    @Query(value = "SELECT new at.adridi.generalmanagement.media.model.video.VideoGraph(vg.genreTitle, COUNT(v.videoId)) FROM Video AS v LEFT JOIN v.videoGenre AS vg WHERE v.userId=?1 GROUP BY vg.genreTitle ORDER BY vg.genreTitle ASC")
    ArrayList<VideoGraph> getVideoListGroupedByGenre(int userId);

    @Query(value = "SELECT new at.adridi.generalmanagement.media.model.video.VideoGraph(vl.languageTitle, COUNT(v.videoId)) FROM Video AS v LEFT JOIN v.videoLanguage AS vl WHERE v.userId=?1 GROUP BY vl.languageTitle ORDER BY vl.languageTitle ASC")
    ArrayList<VideoGraph> getVideoListGroupedByLanguage(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Video SET deleted=false WHERE video_id=?1", nativeQuery = true)
    void restoreDeletedVideo(Long videoId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Video SET title = ?1, is_own_production = ?2, video_language_videolanguage_id = ?3, is_hd = ?4, video_genre_videogenre_id = ?5, duration_length = ?6, year_date = ?7, is_series = ?8, native_title = ?9, link_value = ?10 WHERE video_id=?11 and user_id=?12", nativeQuery = true)
    void updateVideoTableData(String title, boolean isOwnProduction, Long videolanguageId, boolean isHd, Long videogenreId, int durationLength, int yearDate, boolean isSeries, String nativeTitle, String linkValue, Long videoId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Video SET video_language_videolanguage_id = ?1 WHERE video_language_videolanguage_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateVideoLanguageOfAllVideoItems(long newVideolanguageId, long oldVideolanguageId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Video SET video_genre_videogenre_id = ?1 WHERE video_genre_videogenre_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateVideoGenreOfAllVideoItems(long newVideogenreId, long oldVideogenreId, int userId);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.videoclip;

import at.adridi.generalmanagement.media.model.video.VideoGraph;
import at.adridi.generalmanagement.media.model.videoclip.Videoclip;
import at.adridi.generalmanagement.media.model.videoclip.VideoclipLanguage;
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
public interface VideoclipRepository extends JpaRepository<Videoclip, Long> {

    Optional<Videoclip> findByVideoclipId(Long videoclipId);

    Optional<ArrayList<Videoclip>> findByVideoclipLanguageAndUserId(VideoclipLanguage videoclipLanguage, int userId);

    @Query(value = "SELECT * FROM Videoclip WHERE user_id=?1 AND deleted=false ORDER BY videoclip_id DESC", nativeQuery = true)
    Optional<ArrayList<Videoclip>> getAllVideoclipList(int userId);

    @Query(value = "SELECT new at.adridi.generalmanagement.media.model.video.VideoGraph(vcl.languageTitle, COUNT(vc.videoclipId)) FROM Videoclip AS vc LEFT JOIN vc.videoclipLanguage AS vcl WHERE vc.userId=?1 GROUP BY vcl.languageTitle ORDER BY vcl.languageTitle ASC")
    ArrayList<VideoGraph> getVideoclipListGroupedByLanguage(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Videoclip SET deleted=false WHERE videoclip_id=?1", nativeQuery = true)
    void restoreDeletedVideoclip(Long videoclipId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Videoclip SET interpreter = ?1, video_title = ?2, videoclip_language_videocliplanguage_id = ?3, year_date = ?4, native_title = ?5, link_value = ?6 WHERE videoclip_id=?7 and user_id=?8", nativeQuery = true)
    void updateVideoclipTableData(String interpreter, String videoTitle, Long videocliplanguageId, int yearDate, String nativeTitle, String linkValue, Long videoclipId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Videoclip SET videoclip_language_videocliplanguage_id = ?1 WHERE videoclip_language_videocliplanguage_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateLanguageOfAllVideoclipItems(long newVideocliplanguageId, long oldVideocliplanguageId, int userId);

}

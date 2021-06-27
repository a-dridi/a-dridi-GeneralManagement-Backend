/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.video;

import at.adridi.generalmanagement.media.model.video.VideoLanguage;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface VideoLanguageRepository extends JpaRepository<VideoLanguage, Long> {

    Optional<VideoLanguage> findByVideolanguageId(Long videolanguageId);

    Optional<VideoLanguage> findByLanguageTitle(String languageTitle);

    @Query(value = "SELECT * FROM Video_Language ORDER BY videolanguage_id ASC", nativeQuery = true)
    Optional<ArrayList<VideoLanguage>> getAllVideoLanguageList();

}

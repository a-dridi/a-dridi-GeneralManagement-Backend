/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.videoclip;

import at.adridi.generalmanagement.media.model.videoclip.VideoclipLanguage;
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
public interface VideoclipLanguageRepository extends JpaRepository<VideoclipLanguage, Long> {

    Optional<VideoclipLanguage> findByVideocliplanguageId(Long videocliplanguageId);

    Optional<VideoclipLanguage> findByLanguageTitle(String languageTitle);

    @Query(value = "SELECT * FROM Videoclip_Language ORDER BY videocliplanguage_id ASC", nativeQuery = true)
    Optional<ArrayList<VideoclipLanguage>> getAllVideoclipLanguageList();

}

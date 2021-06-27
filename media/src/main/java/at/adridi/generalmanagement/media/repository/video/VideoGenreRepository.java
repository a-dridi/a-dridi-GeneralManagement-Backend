/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.video;

import at.adridi.generalmanagement.media.model.software.SoftwareOs;
import at.adridi.generalmanagement.media.model.video.VideoGenre;
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
public interface VideoGenreRepository extends JpaRepository<VideoGenre, Long> {

    Optional<VideoGenre> findByVideogenreId(Long videogenreId);

    Optional<VideoGenre> findByGenreTitle(String genreTitle);

    @Query(value = "SELECT * FROM Video_Genre ORDER BY videogenre_id ASC", nativeQuery = true)
    Optional<ArrayList<VideoGenre>> getAllVideoGenreList();

}

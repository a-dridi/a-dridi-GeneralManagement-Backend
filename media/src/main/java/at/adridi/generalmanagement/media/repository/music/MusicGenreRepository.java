/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.music;

import at.adridi.generalmanagement.media.model.music.MusicGenre;
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
public interface MusicGenreRepository extends JpaRepository<MusicGenre, Long> {

    Optional<MusicGenre> findByMusicgenreId(Long musicgenreId);

    Optional<MusicGenre> findByGenreTitle(String genreTitle);

    @Query(value = "SELECT * FROM Music_Genre ORDER BY genre_title ASC", nativeQuery = true)
    Optional<ArrayList<MusicGenre>> getAllMusicGenreList();

}

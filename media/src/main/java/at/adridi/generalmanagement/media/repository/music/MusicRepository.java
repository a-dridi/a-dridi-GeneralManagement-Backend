/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.music;

import at.adridi.generalmanagement.media.model.music.Music;
import at.adridi.generalmanagement.media.model.music.MusicGenre;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    Optional<Music> findByMusicId(Long musicId);

    Optional<ArrayList<Music>> findByMusicGenreAndUserId(MusicGenre musicGenre, int userId);

    @Query(value = "SELECT * FROM Music WHERE user_id=?1 AND deleted=false ORDER BY music_id DESC", nativeQuery = true)
    Optional<ArrayList<Music>> getAllMusicList(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Music SET deleted=false WHERE music_id=?1", nativeQuery = true)
    void restoreDeletedMusic(Long musicId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Music SET interpreter = ?1, songtitle = ?2, year_date = ?3, music_genre_musicgenre_id = ?4, code_value = ?5, link_value = ?6, notice = ?7 WHERE music_id = ?8 and user_id = ?9", nativeQuery = true)
    void updateMusicTableData(String interpreter, String songtitle, Integer yearDate, Long musicGenreId, String codeValue, String linkValue, String notice, Long musicId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Music SET music_genre_musicgenre_id = ?1 WHERE music_genre_musicgenre_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateGenreOfAllMusicItems(long newMusicGenreId, long oldMusicGenreId, int userId);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.music;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.music.MusicGenre;
import at.adridi.generalmanagement.media.repository.music.MusicGenreRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of MusicGenre DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class MusicGenreService {

    @Autowired
    private MusicGenreRepository musicGenreRepository;

    /**
     * Save new music genre.
     *
     * @param newMusicGenre
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public MusicGenre save(MusicGenre newMusicGenre) {
        if (newMusicGenre == null) {
            return null;
        }
        return this.musicGenreRepository.save(newMusicGenre);
    }

    /**
     * Get certain music genre with the passed id. Throws
     * DataValueNotFoundException if music genre is not available.
     *
     * @param id
     * @return
     */
    public MusicGenre getMusicGenreById(Long id) {
        return this.musicGenreRepository.findByMusicgenreId(id).orElseThrow(() -> new DataValueNotFoundException("Music Genre Does Not Exist"));
    }

    /**
     * Get certain music genre with the passed genre title. Throws
     * DataValueNotFoundException if music genre is not available.
     *
     * @param genreTitle
     * @return
     */
    public MusicGenre getMusicGenreByCategoryTitle(String genreTitle) {
        return this.musicGenreRepository.findByGenreTitle(genreTitle).orElseThrow(() -> new DataValueNotFoundException("Music Genre Does Not Exist"));
    }

    /**
     * Get a List of all saved music genres
     *
     * @return
     */
    public List<MusicGenre> getAllMusicGenre() {
        return this.musicGenreRepository.getAllMusicGenreList().orElseThrow(() -> new DataValueNotFoundException("Music Genre List could not be loaded!"));
    }

    /**
     * Delete an existing music genre
     *
     * @param musicGenreId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long musicGenreId) {
        if (musicGenreId == null || musicGenreId == 0) {
            return false;
        }

        MusicGenre musicGenre = this.getMusicGenreById(musicGenreId);

        try {
            this.musicGenreRepository.delete(musicGenre);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

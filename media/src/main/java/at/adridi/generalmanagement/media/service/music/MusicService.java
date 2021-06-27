/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.music;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.music.Music;
import at.adridi.generalmanagement.media.model.music.MusicGenre;
import at.adridi.generalmanagement.media.repository.music.MusicGenreRepository;
import at.adridi.generalmanagement.media.repository.music.MusicRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Music DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private MusicGenreRepository musicGenreRepository;
    @Autowired
    private MusicGenreService musicGenreService;

    /**
     * Save new music.
     *
     * @param newMusic
     * @return saved music object. Null if not successful.
     */
    @Transactional
    public Music save(Music newMusic) {
        if (newMusic == null) {
            return null;
        }
        return this.musicRepository.save(newMusic);
    }

    /**
     * Get certain Music with the passed id. Throws DataValueNotFoundException
     * if Music is not available.
     *
     * @param id
     * @return
     */
    public Music getMusicById(Long id) {
        return this.musicRepository.findByMusicId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Music Does Not Exist"));
    }

    /**
     * Get certain Music with the passed MusicGenre object. Throws
     * DataValueNotFoundException if Music is not available
     *
     * @param musicGenre
     * @param userId
     * @return
     */
    public List<Music> getMusicsByGenreAndUserId(MusicGenre musicGenre, int userId) {
        return this.musicRepository.findByMusicGenreAndUserId(musicGenre, userId).orElseThrow(() -> new DataValueNotFoundException("Music Does Not Exist"));
    }

    /**
     * Get a List of all saved organization items of a user
     *
     * @param userId
     * @return
     */
    public List<Music> getAllMusic(int userId) {
        return this.musicRepository.getAllMusicList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Music items or does not exist"));
    }

    /**
     * Update only table data (without attachment info) of an Music item
     *
     * @param interpret
     * @param songtitle
     * @param yearDate
     * @param musicGenreId
     * @param codeValue
     * @param linkValue
     * @param notice
     * @param musicId
     * @param userId
     * @return
     */
    @Transactional
    public int updateMusicTableData(String interpret, String songtitle, Integer yearDate, Long musicGenreId, String codeValue, String linkValue, String notice, Long musicId, int userId) {
        try {
            this.musicRepository.updateMusicTableData(interpret, songtitle, yearDate, musicGenreId, codeValue, linkValue, notice, musicId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update music genre of all music items with oldMusicGenreId and userId to
     * the new music genre id. And delete old organization category object.
     *
     * @param oldMusicGenreId
     * @param newMusicGenreId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateMusicItemsGenreId(long oldMusicGenreId, long newMusicGenreId, int userId) {
        if ((oldMusicGenreId > 0 && newMusicGenreId > 0 && userId > 0)) {
            MusicGenre oldMusicGenre = this.musicGenreService.getMusicGenreById(oldMusicGenreId);
            MusicGenre newMusicGenre = this.musicGenreService.getMusicGenreById(newMusicGenreId);

            if ((oldMusicGenre != null && !oldMusicGenre.getGenreTitle().equals("")) && (newMusicGenre != null && !newMusicGenre.getGenreTitle().equals(""))) {
                try {
                    this.musicRepository.updateGenreOfAllMusicItems(newMusicGenreId, oldMusicGenreId, userId);
                    this.musicGenreRepository.delete(oldMusicGenre);
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
     * Delete an existing music
     *
     * @param musicId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long musicId) {
        if (musicId == null || musicId == 0) {
            return false;
        }
        Music music = null;
        try {
            music = this.getMusicById(musicId);
        } catch (DataValueNotFoundException e) {
        }

        if (music != null) {
            music.setDeleted(true);
            try {
                if (this.musicRepository.save(music) != null) {
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
     * @param musicId
     */
    @Transactional
    public void restoreDeletedMusic(Long musicId) {
        this.musicRepository.restoreDeletedMusic(musicId);
    }

}

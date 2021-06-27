/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.music;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.music.Music;
import at.adridi.generalmanagement.media.model.music.MusicGenre;
import at.adridi.generalmanagement.media.service.music.MusicService;
import at.adridi.generalmanagement.media.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Music - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/all/{userId}")
    public ResponseEntity<List<Music>> getAllMusic(@PathVariable int userId) {
        List<Music> musicList = new ArrayList<>();
        try {
            musicList = this.musicService.getAllMusic(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(musicList)) {
            return status(HttpStatus.OK).body(musicList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Music>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/get/byId/{id}")
    public ResponseEntity<Music> getMusicById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.musicService.getMusicById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Music());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/get/byCategory/{userId}")
    public ResponseEntity<List<Music>> getAllMusicByMusicGenre(@RequestBody String musicGenreJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        MusicGenre musicGenre;
        try {
            musicGenre = objectMapper.readValue(musicGenreJson, MusicGenre.class);
            List<Music> musicList = new ArrayList<>();
            try {
                musicList = this.musicService.getMusicsByGenreAndUserId(musicGenre, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(musicList)) {
                return status(HttpStatus.OK).body(musicList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Music>());
            }
        } catch (IOException ex) {
            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Music>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/add")
    public ResponseEntity<Music> addMusic(@RequestBody String newMusicJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Music newMusic;
        Music savedMusic;
        try {
            newMusic = objectMapper.readValue(newMusicJson, Music.class);
            savedMusic = this.musicService.save(newMusic);
            return ResponseEntity.status(HttpStatus.OK).body(savedMusic);
        } catch (IOException ex) {
            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Music());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/update")
    public ResponseEntity<Music> updateMusic(@RequestBody String updatedMusicJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Music updatedMusic;
        Music savedMusic;
        try {
            updatedMusic = objectMapper.readValue(updatedMusicJson, Music.class);
            savedMusic = this.musicService.save(updatedMusic);
            return ResponseEntity.status(HttpStatus.OK).body(savedMusic);
        } catch (IOException ex) {
            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Music());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateMusicTableData(@RequestBody String updatedMusicJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Music updatedMusic;
        try {
            updatedMusic = objectMapper.readValue(updatedMusicJson, Music.class);
            if (this.musicService.updateMusicTableData(updatedMusic.getInterpreter(), updatedMusic.getSongtitle(), updatedMusic.getYearDate(), updatedMusic.getMusicGenre().getMusicgenreId(), updatedMusic.getCodeValue(), updatedMusic.getLinkValue(), updatedMusic.getNotice(), updatedMusic.getMusicId(), updatedMusic.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Music updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Music could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(MusicController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Music could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/updateMusicGenres/{oldMusicGenreId}/{newMusicGenreId}/{userId}")
    public ResponseEntity<ResponseMessage> updateGenreOfMusicItems(@PathVariable long oldMusicGenreId, @PathVariable long newMusicGenreId, @PathVariable int userId) {
        if (this.musicService.updateMusicItemsGenreId(oldMusicGenreId, newMusicGenreId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Genre of Music items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Genre of Music items COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/delete/{musicId}")
    public ResponseEntity<ResponseMessage> deleteMusicById(@PathVariable Long musicId) {
        if (this.musicService.deleteById(musicId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Music item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Music item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSIC + "/restore/{musicId}")
    public ResponseEntity<ResponseMessage> restoreDeletedMusicItem(@PathVariable Long musicId) {
        try {
            this.musicService.restoreDeletedMusic(musicId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(musicId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

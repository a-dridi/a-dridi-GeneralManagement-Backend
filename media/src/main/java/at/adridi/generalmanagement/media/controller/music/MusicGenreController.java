/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.music;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.music.MusicGenre;
import at.adridi.generalmanagement.media.service.music.MusicGenreService;
import at.adridi.generalmanagement.media.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
 * API: MusicGenre - Categories for Organization
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class MusicGenreController {

    @Autowired
    private MusicGenreService musicGenreService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSICGENRE + "/all")
    public ResponseEntity<List<MusicGenre>> getAllMusicGenre() {
        List<MusicGenre> organizationCategoryList = new ArrayList<>();
        try {
            organizationCategoryList = this.musicGenreService.getAllMusicGenre();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationCategoryList)) {
            return status(HttpStatus.OK).body(organizationCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<MusicGenre>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSICGENRE + "/get/byId/{id}")
    public ResponseEntity<MusicGenre> getMusicGenreById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.musicGenreService.getMusicGenreById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new MusicGenre());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSICGENRE + "/get/byTitle/{title}")
    public ResponseEntity<MusicGenre> getAllMusicGenreByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.musicGenreService.getMusicGenreByCategoryTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new MusicGenre());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSICGENRE + "/add")
    public ResponseEntity<MusicGenre> addMusicGenre(@RequestBody String newMusicGenreString) {
        if (newMusicGenreString != null || newMusicGenreString.trim().equals("")) {
            MusicGenre newMusicGenre = new MusicGenre();
            newMusicGenre.setGenreTitle(newMusicGenreString);
            MusicGenre createdMusicGenre = this.musicGenreService.save(newMusicGenre);
            if (createdMusicGenre != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdMusicGenre);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MusicGenre());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MusicGenre());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSICGENRE + "/update")
    public ResponseEntity<MusicGenre> updateMusicGenre(@RequestBody String updatedMusicGenreString) {
        ObjectMapper objectMapper = new ObjectMapper();
        MusicGenre updatedMusicGenre;
        try {
            updatedMusicGenre = objectMapper.readValue(updatedMusicGenreString, MusicGenre.class);
            MusicGenre newUpdatedMusicGenre = this.musicGenreService.save(updatedMusicGenre);
            if (newUpdatedMusicGenre != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedMusicGenre);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MusicGenre());
            }
        } catch (IOException ex) {
            Logger.getLogger(MusicGenreController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MusicGenre());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_MUSICGENRE + "/delete/byId/{musicGenreId}")
    public ResponseEntity<ResponseMessage> deleteMusicGenreById(@PathVariable Long musicGenreId) {
        if (this.musicGenreService.deleteById(musicGenreId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your music genre was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Music genre cannot be null!"));
        }
    }

}

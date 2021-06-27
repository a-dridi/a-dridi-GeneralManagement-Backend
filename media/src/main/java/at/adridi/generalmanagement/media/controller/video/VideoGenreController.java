/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.video;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.video.VideoGenre;
import at.adridi.generalmanagement.media.service.video.VideoGenreService;
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
 * API: VideoGenre - Genre for Video
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoGenreController {

    @Autowired
    private VideoGenreService videoGenreService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/all")
    public ResponseEntity<List<VideoGenre>> getAllVideoGenre() {
        List<VideoGenre> bookCategoryList = new ArrayList<>();
        try {
            bookCategoryList = this.videoGenreService.getAllVideoGenre();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(bookCategoryList)) {
            return status(HttpStatus.OK).body(bookCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<VideoGenre>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/get/byId/{id}")
    public ResponseEntity<VideoGenre> getVideoGenreById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.videoGenreService.getVideoGenreById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new VideoGenre());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/get/byTitle/{title}")
    public ResponseEntity<VideoGenre> getAllVideoGenreByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.videoGenreService.getVideoGenreByGenreTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new VideoGenre());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/add")
    public ResponseEntity<VideoGenre> addVideoGenre(@RequestBody String newVideoGenreString) {
        if (newVideoGenreString != null || newVideoGenreString.trim().equals("")) {
            VideoGenre newVideoGenre = new VideoGenre();
            newVideoGenre.setGenreTitle(newVideoGenreString);
            VideoGenre createdVideoGenre = this.videoGenreService.save(newVideoGenre);
            if (createdVideoGenre != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdVideoGenre);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoGenre());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoGenre());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/update")
    public ResponseEntity<VideoGenre> updateVideoGenre(@RequestBody String updatedVideoGenreString) {
        ObjectMapper objectMapper = new ObjectMapper();
        VideoGenre updatedVideoGenre;
        try {
            updatedVideoGenre = objectMapper.readValue(updatedVideoGenreString, VideoGenre.class);
            VideoGenre newUpdatedVideoGenre = this.videoGenreService.save(updatedVideoGenre);
            if (newUpdatedVideoGenre != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedVideoGenre);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoGenre());
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoGenreController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoGenre());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/delete/byId/{videoGenreId}")
    public ResponseEntity<ResponseMessage> deleteVideoGenreById(@PathVariable Long videoGenreId) {
        if (this.videoGenreService.deleteById(videoGenreId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your video genre was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video genre cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGENRE + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteVideoGenreByTitle(@PathVariable String title) {
        if (this.videoGenreService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your video genre was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video genre does not exists!"));
        }
    }

}

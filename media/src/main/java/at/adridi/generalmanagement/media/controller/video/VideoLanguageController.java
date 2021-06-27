/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.video;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.video.VideoLanguage;
import at.adridi.generalmanagement.media.service.video.VideoLanguageService;
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
 * API: VideoLanguage - Language for Video
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoLanguageController {

    @Autowired
    private VideoLanguageService videoLanguageService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/all")
    public ResponseEntity<List<VideoLanguage>> getAllVideoLanguage() {
        List<VideoLanguage> bookCategoryList = new ArrayList<>();
        try {
            bookCategoryList = this.videoLanguageService.getAllVideoLanguage();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(bookCategoryList)) {
            return status(HttpStatus.OK).body(bookCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<VideoLanguage>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/get/byId/{id}")
    public ResponseEntity<VideoLanguage> getVideoLanguageById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.videoLanguageService.getVideoLanguageById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new VideoLanguage());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/get/byTitle/{title}")
    public ResponseEntity<VideoLanguage> getAllVideoLanguageByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.videoLanguageService.getVideoLanguageByLanguageTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new VideoLanguage());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/add")
    public ResponseEntity<VideoLanguage> addVideoLanguage(@RequestBody String newVideoLanguageString) {
        if (newVideoLanguageString != null || newVideoLanguageString.trim().equals("")) {
            VideoLanguage newVideoLanguage = new VideoLanguage();
            newVideoLanguage.setLanguageTitle(newVideoLanguageString);
            VideoLanguage createdVideoLanguage = this.videoLanguageService.save(newVideoLanguage);
            if (createdVideoLanguage != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdVideoLanguage);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoLanguage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoLanguage());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/update")
    public ResponseEntity<VideoLanguage> updateVideoLanguage(@RequestBody String updatedVideoLanguageString) {
        ObjectMapper objectMapper = new ObjectMapper();
        VideoLanguage updatedVideoLanguage;
        try {
            updatedVideoLanguage = objectMapper.readValue(updatedVideoLanguageString, VideoLanguage.class);
            VideoLanguage newUpdatedVideoLanguage = this.videoLanguageService.save(updatedVideoLanguage);
            if (newUpdatedVideoLanguage != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedVideoLanguage);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoLanguage());
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoLanguageController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoLanguage());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/delete/byId/{videoLanguageId}")
    public ResponseEntity<ResponseMessage> deleteVideoLanguageById(@PathVariable Long videoLanguageId) {
        if (this.videoLanguageService.deleteById(videoLanguageId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your video language was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video language cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOLANGUAGE + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteVideoLanguageByTitle(@PathVariable String title) {
        if (this.videoLanguageService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your video language was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video language does not exists!"));
        }
    }

}

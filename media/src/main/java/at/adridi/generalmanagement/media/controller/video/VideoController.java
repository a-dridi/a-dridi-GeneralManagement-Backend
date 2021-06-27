/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.video;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.video.Video;
import at.adridi.generalmanagement.media.model.video.VideoGenre;
import at.adridi.generalmanagement.media.model.video.VideoLanguage;
import at.adridi.generalmanagement.media.service.video.VideoService;
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
 * API: Video - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/all/{userId}")
    public ResponseEntity<List<Video>> getAllVideo(@PathVariable int userId) {
        List<Video> videoList = new ArrayList<>();
        try {
            videoList = this.videoService.getAllVideo(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(videoList)) {
            return status(HttpStatus.OK).body(videoList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Video>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/get/byId/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.videoService.getVideoById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Video());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/get/byGenre/{userId}")
    public ResponseEntity<List<Video>> getAllVideoByVideoGenre(@RequestBody String videoGenreJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        VideoGenre videoGenre;
        try {
            videoGenre = objectMapper.readValue(videoGenreJson, VideoGenre.class);
            List<Video> videoList = new ArrayList<>();
            try {
                videoList = this.videoService.getVideosByGenreAndUserId(videoGenre, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(videoList)) {
                return status(HttpStatus.OK).body(videoList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Video>());
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Video>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/get/byLanguage/{userId}")
    public ResponseEntity<List<Video>> getAllVideoByVideoLanguage(@RequestBody String videoLanguageJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        VideoLanguage videoLanguage;
        try {
            videoLanguage = objectMapper.readValue(videoLanguageJson, VideoLanguage.class);
            List<Video> videoList = new ArrayList<>();
            try {
                videoList = this.videoService.getVideosByLanguageAndUserId(videoLanguage, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(videoList)) {
                return status(HttpStatus.OK).body(videoList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Video>());
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Video>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/add")
    public ResponseEntity<Video> addVideo(@RequestBody String newVideoJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Video newVideo;
        Video savedVideo;
        try {
            newVideo = objectMapper.readValue(newVideoJson, Video.class);
            savedVideo = this.videoService.save(newVideo);
            return ResponseEntity.status(HttpStatus.OK).body(savedVideo);
        } catch (IOException ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Video());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/update")
    public ResponseEntity<Video> updateVideo(@RequestBody String updatedVideoJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Video updatedVideo;
        Video savedVideo;
        try {
            updatedVideo = objectMapper.readValue(updatedVideoJson, Video.class);
            savedVideo = this.videoService.save(updatedVideo);
            return ResponseEntity.status(HttpStatus.OK).body(savedVideo);
        } catch (IOException ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Video());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateVideoTableData(@RequestBody String updatedVideoJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Video updatedVideo;
        try {
            updatedVideo = objectMapper.readValue(updatedVideoJson, Video.class);
            if (this.videoService.updateVideoTableData(updatedVideo.getTitle(), updatedVideo.isOwnProduction(), updatedVideo.getVideoLanguage().getVideolanguageId(), updatedVideo.isHd(), updatedVideo.getVideoGenre().getVideogenreId(), updatedVideo.getDurationLength(), updatedVideo.getYearDate(), updatedVideo.isSeries(), updatedVideo.getNativeTitle(), updatedVideo.getLinkValue(), updatedVideo.getVideoId(), updatedVideo.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Video updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/updateVideoGenres/{oldVideoGenreId}/{newVideoGenreId}/{userId}")
    public ResponseEntity<ResponseMessage> updateGenreOfVideoItems(@PathVariable long oldVideoGenreId, @PathVariable long newVideoGenreId, @PathVariable int userId) {
        if (this.videoService.updateVideoItemsGenreId(oldVideoGenreId, newVideoGenreId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Genres for Video items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Genres for Video items COULD NOT be updated!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/updateVideoLanguages/{oldVideoLanguageId}/{newVideoLanguageId}/{userId}")
    public ResponseEntity<ResponseMessage> updateLanguageOfVideoItems(@PathVariable long oldVideoLanguageId, @PathVariable long newVideoLanguageId, @PathVariable int userId) {
        if (this.videoService.updateVideoItemsLanguageId(oldVideoLanguageId, newVideoLanguageId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Languages for Video items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Languages for Video items COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/delete/{videoId}")
    public ResponseEntity<ResponseMessage> deleteVideoById(@PathVariable Long videoId) {
        if (this.videoService.deleteById(videoId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Video item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Video item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEO + "/restore/{videoId}")
    public ResponseEntity<ResponseMessage> restoreDeletedVideoItem(@PathVariable Long videoId) {
        try {
            this.videoService.restoreDeletedVideo(videoId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(videoId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

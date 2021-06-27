/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.video;

import at.adridi.generalmanagement.media.model.video.VideoGraph;
import at.adridi.generalmanagement.media.service.video.VideoGraphService;
import at.adridi.generalmanagement.media.util.ApiEndpoints;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Book - Graph
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoGraphController {

    @Autowired
    private VideoGraphService videoGraphService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGRAPH + "/amountBy/video/genre/{userId}")
    public ResponseEntity<List<VideoGraph>> getVideoGenreAmountList(@PathVariable int userId) {
        List<VideoGraph> videoList = new ArrayList<>();
        try {
            videoList = this.videoGraphService.getAllVideoGenreAmountList(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!CollectionUtils.isEmpty(videoList)) {
            return status(HttpStatus.OK).body(videoList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<VideoGraph>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGRAPH + "/amountBy/video/language/{userId}")
    public ResponseEntity<List<VideoGraph>> getVideoLanguageAmountList(@PathVariable int userId) {
        List<VideoGraph> videoList = new ArrayList<>();
        try {
            videoList = this.videoGraphService.getAllVideoLanguageAmountList(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!CollectionUtils.isEmpty(videoList)) {
            return status(HttpStatus.OK).body(videoList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<VideoGraph>());
        }
    }
}

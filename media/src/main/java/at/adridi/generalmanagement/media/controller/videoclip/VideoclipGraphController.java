/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.videoclip;

import at.adridi.generalmanagement.media.model.video.VideoGraph;
import at.adridi.generalmanagement.media.model.videoclip.Videoclip;
import at.adridi.generalmanagement.media.service.videoclip.VideoclipGraphService;
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
 * API: Videoclip - Graph
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoclipGraphController {

    @Autowired
    private VideoclipGraphService videoclipGraphService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOGRAPH + "/amountBy/videoclip/language/{userId}")
    public ResponseEntity<List<VideoGraph>> getVideoclipLanguageAmountList(@PathVariable int userId) {
        List<VideoGraph> videoclipList = new ArrayList<>();
        try {
            videoclipList = this.videoclipGraphService.getAllVideoclipLanguageAmountList(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!CollectionUtils.isEmpty(videoclipList)) {
            return status(HttpStatus.OK).body(videoclipList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<VideoGraph>());
        }
    }

}

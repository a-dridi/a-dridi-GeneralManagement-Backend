/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.videoclip;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.videoclip.VideoclipLanguage;
import at.adridi.generalmanagement.media.service.videoclip.VideoclipLanguageService;
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
 * API: VideoclipLanguage - Languages for Video Clip Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoclipLanguageController {

    @Autowired
    private VideoclipLanguageService videoclipLanguageService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/all")
    public ResponseEntity<List<VideoclipLanguage>> getAllVideoclipLanguage() {
        List<VideoclipLanguage> videoclipLanguageList = new ArrayList<>();
        try {
            videoclipLanguageList = this.videoclipLanguageService.getAllVideoclipLanguage();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(videoclipLanguageList)) {
            return status(HttpStatus.OK).body(videoclipLanguageList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<VideoclipLanguage>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/get/byId/{id}")
    public ResponseEntity<VideoclipLanguage> getVideoclipLanguageById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.videoclipLanguageService.getVideoclipLanguageById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new VideoclipLanguage());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/get/byTitle/{title}")
    public ResponseEntity<VideoclipLanguage> getAllVideoclipLanguageByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.videoclipLanguageService.getVideoclipLanguageByLanguageTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new VideoclipLanguage());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/add")
    public ResponseEntity<VideoclipLanguage> addVideoclipLanguage(@RequestBody String newVideoclipLanguageString) {
        if (newVideoclipLanguageString != null || newVideoclipLanguageString.trim().equals("")) {
            VideoclipLanguage newVideoclipLanguage = new VideoclipLanguage();
            newVideoclipLanguage.setLanguageTitle(newVideoclipLanguageString);
            VideoclipLanguage createdVideoclipLanguage = this.videoclipLanguageService.save(newVideoclipLanguage);
            if (createdVideoclipLanguage != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdVideoclipLanguage);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoclipLanguage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoclipLanguage());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/update")
    public ResponseEntity<VideoclipLanguage> updateVideoclipLanguage(@RequestBody String updatedVideoclipLanguageString) {
        ObjectMapper objectMapper = new ObjectMapper();
        VideoclipLanguage updatedVideoclipLanguage;
        try {
            updatedVideoclipLanguage = objectMapper.readValue(updatedVideoclipLanguageString, VideoclipLanguage.class);
            VideoclipLanguage newUpdatedVideoclipLanguage = this.videoclipLanguageService.save(updatedVideoclipLanguage);
            if (newUpdatedVideoclipLanguage != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedVideoclipLanguage);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoclipLanguage());
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoclipLanguageController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VideoclipLanguage());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/delete/byId/{videoclipLanguageId}")
    public ResponseEntity<ResponseMessage> deleteVideoclipLanguageById(@PathVariable Long videoclipLanguageId) {
        if (this.videoclipLanguageService.deleteById(videoclipLanguageId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your videoclip language was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Videoclip Language cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIPLANGUAGE + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteVideoclipLanguageByTitle(@PathVariable String title) {
        if (this.videoclipLanguageService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your videoclip language was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Videoclip language does not exists!"));
        }
    }

}

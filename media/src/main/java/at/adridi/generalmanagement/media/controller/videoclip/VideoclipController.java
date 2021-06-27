/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.videoclip;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.videoclip.Videoclip;
import at.adridi.generalmanagement.media.model.videoclip.VideoclipLanguage;
import at.adridi.generalmanagement.media.service.videoclip.VideoclipService;
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
 * API: Videoclip - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class VideoclipController {

    @Autowired
    private VideoclipService videoclipService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/all/{userId}")
    public ResponseEntity<List<Videoclip>> getAllVideoclip(@PathVariable int userId) {
        List<Videoclip> videoclipList = new ArrayList<>();
        try {
            videoclipList = this.videoclipService.getAllVideoclip(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(videoclipList)) {
            return status(HttpStatus.OK).body(videoclipList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Videoclip>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/get/byId/{id}")
    public ResponseEntity<Videoclip> getVideoclipById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.videoclipService.getVideoclipById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Videoclip());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/get/byLanguage/{userId}")
    public ResponseEntity<List<Videoclip>> getAllVideoclipByVideoclipLanguage(@RequestBody String videoclipLanguageJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        VideoclipLanguage videoclipLanguage;
        try {
            videoclipLanguage = objectMapper.readValue(videoclipLanguageJson, VideoclipLanguage.class);
            List<Videoclip> videoclipList = new ArrayList<>();
            try {
                videoclipList = this.videoclipService.getVideoclipsByLanguageAndUserId(videoclipLanguage, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(videoclipList)) {
                return status(HttpStatus.OK).body(videoclipList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Videoclip>());
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoclipController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Videoclip>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/add")
    public ResponseEntity<Videoclip> addVideoclip(@RequestBody String newVideoclipJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Videoclip newVideoclip;
        Videoclip savedVideoclip;
        try {
            newVideoclip = objectMapper.readValue(newVideoclipJson, Videoclip.class);
            savedVideoclip = this.videoclipService.save(newVideoclip);
            return ResponseEntity.status(HttpStatus.OK).body(savedVideoclip);
        } catch (IOException ex) {
            Logger.getLogger(VideoclipController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Videoclip());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/update")
    public ResponseEntity<Videoclip> updateVideoclip(@RequestBody String updatedVideoclipJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Videoclip updatedVideoclip;
        Videoclip savedVideoclip;
        try {
            updatedVideoclip = objectMapper.readValue(updatedVideoclipJson, Videoclip.class);
            savedVideoclip = this.videoclipService.save(updatedVideoclip);
            return ResponseEntity.status(HttpStatus.OK).body(savedVideoclip);
        } catch (IOException ex) {
            Logger.getLogger(VideoclipController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Videoclip());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateVideoclipTableData(@RequestBody String updatedVideoclipJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Videoclip updatedVideoclip;
        try {
            updatedVideoclip = objectMapper.readValue(updatedVideoclipJson, Videoclip.class);
            if (this.videoclipService.updateVideoclipTableData(updatedVideoclip.getInterpreter(), updatedVideoclip.getVideoTitle(), updatedVideoclip.getVideoclipLanguage().getVideocliplanguageId(), updatedVideoclip.getYearDate(), updatedVideoclip.getNativeTitle(), updatedVideoclip.getLinkValue(), updatedVideoclip.getVideoclipId(), updatedVideoclip.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Videoclip updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Videoclip could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(VideoclipController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Videoclip could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/updateVideoclipLanguages/{oldVideoclipLanguageId}/{newVideoclipLanguageId}/{userId}")
    public ResponseEntity<ResponseMessage> updateLanguageOfVideoclipItems(@PathVariable long oldVideoclipLanguageId, @PathVariable long newVideoclipLanguageId, @PathVariable int userId) {
        if (this.videoclipService.updateVideoclipItemsVideoclipLanguageId(oldVideoclipLanguageId, newVideoclipLanguageId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Languages for Videoclip items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Languages for Videoclip items COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/delete/{videoclipId}")
    public ResponseEntity<ResponseMessage> deleteVideoclipById(@PathVariable Long videoclipId) {
        if (this.videoclipService.deleteById(videoclipId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Videoclip item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Videoclip item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_VIDEOCLIP + "/restore/{videoclipId}")
    public ResponseEntity<ResponseMessage> restoreDeletedVideoclipItem(@PathVariable Long videoclipId) {
        try {
            this.videoclipService.restoreDeletedVideoclip(videoclipId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(videoclipId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

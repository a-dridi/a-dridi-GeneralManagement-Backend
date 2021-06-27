/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.software;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.software.Software;
import at.adridi.generalmanagement.media.model.software.SoftwareOs;
import at.adridi.generalmanagement.media.service.software.SoftwareService;
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
 * API: Software - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/all/{userId}")
    public ResponseEntity<List<Software>> getAllSoftware(@PathVariable int userId) {
        List<Software> softwareList = new ArrayList<>();
        try {
            softwareList = this.softwareService.getAllSoftware(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(softwareList)) {
            return status(HttpStatus.OK).body(softwareList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Software>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/get/byId/{id}")
    public ResponseEntity<Software> getSoftwareById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.softwareService.getSoftwareById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Software());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/get/byOs/{userId}")
    public ResponseEntity<List<Software>> getAllSoftwareBySoftwareCategory(@RequestBody String softwareOsJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        SoftwareOs softwareOs;
        try {
            softwareOs = objectMapper.readValue(softwareOsJson, SoftwareOs.class);
            List<Software> softwareList = new ArrayList<>();
            try {
                softwareList = this.softwareService.getSoftwaresByOsAndUserId(softwareOs, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(softwareList)) {
                return status(HttpStatus.OK).body(softwareList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Software>());
            }
        } catch (IOException ex) {
            Logger.getLogger(SoftwareController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Software>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/add")
    public ResponseEntity<Software> addSoftware(@RequestBody String newSoftwareJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Software newSoftware;
        Software savedSoftware;
        try {
            newSoftware = objectMapper.readValue(newSoftwareJson, Software.class);
            savedSoftware = this.softwareService.save(newSoftware);
            return ResponseEntity.status(HttpStatus.OK).body(savedSoftware);
        } catch (IOException ex) {
            Logger.getLogger(SoftwareController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Software());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/update")
    public ResponseEntity<Software> updateSoftware(@RequestBody String updatedSoftwareJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Software updatedSoftware;
        Software savedSoftware;
        try {
            updatedSoftware = objectMapper.readValue(updatedSoftwareJson, Software.class);
            savedSoftware = this.softwareService.save(updatedSoftware);
            return ResponseEntity.status(HttpStatus.OK).body(savedSoftware);
        } catch (IOException ex) {
            Logger.getLogger(SoftwareController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Software());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateSoftwareTableData(@RequestBody String updatedSoftwareJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Software updatedSoftware;
        try {
            updatedSoftware = objectMapper.readValue(updatedSoftwareJson, Software.class);
            if (this.softwareService.updateSoftwareTableData(updatedSoftware.getTitle(), updatedSoftware.getSoftwareOs().getSoftwareosId(), updatedSoftware.getManufacturer(), updatedSoftware.getLanguage(), updatedSoftware.getVersion(), updatedSoftware.getNotice(), updatedSoftware.getLinkValue(), updatedSoftware.getSoftwareId(), updatedSoftware.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Software updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Software could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(SoftwareController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Software could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/updateSoftwareOs/{oldSoftwareOsId}/{newSoftwareOsId}/{userId}")
    public ResponseEntity<ResponseMessage> updateOsOfSoftwareItems(@PathVariable long oldSoftwareOsId, @PathVariable long newSoftwareOsId, @PathVariable int userId) {
        if (this.softwareService.updateSoftwareItemsOsId(oldSoftwareOsId, newSoftwareOsId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Operating Systems (OS) for Software items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Operating Systems (OS) for Software items COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/delete/{softwareId}")
    public ResponseEntity<ResponseMessage> deleteSoftwareById(@PathVariable Long softwareId) {
        if (this.softwareService.deleteById(softwareId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Software item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Software item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWARE + "/restore/{softwareId}")
    public ResponseEntity<ResponseMessage> restoreDeletedSoftwareItem(@PathVariable Long softwareId) {
        try {
            this.softwareService.restoreDeletedSoftware(softwareId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(softwareId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.controller.software;

import at.adridi.generalmanagement.media.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.media.model.ResponseMessage;
import at.adridi.generalmanagement.media.model.software.SoftwareOs;
import at.adridi.generalmanagement.media.service.software.SoftwareOsService;
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
 * API: Software Os - Operating Systems for Software Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class SoftwareOsController {

    @Autowired
    private SoftwareOsService softwareOsService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWAREOS + "/all")
    public ResponseEntity<List<SoftwareOs>> getAllSoftwareOs() {
        List<SoftwareOs> softwareOsList = new ArrayList<>();
        try {
            softwareOsList = this.softwareOsService.getAllSoftwareOs();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(softwareOsList)) {
            return status(HttpStatus.OK).body(softwareOsList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<SoftwareOs>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWAREOS + "/get/byId/{id}")
    public ResponseEntity<SoftwareOs> getSoftwareOsById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.softwareOsService.getSoftwareOsById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new SoftwareOs());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWAREOS + "/get/byTitle/{title}")
    public ResponseEntity<SoftwareOs> getAllSoftwareOsByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.softwareOsService.getSoftwareOsByOsTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new SoftwareOs());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWAREOS + "/add")
    public ResponseEntity<SoftwareOs> addSoftwareOs(@RequestBody String newSoftwareOsString) {
        if (newSoftwareOsString != null || newSoftwareOsString.trim().equals("")) {
            SoftwareOs newSoftwareOs = new SoftwareOs();
            newSoftwareOs.setOsTitle(newSoftwareOsString);
            SoftwareOs createdSoftwareOs = this.softwareOsService.save(newSoftwareOs);
            if (createdSoftwareOs != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdSoftwareOs);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SoftwareOs());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SoftwareOs());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWAREOS + "/update")
    public ResponseEntity<SoftwareOs> updateSoftwareOs(@RequestBody String updatedSoftwareOsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        SoftwareOs updatedSoftwareOs;
        try {
            updatedSoftwareOs = objectMapper.readValue(updatedSoftwareOsString, SoftwareOs.class);
            SoftwareOs newUpdatedSoftwareOs = this.softwareOsService.save(updatedSoftwareOs);
            if (newUpdatedSoftwareOs != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedSoftwareOs);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SoftwareOs());
            }
        } catch (IOException ex) {
            Logger.getLogger(SoftwareOsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SoftwareOs());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SOFTWAREOS + "/delete/byId/{softwareosId}")
    public ResponseEntity<ResponseMessage> deleteSoftwareOsById(@PathVariable Long softwareosId) {
        if (this.softwareOsService.deleteById(softwareosId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your software os was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Software os was cannot be null!"));
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller.decision;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.model.decision.CriteriaOption;
import at.adridi.generalmanagement.common.service.criteriaOption.CriteriaOptionService;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
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
 * API: CriteriaOption - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class CriteriaOptionController {

    @Autowired
    private CriteriaOptionService criteriaOptionService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/all/{userId}")
    public ResponseEntity<List<CriteriaOption>> getAllCriteriaOption(@PathVariable int userId) {
        List<CriteriaOption> organizationList = new ArrayList<>();
        try {
            organizationList = this.criteriaOptionService.getAllCriteriaOption(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CriteriaOption>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/ofDecision/{decisionId}/{userId}")
    public ResponseEntity<List<CriteriaOption>> getAllCriteriaOptionOfDecision(@PathVariable int decisionId, @PathVariable int userId) {
        List<CriteriaOption> organizationList = new ArrayList<>();
        try {
            organizationList = this.criteriaOptionService.getAllCriteriaOptionOfDecision(decisionId, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CriteriaOption>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/get/byId/{id}")
    public ResponseEntity<CriteriaOption> getCriteriaOptionById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.criteriaOptionService.getCriteriaOptionById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new CriteriaOption());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/search/byCriteriaTitle/{userId}")
    public ResponseEntity<List<CriteriaOption>> searchCriteriaOptionByCriteriaTitle(@RequestBody String criteriaTitlePattern, @PathVariable int userId) {
        List<CriteriaOption> criteriaOptionList = new ArrayList<>();
        try {
            criteriaOptionList = this.criteriaOptionService.searchCriteriaOptionItemByTitle(criteriaTitlePattern, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(criteriaOptionList)) {
            return status(HttpStatus.OK).body(criteriaOptionList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CriteriaOption>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/add")
    public ResponseEntity<CriteriaOption> addCriteriaOption(@RequestBody String newCriteriaOptionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CriteriaOption newCriteriaOption;
        CriteriaOption savedCriteriaOption;
        try {
            newCriteriaOption = objectMapper.readValue(newCriteriaOptionJson, CriteriaOption.class);
            savedCriteriaOption = this.criteriaOptionService.save(newCriteriaOption);
            return ResponseEntity.status(HttpStatus.OK).body(savedCriteriaOption);
        } catch (IOException ex) {
            Logger.getLogger(CriteriaOptionController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CriteriaOption());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/update")
    public ResponseEntity<CriteriaOption> updateCriteriaOption(@RequestBody String updatedCriteriaOptionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CriteriaOption updatedCriteriaOption;
        CriteriaOption savedCriteriaOption;
        try {
            updatedCriteriaOption = objectMapper.readValue(updatedCriteriaOptionJson, CriteriaOption.class);
            savedCriteriaOption = this.criteriaOptionService.save(updatedCriteriaOption);
            return ResponseEntity.status(HttpStatus.OK).body(savedCriteriaOption);
        } catch (IOException ex) {
            Logger.getLogger(CriteriaOptionController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CriteriaOption());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRITERIAOPTION + "/delete/{criteriaoptionId}")
    public ResponseEntity<ResponseMessage> deleteCriteriaOptionById(@PathVariable Long criteriaoptionId) {
        if (this.criteriaOptionService.deleteById(criteriaoptionId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your CriteriaOption item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CriteriaOption item does not exists!"));
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller.decision;

import at.adridi.generalmanagement.common.controller.organization.OrganizationController;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.model.decision.Decision;
import at.adridi.generalmanagement.common.model.organization.Organization;
import at.adridi.generalmanagement.common.service.decision.DecisionService;
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
 * API: Decision - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class DecisionController {

    @Autowired
    private DecisionService decisionService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/all/{userId}")
    public ResponseEntity<List<Decision>> getAllDecision(@PathVariable int userId) {
        List<Decision> organizationList = new ArrayList<>();
        try {
            organizationList = this.decisionService.getAllDecision(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Decision>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/get/byId/{id}")
    public ResponseEntity<Decision> getDecisionById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.decisionService.getDecisionById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Decision());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/search/byTitle/{userId}")
    public ResponseEntity<List<Decision>> searchDecisionByTitle(@RequestBody String titlePattern, @PathVariable int userId) {
        List<Decision> decisionList = new ArrayList<>();
        try {
            decisionList = this.decisionService.searchDecisionItemByTitle(titlePattern, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(decisionList)) {
            return status(HttpStatus.OK).body(decisionList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Decision>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/add")
    public ResponseEntity<Decision> addDecision(@RequestBody String newDecisionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Decision newDecision;
        Decision savedDecision;
        try {
            newDecision = objectMapper.readValue(newDecisionJson, Decision.class);
            savedDecision = this.decisionService.save(newDecision);
            return ResponseEntity.status(HttpStatus.OK).body(savedDecision);
        } catch (IOException ex) {
            Logger.getLogger(DecisionController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Decision());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/update")
    public ResponseEntity<Decision> updateDecision(@RequestBody String updatedDecisionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Decision updatedDecision;
        Decision savedDecision;
        try {
            updatedDecision = objectMapper.readValue(updatedDecisionJson, Decision.class);
            savedDecision = this.decisionService.save(updatedDecision);
            return ResponseEntity.status(HttpStatus.OK).body(savedDecision);
        } catch (IOException ex) {
            Logger.getLogger(DecisionController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Decision());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateDecisionTableData(@RequestBody String updatedDecisionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Decision updatedDecision;
        try {
            updatedDecision = objectMapper.readValue(updatedDecisionJson, Decision.class);
            if (this.decisionService.updateDecisionTableData(updatedDecision.getTitle(), updatedDecision.getChosenOption(), updatedDecision.getChosenOptionId(), updatedDecision.getInformation(), updatedDecision.getDecisionId(), updatedDecision.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Decision updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Decision could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Decision could not be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/delete/{decisionId}")
    public ResponseEntity<ResponseMessage> deleteDecisionById(@PathVariable Long decisionId) {
        if (this.decisionService.deleteById(decisionId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Decision item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Decision item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISION + "/setChosenOption/{chosenOptionId}/{chosenOption}/{decisionId}/{userId}")
    public ResponseEntity<ResponseMessage> setChosenDecisionOption(@PathVariable long chosenOptionId, @PathVariable String chosenOption, @PathVariable Long decisionId, @PathVariable int userId) {
        try {
            this.decisionService.setChosenDecisionOption(chosenOptionId, chosenOption, decisionId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Chosen option updated."));
        } catch (Exception ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Chosen option could not be updated!"));
        }
    }

}

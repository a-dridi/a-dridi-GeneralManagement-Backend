/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller.decision;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.model.decision.DecisionOption;
import at.adridi.generalmanagement.common.service.decisionOption.DecisionOptionService;
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
 * API: DecisionOption - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class DecisionOptionController {

    @Autowired
    private DecisionOptionService decisionOptionService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/all/{userId}")
    public ResponseEntity<List<DecisionOption>> getAllDecisionOption(@PathVariable int userId) {
        List<DecisionOption> organizationList = new ArrayList<>();
        try {
            organizationList = this.decisionOptionService.getAllDecisionOption(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<DecisionOption>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/get/ByDecision/{decisionOptionId}/{userId}")
    public ResponseEntity<List<DecisionOption>> getAllDecisionOptionByDecisionId(@PathVariable int decisionOptionId, @PathVariable int userId) {
        List<DecisionOption> organizationList = new ArrayList<>();
        try {
            organizationList = this.decisionOptionService.getDecisionOptionListByDecision(decisionOptionId, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<DecisionOption>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/get/byId/{id}")
    public ResponseEntity<DecisionOption> getDecisionOptionById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.decisionOptionService.getDecisionOptionById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new DecisionOption());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/search/byTitle/{userId}")
    public ResponseEntity<List<DecisionOption>> searchDecisionOptionByTitle(@RequestBody String titlePattern, @PathVariable int userId) {
        List<DecisionOption> decisionOptionList = new ArrayList<>();
        try {
            decisionOptionList = this.decisionOptionService.searchDecisionOptionItemByTitle(titlePattern, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(decisionOptionList)) {
            return status(HttpStatus.OK).body(decisionOptionList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<DecisionOption>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/add")
    public ResponseEntity<DecisionOption> addDecisionOption(@RequestBody String newDecisionOptionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        DecisionOption newDecisionOption;
        DecisionOption savedDecisionOption;
        try {
            newDecisionOption = objectMapper.readValue(newDecisionOptionJson, DecisionOption.class);
            savedDecisionOption = this.decisionOptionService.save(newDecisionOption);
            return ResponseEntity.status(HttpStatus.OK).body(savedDecisionOption);
        } catch (IOException ex) {
            Logger.getLogger(DecisionOptionController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DecisionOption());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/update")
    public ResponseEntity<DecisionOption> updateDecisionOption(@RequestBody String updatedDecisionOptionJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        DecisionOption updatedDecisionOption;
        DecisionOption savedDecisionOption;
        try {
            updatedDecisionOption = objectMapper.readValue(updatedDecisionOptionJson, DecisionOption.class);
            savedDecisionOption = this.decisionOptionService.save(updatedDecisionOption);
            return ResponseEntity.status(HttpStatus.OK).body(savedDecisionOption);
        } catch (IOException ex) {
            Logger.getLogger(DecisionOptionController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DecisionOption());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DECISIONOPTION + "/delete/{decisionoptionId}")
    public ResponseEntity<ResponseMessage> deleteDecisionOptionById(@PathVariable Long decisionoptionId) {
        if (this.decisionOptionService.deleteById(decisionoptionId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your DecisionOption item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. DecisionOption item does not exists!"));
        }
    }

}

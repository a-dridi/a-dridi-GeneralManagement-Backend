/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller.decision;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.model.decision.CriteriaOptionPoint;
import at.adridi.generalmanagement.common.service.decision.CriteriaOptionPointService;
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
 * API: CriteriaOptionPoint - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class CriteriaOptionPointController {

    @Autowired
    private CriteriaOptionPointService optionPointService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/all/{userId}")
    public ResponseEntity<List<CriteriaOptionPoint>> getAllOptionPoint(@PathVariable int userId) {
        List<CriteriaOptionPoint> organizationList = new ArrayList<>();
        try {
            organizationList = this.optionPointService.getAllOptionPoint(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CriteriaOptionPoint>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/get/byCriteriaId/{criteriaId}/{userId}")
    public ResponseEntity<List<CriteriaOptionPoint>> getOptionPointListByCriteriaId(@PathVariable int criteriaId, @PathVariable int userId) {
        List<CriteriaOptionPoint> organizationList = new ArrayList<>();
        try {
            organizationList = this.optionPointService.getOptionPointListByCriteriaId(criteriaId, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CriteriaOptionPoint>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/get/byId/{id}")
    public ResponseEntity<CriteriaOptionPoint> getOptionPointById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.optionPointService.getOptionPointById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new CriteriaOptionPoint());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/get/pointsSum/ofDecisionOption/{decisionOptionId}/{userId}")
    public ResponseEntity<Integer> getPointsSumOfDecisionOption(@PathVariable Long decisionOptionId, @PathVariable int userId) {
        try {
            return status(HttpStatus.OK).body(this.optionPointService.getPointsSumOfDecisionOption(decisionOptionId, userId));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/add")
    public ResponseEntity<CriteriaOptionPoint> addOptionPoint(@RequestBody String newOptionPointJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CriteriaOptionPoint newOptionPoint;
        CriteriaOptionPoint savedOptionPoint;
        try {
            newOptionPoint = objectMapper.readValue(newOptionPointJson, CriteriaOptionPoint.class);
            savedOptionPoint = this.optionPointService.save(newOptionPoint);
            return ResponseEntity.status(HttpStatus.OK).body(savedOptionPoint);
        } catch (IOException ex) {
            Logger.getLogger(CriteriaOptionPointController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CriteriaOptionPoint());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/update")
    public ResponseEntity<CriteriaOptionPoint> updateOptionPoint(@RequestBody String updatedOptionPointJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CriteriaOptionPoint updatedOptionPoint;
        CriteriaOptionPoint savedOptionPoint;
        try {
            updatedOptionPoint = objectMapper.readValue(updatedOptionPointJson, CriteriaOptionPoint.class);
            savedOptionPoint = this.optionPointService.save(updatedOptionPoint);
            return ResponseEntity.status(HttpStatus.OK).body(savedOptionPoint);
        } catch (IOException ex) {
            Logger.getLogger(CriteriaOptionPointController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CriteriaOptionPoint());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_OPTIONPOINT + "/delete/{optionpointId}")
    public ResponseEntity<ResponseMessage> deleteOptionPointById(@PathVariable Long optionpointId) {
        if (this.optionPointService.deleteById(optionpointId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your OptionPoint item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. OptionPoint item does not exists!"));
        }
    }

}

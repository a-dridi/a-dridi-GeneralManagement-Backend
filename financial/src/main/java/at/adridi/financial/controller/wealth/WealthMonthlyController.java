/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.wealth;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.wealth.WealthMonthly;
import at.adridi.financial.service.wealth.WealthMonthlyService;
import at.adridi.financial.util.ApiEndpoints;
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
 * API: WealthMonthly - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class WealthMonthlyController {

    @Autowired
    private WealthMonthlyService wealthMonthlyService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHMONTHLY + "/all/{userId}")
    public ResponseEntity<List<WealthMonthly>> getAllWealthMonthly(@PathVariable int userId) {
        List<WealthMonthly> wealthMonthlyList = new ArrayList<>();
        try {
            wealthMonthlyList = this.wealthMonthlyService.getAllWealthMonthly(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(wealthMonthlyList)) {
            return status(HttpStatus.OK).body(wealthMonthlyList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<WealthMonthly>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHMONTHLY + "/get/byId/{id}")
    public ResponseEntity<WealthMonthly> getWealthMonthlyById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.wealthMonthlyService.getWealthMonthlyById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new WealthMonthly());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHMONTHLY + "/update")
    public ResponseEntity<WealthMonthly> updateWealthMonthly(@RequestBody String updatedWealthMonthlyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        WealthMonthly updatedWealthMonthly;
        WealthMonthly savedWealthMonthly;
        try {
            updatedWealthMonthly = objectMapper.readValue(updatedWealthMonthlyJson, WealthMonthly.class);
            savedWealthMonthly = this.wealthMonthlyService.save(updatedWealthMonthly);
            return ResponseEntity.status(HttpStatus.OK).body(savedWealthMonthly);
        } catch (IOException ex) {
            Logger.getLogger(WealthMonthlyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new WealthMonthly());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHMONTHLY + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateWealthMonthlyTableData(@RequestBody String updatedWealthMonthlyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        WealthMonthly updatedWealthMonthly;
        try {
            updatedWealthMonthly = objectMapper.readValue(updatedWealthMonthlyJson, WealthMonthly.class);
            if (this.wealthMonthlyService.updateWealthMonthlyTableData(updatedWealthMonthly.getMonthDate(), updatedWealthMonthly.getYearDate(), updatedWealthMonthly.getExpenseCent(), updatedWealthMonthly.getEarningCent(), updatedWealthMonthly.getDifferenceCent(), updatedWealthMonthly.getImprovementPct(), updatedWealthMonthly.getNotice(), updatedWealthMonthly.getWealthmonthlyId(), updatedWealthMonthly.getUserId()) != -1) {
                //Adjust the value for improvementPct of the latest monthly wealth item (of the current year and month). 
                this.wealthMonthlyService.updateImprovementPct(updatedWealthMonthly.getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Wealth Monthly updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Wealth Monthly could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(WealthMonthlyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Wealth Monthly could not be updated!"));

        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHMONTHLY + "/delete/{wealthMonthlyId}")
    public ResponseEntity<ResponseMessage> deleteWealthMonthlyById(@PathVariable Long wealthMonthlyId) {
        if (this.wealthMonthlyService.deleteById(wealthMonthlyId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Wealth Monthly item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Wealth Monthly item does not exists!"));
        }
    }

}

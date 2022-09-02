/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.wealth;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.wealth.WealthYearly;
import at.adridi.financial.service.wealth.WealthYearlyService;
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
 * API: WealthYearly - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class WealthYearlyController {

    @Autowired
    private WealthYearlyService wealthYearlyService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHYEARLY + "/all/{userId}")
    public ResponseEntity<List<WealthYearly>> getAllWealthYearly(@PathVariable int userId) {
        List<WealthYearly> wealthYearlyList = new ArrayList<>();
        try {
            wealthYearlyList = this.wealthYearlyService.getAllWealthYearly(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(wealthYearlyList)) {
            return status(HttpStatus.OK).body(wealthYearlyList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<WealthYearly>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHYEARLY + "/get/byId/{id}")
    public ResponseEntity<WealthYearly> getWealthYearlyById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.wealthYearlyService.getWealthYearlyById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new WealthYearly());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHYEARLY + "/update")
    public ResponseEntity<WealthYearly> updateWealthYearly(@RequestBody String updatedWealthYearlyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        WealthYearly updatedWealthYearly;
        WealthYearly savedWealthYearly;
        try {
            updatedWealthYearly = objectMapper.readValue(updatedWealthYearlyJson, WealthYearly.class);
            savedWealthYearly = this.wealthYearlyService.save(updatedWealthYearly);
            return ResponseEntity.status(HttpStatus.OK).body(savedWealthYearly);
        } catch (IOException ex) {
            Logger.getLogger(WealthYearlyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new WealthYearly());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHYEARLY + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateWealthYearlyTableData(@RequestBody String updatedWealthYearlyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        WealthYearly updatedWealthYearly;
        try {
            updatedWealthYearly = objectMapper.readValue(updatedWealthYearlyJson, WealthYearly.class);

            if (updatedWealthYearly.getDifferenceCent() == 0) {
                updatedWealthYearly.setDifferenceCent(updatedWealthYearly.getExpenseCent() - updatedWealthYearly.getEarningCent());
            }
            //updatedWealthYearly.setDifferenceCent(updatedWealthYearly.getExpenseCent() - updatedWealthYearly.getEarningCent());

            int previousYear = updatedWealthYearly.getYearDate() - 1;
            double improvementPct = this.wealthYearlyService.getImprovementPct(previousYear, updatedWealthYearly);

            if (this.wealthYearlyService.updateWealthYearlyTableData(updatedWealthYearly.getYearDate(), updatedWealthYearly.getExpenseCent(), updatedWealthYearly.getEarningCent(), updatedWealthYearly.getDifferenceCent(), improvementPct, updatedWealthYearly.getNotice(), updatedWealthYearly.getWealthyearlyId(), updatedWealthYearly.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Wealth Yearly updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Wealth Yearly could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(WealthYearlyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Wealth Yearly could not be updated!"));

        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_WEALTHYEARLY + "/delete/{wealthYearlyId}")
    public ResponseEntity<ResponseMessage> deleteWealthYearlyById(@PathVariable Long wealthYearlyId) {
        if (this.wealthYearlyService.deleteById(wealthYearlyId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Wealth Yearly item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Wealth Yearly item does not exists!"));
        }
    }

}

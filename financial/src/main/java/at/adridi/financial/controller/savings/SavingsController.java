/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.savings;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.savings.Savings;
import at.adridi.financial.service.savings.SavingsService;
import at.adridi.financial.util.ApiEndpoints;
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
 * API: Savings - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class SavingsController {

    @Autowired
    private SavingsService savingsService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/all/{userId}")
    public ResponseEntity<List<Savings>> getAllSavings(@PathVariable int userId) {
        List<Savings> savingsList = new ArrayList<>();
        try {
            savingsList = this.savingsService.getAllSavings(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(savingsList)) {
            return status(HttpStatus.OK).body(savingsList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Savings>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/get/byId/{id}")
    public ResponseEntity<Savings> getSavingsById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.savingsService.getSavingsById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Savings());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/get/byFrequency/{userId}")
    public ResponseEntity<List<Savings>> getAllSavingsByFrequency(@PathVariable int frequency, @PathVariable int userId) {
        try {
            List<Savings> savingsList = new ArrayList<>();
            try {
                savingsList = this.savingsService.getSavingssByFrequencyAndUserId(frequency, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(savingsList)) {
                return status(HttpStatus.OK).body(savingsList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Savings>());
            }
        } catch (Exception ex) {
            Logger.getLogger(SavingsController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Savings>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/add")
    public ResponseEntity<Savings> addSavings(@RequestBody String newSavingsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Savings newSavings;
        Savings savedSavings;
        try {
            newSavings = objectMapper.readValue(newSavingsJson, Savings.class);
            savedSavings = this.savingsService.save(newSavings);
            this.savingsService.setupSavingsAccount(savedSavings);
            return ResponseEntity.status(HttpStatus.OK).body(savedSavings);
        } catch (IOException ex) {
            Logger.getLogger(SavingsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Savings());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/update")
    public ResponseEntity<Savings> updateSavings(@RequestBody String updatedSavingsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Savings updatedSavings;
        Savings savedSavings;
        try {
            updatedSavings = objectMapper.readValue(updatedSavingsJson, Savings.class);
            savedSavings = this.savingsService.save(updatedSavings);
            return ResponseEntity.status(HttpStatus.OK).body(savedSavings);
        } catch (IOException ex) {
            Logger.getLogger(SavingsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Savings());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateSavingsTableData(@RequestBody String updatedSavingsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Savings updatedSavings;
        try {
            updatedSavings = objectMapper.readValue(updatedSavingsJson, Savings.class);
            if (this.savingsService.updateSavingsTableData(updatedSavings.getDescription(), updatedSavings.getTargetCent(), updatedSavings.getStepAmountCent(), updatedSavings.getSavingsFrequency(), updatedSavings.getSavedTillNowCent(), updatedSavings.getLastSavingsUpdateDate(), updatedSavings.getNotice(), updatedSavings.getSavingsId(), updatedSavings.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Savings updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Savings could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(SavingsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Savings could not be updated!"));

        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/delete/{savingsId}")
    public ResponseEntity<ResponseMessage> deleteSavingsById(@PathVariable Long savingsId) {
        if (this.savingsService.deleteById(savingsId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Savings item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Savings item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_SAVINGS + "/restore/{savingsId}")
    public ResponseEntity<ResponseMessage> restoreDeletedSavingsItem(@PathVariable Long savingsId) {
        try {
            this.savingsService.restoreDeletedSavings(savingsId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(savingsId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

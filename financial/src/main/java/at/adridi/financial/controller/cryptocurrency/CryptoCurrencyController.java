/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.cryptocurrency;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.cryptocurrency.CryptoCurrency;
import at.adridi.financial.service.cryptocurrency.CryptoCurrencyService;
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
 * API: CryptoCurrency - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class CryptoCurrencyController {

    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/all/{userId}")
    public ResponseEntity<List<CryptoCurrency>> getAllCryptoCurrency(@PathVariable int userId) {
        List<CryptoCurrency> cryptoCurrencyList = new ArrayList<>();
        try {
            cryptoCurrencyList = this.cryptoCurrencyService.getAllCryptoCurrency(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(cryptoCurrencyList)) {
            return status(HttpStatus.OK).body(cryptoCurrencyList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CryptoCurrency>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/get/byId/{id}")
    public ResponseEntity<CryptoCurrency> getCryptoCurrencyById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.cryptoCurrencyService.getCryptoCurrencyById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new CryptoCurrency());
        }
    }


    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/add")
    public ResponseEntity<CryptoCurrency> addCryptoCurrency(@RequestBody String newCryptoCurrencyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        CryptoCurrency newCryptoCurrency;
        CryptoCurrency savedCryptoCurrency;
        try {
            newCryptoCurrency = objectMapper.readValue(newCryptoCurrencyJson, CryptoCurrency.class);
            savedCryptoCurrency = this.cryptoCurrencyService.save(newCryptoCurrency);
            return ResponseEntity.status(HttpStatus.OK).body(savedCryptoCurrency);
        } catch (IOException ex) {
            Logger.getLogger(CryptoCurrencyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CryptoCurrency());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/update")
    public ResponseEntity<CryptoCurrency> updateCryptoCurrency(@RequestBody String updatedCryptoCurrencyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CryptoCurrency updatedCryptoCurrency;
        CryptoCurrency savedCryptoCurrency;
        try {
            updatedCryptoCurrency = objectMapper.readValue(updatedCryptoCurrencyJson, CryptoCurrency.class);
            savedCryptoCurrency = this.cryptoCurrencyService.save(updatedCryptoCurrency);
            return ResponseEntity.status(HttpStatus.OK).body(savedCryptoCurrency);
        } catch (IOException ex) {
            Logger.getLogger(CryptoCurrencyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CryptoCurrency());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateCryptoCurrencyTableData(@RequestBody String updatedCryptoCurrencyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CryptoCurrency updatedCryptoCurrency;

        try {
            updatedCryptoCurrency = objectMapper.readValue(updatedCryptoCurrencyJson, CryptoCurrency.class);

            if (this.cryptoCurrencyService.updateCryptoCurrencyTableData(updatedCryptoCurrency.getAmount(), updatedCryptoCurrency.getCurrency(), updatedCryptoCurrency.getStorageLocation(), updatedCryptoCurrency.getNotice(), updatedCryptoCurrency.getCryptocurrencyId(), updatedCryptoCurrency.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. CryptoCurrency updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CryptoCurrency could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(CryptoCurrencyController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CryptoCurrency could not be updated!"));

        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/delete/{cryptoCurrencyId}")
    public ResponseEntity<ResponseMessage> deleteCryptoCurrency(@PathVariable Long cryptoCurrencyId) {
        if (this.cryptoCurrencyService.deleteById(cryptoCurrencyId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your CryptoCurrency was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CryptoCurrency does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCY + "/restore/{cryptoCurrencyId}")
    public ResponseEntity<ResponseMessage> restoreDeletedCryptoCurrency(@PathVariable int cryptoCurrencyId) {
        try {
            this.cryptoCurrencyService.restoreDeletedCryptoCurrency(cryptoCurrencyId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(cryptoCurrencyId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

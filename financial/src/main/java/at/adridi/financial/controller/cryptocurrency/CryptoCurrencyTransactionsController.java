/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.cryptocurrency;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.cryptocurrency.CryptoCurrencyTransactions;
import at.adridi.financial.service.cryptocurrency.CryptoCurrencyTransactionsService;
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
public class CryptoCurrencyTransactionsController {

    @Autowired
    private CryptoCurrencyTransactionsService cryptoCurrencyTransactionsService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/all/{userId}")
    public ResponseEntity<List<CryptoCurrencyTransactions>> getAllCryptoCurrencyTransactions(@PathVariable int userId) {
        List<CryptoCurrencyTransactions> cryptoCurrencyList = new ArrayList<>();
        try {
            cryptoCurrencyList = this.cryptoCurrencyTransactionsService.getAllCryptoCurrencyTransaction(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(cryptoCurrencyList)) {
            return status(HttpStatus.OK).body(cryptoCurrencyList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<CryptoCurrencyTransactions>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/get/byId/{id}")
    public ResponseEntity<CryptoCurrencyTransactions> getCryptoCurrencyTransactionsById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.cryptoCurrencyTransactionsService.getCryptoCurrencyTransactionsById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new CryptoCurrencyTransactions());
        }
    }


    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/add")
    public ResponseEntity<CryptoCurrencyTransactions> addCryptoCurrencyTransactions(@RequestBody String newCryptoCurrencyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        CryptoCurrencyTransactions newCryptoCurrencyTransactions;
        CryptoCurrencyTransactions savedCryptoCurrencyTransactions;
        try {
            newCryptoCurrencyTransactions = objectMapper.readValue(newCryptoCurrencyJson, CryptoCurrencyTransactions.class);
            savedCryptoCurrencyTransactions = this.cryptoCurrencyTransactionsService.save(newCryptoCurrencyTransactions);
            return ResponseEntity.status(HttpStatus.OK).body(savedCryptoCurrencyTransactions);
        } catch (IOException ex) {
            Logger.getLogger(CryptoCurrencyTransactionsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CryptoCurrencyTransactions());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/update")
    public ResponseEntity<CryptoCurrencyTransactions> updateCryptoCurrencyTransactions(@RequestBody String updatedCryptoCurrencyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CryptoCurrencyTransactions updatedCryptoCurrency;
        CryptoCurrencyTransactions savedCryptoCurrency;
        try {
            updatedCryptoCurrency = objectMapper.readValue(updatedCryptoCurrencyJson, CryptoCurrencyTransactions.class);
            savedCryptoCurrency = this.cryptoCurrencyTransactionsService.save(updatedCryptoCurrency);
            return ResponseEntity.status(HttpStatus.OK).body(savedCryptoCurrency);
        } catch (IOException ex) {
            Logger.getLogger(CryptoCurrencyTransactionsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CryptoCurrencyTransactions());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateCryptoCurrencyTransactionsTableData(@RequestBody String updatedCryptoCurrencyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CryptoCurrencyTransactions updatedCryptoCurrencyTransactions;

        try {
            updatedCryptoCurrencyTransactions = objectMapper.readValue(updatedCryptoCurrencyJson, CryptoCurrencyTransactions.class);

            if (this.cryptoCurrencyTransactionsService.updateCryptoCurrencyTransactionsTableData(updatedCryptoCurrencyTransactions.getSenderFrom(), updatedCryptoCurrencyTransactions.getCurrencyFrom(), updatedCryptoCurrencyTransactions.getDestinationTo(), updatedCryptoCurrencyTransactions.getCurrencyTo(), updatedCryptoCurrencyTransactions.getAmount(), updatedCryptoCurrencyTransactions.getStorageLocation(), updatedCryptoCurrencyTransactions.getNotice(), updatedCryptoCurrencyTransactions.getCryptocurrencytransactionId(), updatedCryptoCurrencyTransactions.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. CryptoCurrencyTransaction updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CryptoCurrencyTransaction could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(CryptoCurrencyTransactionsController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CryptoCurrencyTransaction could not be updated!"));

        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/delete/{cryptoCurrencyTransactionId}")
    public ResponseEntity<ResponseMessage> deleteCryptoCurrencyTransactions(@PathVariable Long cryptoCurrencyTransactionId) {
        if (this.cryptoCurrencyTransactionsService.deleteById(cryptoCurrencyTransactionId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your CryptoCurrencyTransaction was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. CryptoCurrencyTransaction does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_CRYPTOCURRENCYTRANSACTIONS + "/restore/{cryptoCurrencyTransactionId}")
    public ResponseEntity<ResponseMessage> restoreDeletedCryptoCurrencyTransactions(@PathVariable int cryptoCurrencyTransactionId) {
        try {
            this.cryptoCurrencyTransactionsService.restoreDeletedCryptoCurrencyTransaction(cryptoCurrencyTransactionId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(cryptoCurrencyTransactionId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

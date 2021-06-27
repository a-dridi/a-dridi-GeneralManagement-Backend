/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.reserves;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.reserves.ReservesCategory;
import at.adridi.financial.service.reserves.ReservesCategoryService;
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
 * API: ReservesCategory - Categories for Reserves
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ReservesCategoryController {

    @Autowired
    private ReservesCategoryService reservesCategoryService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/all")
    public ResponseEntity<List<ReservesCategory>> getAllReservesCategory() {
        List<ReservesCategory> reservesCategoryList = new ArrayList<>();
        try {
            reservesCategoryList = this.reservesCategoryService.getAllReservesCategory();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(reservesCategoryList)) {
            return status(HttpStatus.OK).body(reservesCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ReservesCategory>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/get/byId/{id}")
    public ResponseEntity<ReservesCategory> getReservesCategoryById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.reservesCategoryService.getReservesCategoryById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ReservesCategory());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/get/byTitle/{title}")
    public ResponseEntity<ReservesCategory> getAllReservesCategoryByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.reservesCategoryService.getReservesCategoryByTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ReservesCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/add")
    public ResponseEntity<ReservesCategory> addReservesCategory(@RequestBody String newReservesCategoryString) {
        if (newReservesCategoryString != null || newReservesCategoryString.trim().equals("")) {
            ReservesCategory newReservesCategory = new ReservesCategory();
            newReservesCategory.setCategoryTitle(newReservesCategoryString);
            ReservesCategory createdReservesCategory = this.reservesCategoryService.save(newReservesCategory);
            if (createdReservesCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdReservesCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReservesCategory());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReservesCategory());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/update")
    public ResponseEntity<ReservesCategory> updateReservesCategory(@RequestBody String updatedReservesCategoryString) {
        ObjectMapper objectMapper = new ObjectMapper();
        ReservesCategory updatedReservesCategory;
        try {
            updatedReservesCategory = objectMapper.readValue(updatedReservesCategoryString, ReservesCategory.class);
            ReservesCategory newUpdatedReservesCategory = this.reservesCategoryService.save(updatedReservesCategory);
            if (newUpdatedReservesCategory != null || updatedReservesCategoryString.trim().equals("")) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedReservesCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReservesCategory());
            }
        } catch (IOException ex) {
            Logger.getLogger(ReservesTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReservesCategory());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/delete/byId/{reservesCategoryId}")
    public ResponseEntity<ResponseMessage> deleteReservesCategoryById(@PathVariable Long reservesCategoryId) {
        if (this.reservesCategoryService.deleteById(reservesCategoryId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Reserves category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Reserves Category cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVESCATEGORY + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteReservesCategoryByTitle(@PathVariable String title) {
        if (this.reservesCategoryService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your Reserves category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Reserves category does not exists!"));
        }
    }

}

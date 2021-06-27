/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.controller.reserves;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.ResponseMessage;
import at.adridi.financial.model.reserves.Reserves;
import at.adridi.financial.model.reserves.ReservesCategory;
import at.adridi.financial.service.reserves.ReservesService;
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
 * API: Reserves - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ReservesTableController {

    @Autowired
    private ReservesService reservesService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/all/{userId}")
    public ResponseEntity<List<Reserves>> getAllReserves(@PathVariable int userId) {
        List<Reserves> reservesList = new ArrayList<>();
        try {
            reservesList = this.reservesService.getAllReserves(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(reservesList)) {
            return status(HttpStatus.OK).body(reservesList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Reserves>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/get/byId/{id}")
    public ResponseEntity<Reserves> getReservesById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.reservesService.getReservesById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Reserves());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/get/byDescription/{description}/{userId}")
    public ResponseEntity<List<Reserves>> getAllReservesByDescription(@PathVariable String description, @PathVariable int userId) {
        List<Reserves> reservesList = new ArrayList<>();
        try {
            reservesList = this.reservesService.getReservesByDescription(description, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(reservesList)) {
            return status(HttpStatus.OK).body(reservesList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Reserves>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/get/byCategory/{userId}")
    public ResponseEntity<List<Reserves>> getAllReservesByReservesCategory(@RequestBody String reservesCategoryJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ReservesCategory reservesCategory;
        try {
            reservesCategory = objectMapper.readValue(reservesCategoryJson, ReservesCategory.class);
            List<Reserves> reservesList = new ArrayList<>();
            try {
                reservesList = this.reservesService.getReservesByCategory(reservesCategory, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(reservesList)) {
                return status(HttpStatus.OK).body(reservesList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Reserves>());
            }
        } catch (IOException ex) {
            Logger.getLogger(ReservesTableController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Reserves>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/add")
    public ResponseEntity<Reserves> addReserves(@RequestBody String newReservesJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Reserves newReserves;
        Reserves savedReserves;
        try {
            newReserves = objectMapper.readValue(newReservesJson, Reserves.class);
            savedReserves = this.reservesService.save(newReserves);
            return ResponseEntity.status(HttpStatus.OK).body(savedReserves);
        } catch (IOException ex) {
            Logger.getLogger(ReservesTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Reserves());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/update")
    public ResponseEntity<Reserves> updateReserves(@RequestBody String updatedReservesJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Reserves updatedReserves;
        Reserves savedReserves;
        try {
            updatedReserves = objectMapper.readValue(updatedReservesJson, Reserves.class);
            savedReserves = this.reservesService.save(updatedReserves);
            return ResponseEntity.status(HttpStatus.OK).body(savedReserves);
        } catch (IOException ex) {
            Logger.getLogger(ReservesTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Reserves());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateReservesTableData(@RequestBody String updatedReservesJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Reserves updatedReserves;

        try {
            updatedReserves = objectMapper.readValue(updatedReservesJson, Reserves.class);

            if (this.reservesService.updateReservesTableData(updatedReserves.getCategory().getReservesCategoryId(), updatedReserves.getDescription(), updatedReserves.getAmount(), updatedReserves.getCurrency(), updatedReserves.getStorageLocation(), updatedReserves.getNotice(), updatedReserves.getReservesId(), updatedReserves.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Reserves updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Reserves could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(ReservesTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Reserves could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/updateReservesCategories/{oldReservesCategoryId}/{newReservesCategoryId}/{userId}")
    public ResponseEntity<ResponseMessage> updateReservesCategories(@PathVariable long oldReservesCategoryId, @PathVariable long newReservesCategoryId, @PathVariable int userId) {
        if (this.reservesService.updateReservesCategoryId(oldReservesCategoryId, newReservesCategoryId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Categories for Reserves were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Categories for Reserves COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/delete/{reservesId}")
    public ResponseEntity<ResponseMessage> deleteReserves(@PathVariable Long reservesId) {
        if (this.reservesService.deleteById(reservesId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Reserves was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Reserves does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_RESERVES + "/restore/{reservesId}")
    public ResponseEntity<ResponseMessage> restoreDeletedReserves(@PathVariable int reservesId) {
        try {
            this.reservesService.restoreDeletedReserves(reservesId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(reservesId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

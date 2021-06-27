/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.earning;

import at.adridi.generalmanagement.budgeting.controller.expense.*;
import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.earning.EarningTimerange;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import at.adridi.generalmanagement.budgeting.service.earning.EarningTimerangeService;
import at.adridi.generalmanagement.budgeting.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
 * API: EarningTimerange - Time Ranges for Earning
 *
 * @author A.Dridi
 */
@RestController
public class EarningTimerangeController {

    @Autowired
    private EarningTimerangeService earningTimerangeService;

    /**
     * Add fixed timeranges to database.
     */
    @PostConstruct
    public void prepareTimeranges() {
        this.saveEarningTimerange(1, "onetime");
        this.saveEarningTimerange(2, "daily");
        this.saveEarningTimerange(3, "weekly");
        this.saveEarningTimerange(4, "biweekly");
        this.saveEarningTimerange(5, "monthly");
        this.saveEarningTimerange(6, "every 2 months");
        this.saveEarningTimerange(7, "every quartal");
        this.saveEarningTimerange(8, "every 6 months");
        this.saveEarningTimerange(9, "yearly");
        this.saveEarningTimerange(10, "every 2 years");
        this.saveEarningTimerange(11, "every 5 years");
        this.saveEarningTimerange(12, "custom");
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/all")
    public ResponseEntity<List<EarningTimerange>> getAllEarningTimerange() {
        List<EarningTimerange> earningTimerangeList = new ArrayList<>();
        try {
            earningTimerangeList = this.earningTimerangeService.getAllEarningTimerange();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningTimerangeList)) {
            return status(HttpStatus.OK).body(earningTimerangeList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningTimerange>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/get/byId/{id}")
    public ResponseEntity<EarningTimerange> getEarningTimerangeById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.earningTimerangeService.getEarningTimerangeById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new EarningTimerange());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/get/byTitle/{title}")
    public ResponseEntity<EarningTimerange> getAllEarningTimerangeByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.earningTimerangeService.getEarningTimerangeByTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new EarningTimerange());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/add")
    public ResponseEntity<ResponseMessage> addEarningTimerange(@RequestBody String newEarningTimerangeTitle) {
        EarningTimerange newEarningTimerange = new EarningTimerange();
        int resultCode;
        if (newEarningTimerangeTitle != null && !newEarningTimerangeTitle.trim().equals("")) {
            newEarningTimerange.setTimerangeTitle(newEarningTimerangeTitle);
            resultCode = this.earningTimerangeService.save(newEarningTimerange);
        } else {
            resultCode = 4;
        }

        switch (resultCode) {
            case 0:
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Earning Timerange was added successfully."));
            case 1:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning Timerange cannot be null!"));
            case 2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning Timerange cannot be null!"));
            case 4:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. The JSON object string could not be processed!"));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning Timerange could not be saved!"));
        }
    }

    public boolean saveEarningTimerange(long earningTimerangeId, String newEarningTimerangeTitle) {
        EarningTimerange newEarningTimerange = new EarningTimerange();
        try {
            if (newEarningTimerangeTitle != null && !newEarningTimerangeTitle.trim().equals("")) {
                newEarningTimerange.setTimerangeId(earningTimerangeId);
                newEarningTimerange.setTimerangeTitle(newEarningTimerangeTitle);
                this.earningTimerangeService.save(newEarningTimerange);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/update")
    public ResponseEntity<ResponseMessage> updateEarningTimerange(@RequestBody String updatedEarningTimerangeJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        EarningTimerange updatedEarningTimerange;
        try {
            updatedEarningTimerange = objectMapper.readValue(updatedEarningTimerangeJson, EarningTimerange.class);
            int resultCode = this.earningTimerangeService.save(updatedEarningTimerange);
            switch (resultCode) {
                case 0:
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Update was successful."));
                case 1:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning Timerange cannot be null!"));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Update failed!"));

            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Please check the passed JSON object!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/delete/byId/{earningTimerangeId}")
    public ResponseEntity<ResponseMessage> deleteEarningTimerangeById(@PathVariable Long earningTimerangeId) {
        if (this.earningTimerangeService.deleteById(earningTimerangeId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your earning timerange was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning timerange does not exists!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGTIMERANGE + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteEarningTimerangeByTitle(@PathVariable String title) {
        if (this.earningTimerangeService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your earning timerange was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning timerange does not exists!"));
        }
    }
}

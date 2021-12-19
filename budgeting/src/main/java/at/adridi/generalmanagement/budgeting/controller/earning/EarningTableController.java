/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.earning.Earning;
import at.adridi.generalmanagement.budgeting.model.earning.EarningCategory;
import at.adridi.generalmanagement.budgeting.model.earning.EarningTimerange;
import at.adridi.generalmanagement.budgeting.service.earning.EarningDevelopmentService;
import at.adridi.generalmanagement.budgeting.service.earning.EarningService;
import at.adridi.generalmanagement.budgeting.util.ApiEndpoints;
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
 * API: Earning - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class EarningTableController {

    @Autowired
    private EarningService earningService;
    @Autowired
    private EarningDevelopmentService earningDevelopmentService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/all/{userId}")
    public ResponseEntity<List<Earning>> getAllEarning(@PathVariable int userId) {
        List<Earning> earningList = new ArrayList<>();
        try {
            earningList = this.earningService.getAllEarning(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningList)) {
            return status(HttpStatus.OK).body(earningList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/certainYear/{year}/{userId}")
    public ResponseEntity<List<Earning>> getEarningsOfCertainYear(@PathVariable int year, @PathVariable int userId) {
        List<Earning> earningList = new ArrayList<>();
        try {
            earningList = this.earningService.getEarningsOfCertainYear(year, userId);
            this.earningDevelopmentService.checkAndUpdate(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningList)) {
            return status(HttpStatus.OK).body(earningList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/certainMonthYear/{month}/{year}/{userId}")
    public ResponseEntity<List<Earning>> getEarningsOfCertainMonthYear(@PathVariable int month, @PathVariable int year, @PathVariable int userId) {
        List<Earning> earningList = new ArrayList<>();
        try {
            earningList = this.earningService.getEarningsByMonthYear(month, year, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningList)) {
            return status(HttpStatus.OK).body(earningList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/byId/{id}")
    public ResponseEntity<Earning> getEarningById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.earningService.getEarningById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Earning());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/byTitle/{title}/{userId}")
    public ResponseEntity<List<Earning>> getAllEarningByTitle(@PathVariable String title, @PathVariable int userId) {
        List<Earning> earningList = new ArrayList<>();
        try {
            earningList = this.earningService.getEarningByTitle(title, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningList)) {
            return status(HttpStatus.OK).body(earningList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/byCategory/{userId}")
    public ResponseEntity<List<Earning>> getAllEarningByEarningCategory(@RequestBody String earningCategoryJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        EarningCategory earningCategory;
        try {
            earningCategory = objectMapper.readValue(earningCategoryJson, EarningCategory.class);
            List<Earning> earningList = new ArrayList<>();
            try {
                earningList = this.earningService.getEarningsByCategoryAndUserId(earningCategory, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(earningList)) {
                return status(HttpStatus.OK).body(earningList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
            }
        } catch (IOException ex) {
            Logger.getLogger(EarningTableController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/byTimerange/{userId}")
    public ResponseEntity<List<Earning>> getAllEarningByEarningTimerange(@RequestBody String earningTimerangeJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        EarningTimerange earningTimerange;
        try {
            earningTimerange = objectMapper.readValue(earningTimerangeJson, EarningTimerange.class);
            List<Earning> earningList = new ArrayList<>();
            try {
                earningList = this.earningService.getEarningsByEarningTimerangeAndUserId(earningTimerange, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(earningList)) {
                return status(HttpStatus.OK).body(earningList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
            }
        } catch (IOException ex) {
            Logger.getLogger(EarningTableController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Earning>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/add")
    public ResponseEntity<Earning> addEarning(@RequestBody String newEarningJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Earning newEarning;
        Earning savedEarning;
        try {
            newEarning = objectMapper.readValue(newEarningJson, Earning.class);
            savedEarning = this.earningService.save(newEarning);
            /*
            if (!this.earningDevelopmentService.checkAndUpdate(savedEarning.getUserId())) {
                this.earningDevelopmentService.addEarningDevelopmentOfCurrentMonthYear(savedEarning.getCentValue(), savedEarning.getEarningTimerange().getTimerangeId(), savedEarning.getUserId());
            }
             */
            this.earningDevelopmentService.addEarningDevelopmentOfCurrentMonthYear(savedEarning.getCentValue(), savedEarning.getEarningTimerange().getTimerangeId(), savedEarning.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(savedEarning);
        } catch (IOException ex) {
            Logger.getLogger(EarningTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Earning());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/update")
    public ResponseEntity<Earning> updateEarning(@RequestBody String updatedEarningJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Earning oldEarning;
        Earning updatedEarning;
        Earning savedEarning;
        try {
            updatedEarning = objectMapper.readValue(updatedEarningJson, Earning.class);
            oldEarning = this.earningService.getEarningById(updatedEarning.getEarningId());
            savedEarning = this.earningService.save(updatedEarning);
            //this.earningDevelopmentService.checkAndUpdate(oldEarning.getUserId());
            this.earningDevelopmentService.updateEarningDevelopmentOfCurrentMonthYear(oldEarning.getCentValue(), savedEarning.getCentValue(), savedEarning.getEarningTimerange().getTimerangeId(), savedEarning.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(savedEarning);
        } catch (IOException ex) {
            Logger.getLogger(EarningTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Earning());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateEarningTableData(@RequestBody String updatedEarningJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Earning updatedEarning;
        try {
            updatedEarning = objectMapper.readValue(updatedEarningJson, Earning.class);
            Earning oldEarning = this.earningService.getEarningById(updatedEarning.getEarningId());
            this.earningDevelopmentService.checkAndUpdate(updatedEarning.getUserId());

            if (this.earningService.updateEarningTableData(updatedEarning.getTitle(), updatedEarning.getEarningCategory().getEarningCategoryId(), updatedEarning.getCentValue(), updatedEarning.getEarningTimerange().getTimerangeId(), updatedEarning.getEarningDate(), updatedEarning.getInformation(), updatedEarning.getEarningId(), updatedEarning.getUserId()) != -1) {

                if (oldEarning.getEarningTimerange().getTimerangeId().equals(updatedEarning.getEarningTimerange().getTimerangeId())) {
                    this.earningDevelopmentService.updateEarningDevelopmentOfCurrentMonthYear(oldEarning.getCentValue(), updatedEarning.getCentValue(), updatedEarning.getEarningTimerange().getTimerangeId(), oldEarning.getUserId());
                } else {
                    //Timerange was changed - Change calculation method in earning development
                    this.earningDevelopmentService.deleteEarningDevelopmentOfCurrentMonthYear(oldEarning.getCentValue(), oldEarning.getEarningTimerange().getTimerangeId(), oldEarning.getUserId());
                    this.earningDevelopmentService.addEarningDevelopmentOfCurrentMonthYear(updatedEarning.getCentValue(), updatedEarning.getEarningTimerange().getTimerangeId(), updatedEarning.getUserId());
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Earning updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(EarningTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning could not be updated!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/updateEarningCategories/{oldEarningCategoryId}/{newEarningCategoryId}/{userId}")
    public ResponseEntity<ResponseMessage> updateEarningCategories(@PathVariable long oldEarningCategoryId, @PathVariable long newEarningCategoryId, @PathVariable int userId) {
        if (this.earningService.updateEarningsEarningCategoryId(oldEarningCategoryId, newEarningCategoryId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Categories for earnings were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Categories for earnings COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/delete/{earningId}")
    public ResponseEntity<ResponseMessage> deleteEarning(@PathVariable Long earningId) {
        Earning deletedEarning = this.earningService.getEarningById(earningId);
        this.earningDevelopmentService.checkAndUpdate(deletedEarning.getUserId());

        if (this.earningService.deleteById(earningId)) {
            this.earningDevelopmentService.deleteEarningDevelopmentOfCurrentMonthYear(deletedEarning.getCentValue(), deletedEarning.getEarningTimerange().getTimerangeId(), deletedEarning.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your earning was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/sum/monthly/{userId}")
    public ResponseEntity<Integer> getMonthlyEarningsSum(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.earningService.getMonthlyRecurringEarningsSum(userId));
        } catch (DataValueNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/sum/yearly/{userId}")
    public ResponseEntity<Integer> getYearlyEarningsSum(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.earningService.getYearlyRecurringEarningsSum(userId));
        } catch (DataValueNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/sum/single/custom/certainMonth/{month}/{userId}")
    public ResponseEntity<Integer> getOfCertainMonthSingleAndCustomEarningsSum(@PathVariable int month, @PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.earningService.getSingleAndCustomEarningsByCertainMonth(month, userId));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/get/sum/currentMonth/{userId}")
    public ResponseEntity<Integer> getCurrentMonthEarningsSum(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.earningService.getCurrentMonthEarnings(userId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNING + "/restore/{expenseId}")
    public ResponseEntity<ResponseMessage> restoreDeletedEarning(@PathVariable int expenseId) {
        try {
            this.earningService.restoreDeletedEarning(expenseId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(expenseId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseTimerangeService;
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
 * API: ExpenseTimerange - Time Ranges for Expense
 *
 * @author A.Dridi
 */
@RestController
public class ExpenseTimerangeController {

    @Autowired
    private ExpenseTimerangeService expenseTimerangeService;

    /**
     * Add fixed timeranges to database.
     */
    @PostConstruct
    public void prepareTimeranges() {
        this.saveExpenseTimerange(1, "onetime");
        this.saveExpenseTimerange(2, "daily");
        this.saveExpenseTimerange(3, "weekly");
        this.saveExpenseTimerange(4, "biweekly");
        this.saveExpenseTimerange(5, "monthly");
        this.saveExpenseTimerange(6, "every 2 months");
        this.saveExpenseTimerange(7, "every quartal");
        this.saveExpenseTimerange(8, "every 6 months");
        this.saveExpenseTimerange(9, "yearly");
        this.saveExpenseTimerange(10, "every 2 years");
        this.saveExpenseTimerange(11, "every 5 years");
        this.saveExpenseTimerange(12, "custom");
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/all")
    public ResponseEntity<List<ExpenseTimerange>> getAllExpenseTimerange() {
        List<ExpenseTimerange> expenseTimerangeList = new ArrayList<>();
        try {
            expenseTimerangeList = this.expenseTimerangeService.getAllExpenseTimerange();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseTimerangeList)) {
            return status(HttpStatus.OK).body(expenseTimerangeList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseTimerange>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/get/byId/{id}")
    public ResponseEntity<ExpenseTimerange> getExpenseTimerangeById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.expenseTimerangeService.getExpenseTimerangeById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ExpenseTimerange());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/get/byTitle/{title}")
    public ResponseEntity<ExpenseTimerange> getAllExpenseTimerangeByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.expenseTimerangeService.getExpenseTimerangeByTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ExpenseTimerange());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/add")
    public ResponseEntity<ResponseMessage> addExpenseTimerange(@RequestBody String newExpenseTimerangeTitle) {
        ExpenseTimerange newExpenseTimerange = new ExpenseTimerange();
        int resultCode;
        if (newExpenseTimerangeTitle != null && !newExpenseTimerangeTitle.trim().equals("")) {
            newExpenseTimerange.setTimerangeTitle(newExpenseTimerangeTitle);
            resultCode = this.expenseTimerangeService.save(newExpenseTimerange);
        } else {
            resultCode = 4;
        }

        switch (resultCode) {
            case 0:
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Expense Timerange was added successfully."));
            case 1:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Timerange cannot be null!"));
            case 2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Timerange cannot be null!"));
            case 4:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. The JSON object string could not be processed!"));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Timerange could not be saved!"));
        }
    }

    public boolean saveExpenseTimerange(long expenseTimerangeId, String newExpenseTimerangeTitle) {
        ExpenseTimerange newExpenseTimerange = new ExpenseTimerange();
        try {
            if (newExpenseTimerangeTitle != null && !newExpenseTimerangeTitle.trim().equals("")) {
                newExpenseTimerange.setTimerangeId(expenseTimerangeId);
                newExpenseTimerange.setTimerangeTitle(newExpenseTimerangeTitle);
                this.expenseTimerangeService.save(newExpenseTimerange);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/update")
    public ResponseEntity<ResponseMessage> updateExpenseTimerange(@RequestBody String updatedExpenseTimerangeJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseTimerange updatedExpenseTimerange;
        try {
            updatedExpenseTimerange = objectMapper.readValue(updatedExpenseTimerangeJson, ExpenseTimerange.class);
            int resultCode = this.expenseTimerangeService.save(updatedExpenseTimerange);
            switch (resultCode) {
                case 0:
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Update was successful."));
                case 1:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Timerange cannot be null!"));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Update failed!"));

            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Please check the passed JSON object!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/delete/byId/{expenseTimerangeId}")
    public ResponseEntity<ResponseMessage> deleteExpenseTimerangeById(@PathVariable Long expenseTimerangeId) {
        if (this.expenseTimerangeService.deleteById(expenseTimerangeId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your expense timerange was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense timerange does not exists!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSETIMERANGE + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteExpenseTimerangeByTitle(@PathVariable String title) {
        if (this.expenseTimerangeService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your expense timerange was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense timerange does not exists!"));
        }
    }
}

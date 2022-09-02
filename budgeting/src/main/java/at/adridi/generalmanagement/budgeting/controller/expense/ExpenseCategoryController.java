/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseCategoryService;
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
 * API: ExpenseCategory - Categories for Expense
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/all/{userId}")
    public ResponseEntity<List<ExpenseCategory>> getAllExpenseCategory(@PathVariable int userId) {
        List<ExpenseCategory> expenseCategoryList = new ArrayList<>();
        try {
            expenseCategoryList = this.expenseCategoryService.getAllExpenseCategory(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseCategoryList)) {
            return status(HttpStatus.OK).body(expenseCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseCategory>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/get/byId/{id}")
    public ResponseEntity<ExpenseCategory> getExpenseCategoryById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.expenseCategoryService.getExpenseCategoryById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/get/byTitle/{title}/{userId}")
    public ResponseEntity<ExpenseCategory> getAllExpenseCategoryByTitle(@PathVariable String title, @PathVariable int userId) {
        try {
            return status(HttpStatus.OK).body(this.expenseCategoryService.getExpenseCategoryByTitle(title, userId));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/add")
    public ResponseEntity<ExpenseCategory> addExpenseCategory(@RequestBody String newExpenseCategoryJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExpenseCategory newExpenseCategory = objectMapper.readValue(newExpenseCategoryJson, ExpenseCategory.class);
            ExpenseCategory createdExpenseCategory = this.expenseCategoryService.save(newExpenseCategory);
            if (createdExpenseCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdExpenseCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseCategoryController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/update")
    public ResponseEntity<ExpenseCategory> updateExpenseCategory(@RequestBody String updatedExpenseCategoryString
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseCategory updatedExpenseCategory;
        try {
            updatedExpenseCategory = objectMapper.readValue(updatedExpenseCategoryString, ExpenseCategory.class);
            ExpenseCategory newUpdatedExpenseCategory = this.expenseCategoryService.save(updatedExpenseCategory);
            if (newUpdatedExpenseCategory != null || updatedExpenseCategoryString.trim().equals("")) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedExpenseCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/delete/byId/{expenseCategoryId}")
    public ResponseEntity<ResponseMessage> deleteExpenseCategoryById(@PathVariable Long expenseCategoryId
    ) {
        if (this.expenseCategoryService.deleteById(expenseCategoryId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your expense category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Category cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/delete/byTitle/{title}/{userId}")
    public ResponseEntity<ResponseMessage> deleteExpenseCategoryByTitle(@PathVariable String title, @PathVariable int userId
    ) {
        if (this.expenseCategoryService.deleteByTitle(title, userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your expense category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense category does not exists!"));
        }
    }

}

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

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/all")
    public ResponseEntity<List<ExpenseCategory>> getAllExpenseCategory() {
        List<ExpenseCategory> expenseCategoryList = this.expenseCategoryService.getAllExpenseCategory();
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

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/get/byTitle/{title}")
    public ResponseEntity<ExpenseCategory> getAllExpenseCategoryByTitle(@PathVariable String title) {
        ExpenseCategory expenseCategoryList = this.expenseCategoryService.getExpenseCategoryByTitle(title);
        if (expenseCategoryList != null) {
            return status(HttpStatus.OK).body(expenseCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/add")
    public ResponseEntity<ExpenseCategory> addExpenseCategory(@RequestBody String newExpenseCategoryJson) {
        if (newExpenseCategoryJson != null) {
            ExpenseCategory newExpenseCategory = new ExpenseCategory();
            newExpenseCategory.setCategoryTitle(newExpenseCategoryJson);
            ExpenseCategory createdExpenseCategory = this.expenseCategoryService.save(newExpenseCategory);
            if (createdExpenseCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdExpenseCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseCategory());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/update")
    public ResponseEntity<ExpenseCategory> updateExpenseCategory(@RequestBody String updatedExpenseCategoryJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseCategory updatedExpenseCategory;
        try {
            updatedExpenseCategory = objectMapper.readValue(updatedExpenseCategoryJson, ExpenseCategory.class);
            ExpenseCategory newUpdatedExpenseCategory = this.expenseCategoryService.save(updatedExpenseCategory);
            if (newUpdatedExpenseCategory != null) {
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
    public ResponseEntity<ResponseMessage> deleteExpenseCategoryById(@PathVariable Long expenseCategoryId) {
        if (this.expenseCategoryService.deleteById(expenseCategoryId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your expense category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Category cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECATEGORY + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteExpenseCategoryByTitle(@PathVariable String title) {
        if (this.expenseCategoryService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your expense category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense category does not exists!"));
        }
    }

}

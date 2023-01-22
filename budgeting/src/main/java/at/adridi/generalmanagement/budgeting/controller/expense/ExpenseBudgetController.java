/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseBudget;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseBudgetService;
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
 *
 * API: Expense Budget - Budget Expense Categories
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseBudgetController {

    @Autowired
    private ExpenseBudgetService expenseBudgetService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEBUDGET + "/certainMonthYear/{month}/{year}/{userId}")
    public ResponseEntity<List<ExpenseBudget>> getAllExpenseBudgetOfCertainMonthYear(@PathVariable int month, @PathVariable int year, @PathVariable int userId) {
        List<ExpenseBudget> expenseBudgetList = new ArrayList<>();
        try {
            expenseBudgetList = this.expenseBudgetService.getAllExpenseBudgetByMonthYear(month, year, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseBudgetList)) {
            return status(HttpStatus.OK).body(expenseBudgetList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseBudget>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEBUDGET + "/get/byId/{id}")
    public ResponseEntity<ExpenseBudget> getExpenseBudgetById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.expenseBudgetService.getExpenseBudgetById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ExpenseBudget());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEBUDGET + "/add")
    public ResponseEntity<ExpenseBudget> addExpenseBudget(@RequestBody String newExpenseBudgetJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        ExpenseBudget newExpenseBudget;
        ExpenseBudget savedExpenseBudget;
        try {
            newExpenseBudget = objectMapper.readValue(newExpenseBudgetJson, ExpenseBudget.class);
            savedExpenseBudget = this.expenseBudgetService.save(newExpenseBudget);
            return ResponseEntity.status(HttpStatus.OK).body(savedExpenseBudget);
        } catch (IOException ex) {
            Logger.getLogger(ExpenseBudgetController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseBudget());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEBUDGET + "/update")
    public ResponseEntity<ExpenseBudget> updateExpenseBudget(@RequestBody String updatedExpenseBudgetJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseBudget updatedExpenseBudget;
        ExpenseBudget savedExpenseBudget;
        try {
            updatedExpenseBudget = objectMapper.readValue(updatedExpenseBudgetJson, ExpenseBudget.class);
            System.out.println("updateExpenseBudget UPDATE!!");
            savedExpenseBudget = this.expenseBudgetService.save(updatedExpenseBudget);
            return ResponseEntity.status(HttpStatus.OK).body(savedExpenseBudget);
        } catch (IOException ex) {
            Logger.getLogger(ExpenseBudgetController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseBudget());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEBUDGET + "/delete/byId/{expenseBudgetId}")
    public ResponseEntity<ResponseMessage> deleteExpenseCategoryById(@PathVariable Long expenseBudgetId) {
        if (this.expenseBudgetService.deleteById(expenseBudgetId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your expense Budget was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Budget cannot be null!"));
        }
    }

}

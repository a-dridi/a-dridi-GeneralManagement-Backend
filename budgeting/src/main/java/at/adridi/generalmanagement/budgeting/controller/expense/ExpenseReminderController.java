/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseReminder;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseReminderJSON;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseReminderService;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseService;
import at.adridi.generalmanagement.budgeting.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Expense Reminders - for Expenses Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseReminderController {

    @Autowired
    private ExpenseReminderService expenseReminderService;
    @Autowired
    private ExpenseService expenseService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEREMINDER + "/get/byExpenseId/{expenseId}")
    public ResponseEntity<ExpenseReminder> getExpenseReminderByExpenseId(@PathVariable Long expenseId) {
        try {
            return ResponseEntity.ok(this.expenseReminderService.getExpenseReminderByExpenseId(expenseId));
        } catch (DataValueNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEREMINDER + "/add")
    public ResponseEntity<ExpenseReminder> addExpenseReminder(@RequestBody String newExpenseReminderJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
            ExpenseReminderJSON expenseReminderJSON;
            expenseReminderJSON = objectMapper.readValue(newExpenseReminderJson, ExpenseReminderJSON.class);
            
            Expense expenseOfReminder = this.expenseService.getExpenseById(expenseReminderJSON.getExpenseId());
            
            ExpenseReminder newExpenseReminder = new ExpenseReminder(expenseOfReminder, expenseReminderJSON.getDueDate(), expenseReminderJSON.getPayedDate());
            ExpenseReminder savedExpenseReminder;
            savedExpenseReminder = this.expenseReminderService.save(newExpenseReminder);
            return ResponseEntity.status(HttpStatus.OK).body(savedExpenseReminder);
        } catch (Exception ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExpenseReminder());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEREMINDER + "/payExpense")
    public ResponseEntity<ResponseMessage> updateExpenseReminderPayState(@RequestBody String updatedExpenseReminderJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseReminderJSON expenseReminderJSON;
        try {
            expenseReminderJSON = objectMapper.readValue(updatedExpenseReminderJson, ExpenseReminderJSON.class);
            
            Expense expenseOfReminder = this.expenseService.getExpenseById(expenseReminderJSON.getExpenseId());
            
            ExpenseReminder updatedEpenseReminder = new ExpenseReminder(expenseReminderJSON.getExpensereminderId(), expenseOfReminder, expenseReminderJSON.getDueDate(), expenseReminderJSON.getPayedDate());
            if (this.expenseReminderService.payExpenseReminder(updatedEpenseReminder)) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Expense paid."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense Reminder could not be updated and paid!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("SERVER ERROR. Expense Reminder could not be updated and paid!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEREMINDER + "/delete/{expenseReminderId}")
    public ResponseEntity<ResponseMessage> deleteExpenseReminder(@PathVariable Long expenseReminderId) {
        if (this.expenseReminderService.deleteById(expenseReminderId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your expense reminder was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense reminder does not exists!"));
        }
    }
    
        @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEREMINDER + "/delete/{expenseId}")
    public ResponseEntity<ResponseMessage> deleteExpenseReminderByExpenseId(@PathVariable Long expenseId) {
        if (this.expenseReminderService.deleteByExpenseId(expenseId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your expense reminder was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense reminder does not exists!"));
        }
    }

}

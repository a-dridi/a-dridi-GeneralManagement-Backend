/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseBudgetService;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseDevelopmentService;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseService;
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
 * API: Expense - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseTableController {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ExpenseBudgetService expenseBudgetService;
    @Autowired
    private ExpenseDevelopmentService expenseDevelopmentService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/all/{userId}")
    public ResponseEntity<List<Expense>> getAllExpense(@PathVariable int userId) {
        List<Expense> expenseList = new ArrayList<>();
        try {
            expenseList = this.expenseService.getAllExpense(userId);
            this.expenseDevelopmentService.checkAndUpdate(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseList)) {
            return status(HttpStatus.OK).body(expenseList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/certainYear/{year}/{userId}")
    public ResponseEntity<List<Expense>> getExpensesOfCertainYear(@PathVariable int year, @PathVariable int userId) {
        List<Expense> expenseList = new ArrayList<>();
        try {
            expenseList = this.expenseService.getExpensesOfCertainYear(year, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseList)) {
            return status(HttpStatus.OK).body(expenseList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/certainMonthYear/{month}/{year}/{userId}")
    public ResponseEntity<List<Expense>> getExpensesOfCertainMonthYear(@PathVariable int month, @PathVariable int year, @PathVariable int userId) {
        List<Expense> expenseList = new ArrayList<>();
        try {
            expenseList = this.expenseService.getExpensesByMonthYear(month, year, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseList)) {
            return status(HttpStatus.OK).body(expenseList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/byId/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.expenseService.getExpenseById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Expense());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/byTitle/{title}/{userId}")
    public ResponseEntity<List<Expense>> getAllExpenseByTitle(@PathVariable String title, @PathVariable int userId) {
        List<Expense> expenseList = new ArrayList<>();
        try {
            expenseList = this.expenseService.getExpenseByTitle(title, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseList)) {
            return status(HttpStatus.OK).body(expenseList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/byCategory/{userId}")
    public ResponseEntity<List<Expense>> getAllExpenseByExpenseCategory(@RequestBody String expenseCategoryJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseCategory expenseCategory;
        try {
            expenseCategory = objectMapper.readValue(expenseCategoryJson, ExpenseCategory.class);
            List<Expense> expenseList = new ArrayList<>();
            try {
                expenseList = this.expenseService.getExpensesByCategoryAndUserId(expenseCategory, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(expenseList)) {
                return status(HttpStatus.OK).body(expenseList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/byTimerange/{userId}")
    public ResponseEntity<List<Expense>> getAllExpenseByExpenseTimerange(@RequestBody String expenseTimerangeJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseTimerange expenseTimerange;
        try {
            expenseTimerange = objectMapper.readValue(expenseTimerangeJson, ExpenseTimerange.class);
            List<Expense> expenseList = new ArrayList<>();
            try {
                expenseList = this.expenseService.getExpensesByExpenseTimerangeAndUserId(expenseTimerange, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(expenseList)) {
                return status(HttpStatus.OK).body(expenseList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Expense>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/add")
    public ResponseEntity<Expense> addExpense(@RequestBody String newExpenseJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Expense newExpense;
        Expense savedExpense;
        try {
            newExpense = objectMapper.readValue(newExpenseJson, Expense.class);
            savedExpense = this.expenseService.save(newExpense);
            this.expenseBudgetService.updateExpensesOfAExpenseBudgetCategory(savedExpense);

            this.expenseDevelopmentService.addExpenseDevelopmentOfCurrentMonthYear(savedExpense.getCentValue(), savedExpense.getExpenseTimerange().getTimerangeId(), savedExpense.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(savedExpense);
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Expense());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/update")
    public ResponseEntity<Expense> updateExpense(@RequestBody String updatedExpenseJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Expense oldExpense;
        Expense updatedExpense;
        Expense savedExpense;
        try {
            updatedExpense = objectMapper.readValue(updatedExpenseJson, Expense.class);
            oldExpense = this.expenseService.getExpenseById(updatedExpense.getExpenseId());
            savedExpense = this.expenseService.save(updatedExpense);
            this.expenseBudgetService.updateExpensesOfAExpenseBudgetCategory(savedExpense);
            this.expenseDevelopmentService.checkAndUpdate(oldExpense.getUserId());
            this.expenseDevelopmentService.updateExpenseDevelopmentOfCurrentMonthYear(oldExpense.getCentValue(), savedExpense.getCentValue(), savedExpense.getExpenseTimerange().getTimerangeId(), savedExpense.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(savedExpense);
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Expense());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateExpenseTableData(@RequestBody String updatedExpenseJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Expense updatedExpense;

        try {
            updatedExpense = objectMapper.readValue(updatedExpenseJson, Expense.class);
            Expense oldExpense = this.expenseService.getExpenseById(updatedExpense.getExpenseId());
            this.expenseDevelopmentService.checkAndUpdate(oldExpense.getUserId());

            if (this.expenseService.updateExpenseTableData(updatedExpense.getTitle(), updatedExpense.getExpenseCategory().getExpenseCategoryId(), updatedExpense.getCentValue(), updatedExpense.getExpenseTimerange().getTimerangeId(), updatedExpense.getPaymentDate(), updatedExpense.getInformation(), updatedExpense.getExpenseId(), updatedExpense.getUserId()) != -1) {
                if (oldExpense.getExpenseTimerange().getTimerangeId().equals(updatedExpense.getExpenseTimerange().getTimerangeId())) {
                    this.expenseDevelopmentService.updateExpenseDevelopmentOfCurrentMonthYear(oldExpense.getCentValue(), updatedExpense.getCentValue(), updatedExpense.getExpenseTimerange().getTimerangeId(), oldExpense.getUserId());
                } else {
                    //Timerange was changed - Change calculation method in expense development
                    this.expenseDevelopmentService.deleteExpenseDevelopmentOfCurrentMonthYear(oldExpense.getCentValue(), oldExpense.getExpenseTimerange().getTimerangeId(), oldExpense.getUserId());
                    this.expenseDevelopmentService.addExpenseDevelopmentOfCurrentMonthYear(updatedExpense.getCentValue(), updatedExpense.getExpenseTimerange().getTimerangeId(), updatedExpense.getUserId());
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Expense updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/updateExpenseCategories/{oldExpenseCategoryId}/{newExpenseCategoryId}/{userId}")
    public ResponseEntity<ResponseMessage> updateExpenseCategories(@PathVariable long oldExpenseCategoryId, @PathVariable long newExpenseCategoryId, @PathVariable int userId) {
        if (this.expenseService.updateExpensesExpenseCategoryId(oldExpenseCategoryId, newExpenseCategoryId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Categories for expenses were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Categories for expenses COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/delete/{expenseId}")
    public ResponseEntity<ResponseMessage> deleteExpense(@PathVariable Long expenseId) {
        Expense deletedExpense = this.expenseService.getExpenseById(expenseId);
        this.expenseDevelopmentService.checkAndUpdate(deletedExpense.getUserId());

        if (this.expenseService.deleteById(expenseId)) {
            this.expenseBudgetService.updateExpensesOfAExpenseBudgetCategory(deletedExpense);
            this.expenseDevelopmentService.deleteExpenseDevelopmentOfCurrentMonthYear(deletedExpense.getCentValue(), deletedExpense.getExpenseTimerange().getTimerangeId(), deletedExpense.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your expense was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Expense does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/sum/monthly/{userId}")
    public ResponseEntity<Integer> getMonthlyExpensesSum(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.expenseService.getMonthlyRecurringExpensesSum(userId));
        } catch (DataValueNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/sum/yearly/{userId}")
    public ResponseEntity<Integer> getYearlyExpensesSum(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.expenseService.getYearlyRecurringExpensesSum(userId));
        } catch (DataValueNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/sum/single/custom/certainMonth/{month}/{userId}")
    public ResponseEntity<Integer> getOfCertainMonthSingleAndCustomExpensesSum(@PathVariable int month, @PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.expenseService.getSumOfSingleAndCustomExpensesByCertainMonth(month, userId));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/get/sum/currentMonth/{userId}")
    public ResponseEntity<Integer> getCurrentMonthExpensesSum(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(this.expenseService.getCurrentMonthExpenses(userId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSE + "/restore/{expenseId}")
    public ResponseEntity<ResponseMessage> restoreDeletedExpense(@PathVariable int expenseId) {
        try {
            this.expenseService.restoreDeletedExpense(expenseId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(expenseId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

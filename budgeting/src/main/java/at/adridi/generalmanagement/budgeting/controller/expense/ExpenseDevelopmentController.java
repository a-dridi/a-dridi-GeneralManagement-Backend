/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.Earning;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseDevelopment;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseDevelopmentService;
import at.adridi.generalmanagement.budgeting.util.ApiEndpoints;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Expense - Development. Data for displaying the expenses for the last 24
 * months, but also the last 15 years.
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseDevelopmentController {

    @Autowired
    private ExpenseDevelopmentService expenseDevelopmentService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEDEVELOPMENT + "/last/24/months/{userId}")
    public ResponseEntity<List<ExpenseDevelopment>> getExpensesOfLast24Months(@PathVariable int userId) {
        List<ExpenseDevelopment> expenseDevelopmentList = new ArrayList<>();
        try {
            expenseDevelopmentList = this.expenseDevelopmentService.getMonthlyExpenseDevelopmentOfLastCertainMonths(24, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseDevelopmentList)) {
            return status(HttpStatus.OK).body(expenseDevelopmentList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseDevelopment>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEDEVELOPMENT + "/last/15/years/{userId}")
    public ResponseEntity<List<ExpenseDevelopment>> getExpensesOfLast15Years(@PathVariable int userId) {
        List<ExpenseDevelopment> expenseDevelopmentList = new ArrayList<>();
        try {
            expenseDevelopmentList = this.expenseDevelopmentService.getYearlyExpenseDevelopmentOfLastCertainYears(15, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expenseDevelopmentList)) {
            return status(HttpStatus.OK).body(expenseDevelopmentList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseDevelopment>());
        }
    }

}

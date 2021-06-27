/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseGraph;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseService;
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
 * REST API Endpoint Expense Graph
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseGraphController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEGRAPH + "/expensesSum/monthly/{userId}")
    public ResponseEntity<List<ExpenseGraph>> getAllMonthlyExpenseSum(@PathVariable int userId) {
        List<ExpenseGraph> expensesSumList = new ArrayList<>();
        try {
            expensesSumList = this.expenseService.getMonthlySumExpenses(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expensesSumList)) {
            return status(HttpStatus.OK).body(expensesSumList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseGraph>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEGRAPH + "/expensesSum/yearly/{userId}")
    public ResponseEntity<List<ExpenseGraph>> getAllYearlyExpenseSum(@PathVariable int userId) {
        List<ExpenseGraph> expensesSumList = new ArrayList<>();
        try {
            expensesSumList = this.expenseService.getYearlySumExpenses(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expensesSumList)) {
            return status(HttpStatus.OK).body(expensesSumList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseGraph>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSEGRAPH + "/expensesSum/currentYear/{userId}")
    public ResponseEntity<List<ExpenseGraph>> getAllCurrentYearExpenseSum(@PathVariable int userId) {
        List<ExpenseGraph> expensesSumList = new ArrayList<>();
        try {
            expensesSumList = this.expenseService.getSumExpensesOfCurrentYear(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(expensesSumList)) {
            return status(HttpStatus.OK).body(expensesSumList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseGraph>());
        }
    }

}

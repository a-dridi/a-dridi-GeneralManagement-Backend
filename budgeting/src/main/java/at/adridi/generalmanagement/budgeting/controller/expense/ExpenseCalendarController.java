/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCalendar;
import at.adridi.generalmanagement.budgeting.service.expense.ExpenseCalendarService;
import at.adridi.generalmanagement.budgeting.util.ApiEndpoints;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * API for Expense Calendar tab of Expense.
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class ExpenseCalendarController {

    @Autowired
    private ExpenseCalendarService expenseCalendarService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EXPENSECALENDAR + "/all/{userId}")
    public ResponseEntity<List<ExpenseCalendar>> getExpenseById(@PathVariable int userId) {
        try {
            return status(HttpStatus.OK).body(this.expenseCalendarService.getExpensesCalendarData(userId));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<ExpenseCalendar>());
        }
    }
}

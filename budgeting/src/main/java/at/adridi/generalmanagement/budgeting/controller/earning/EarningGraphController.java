/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.EarningGraph;
import at.adridi.generalmanagement.budgeting.service.earning.EarningService;
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
public class EarningGraphController {

    @Autowired
    private EarningService earningService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGGRAPH + "/earningsSum/monthly/{userId}")
    public ResponseEntity<List<EarningGraph>> getAllMonthlyEarningSum(@PathVariable int userId) {
        List<EarningGraph> earningsSumList = new ArrayList<>();
        try {
            earningsSumList = this.earningService.getMonthlySumEarnings(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningsSumList)) {
            return status(HttpStatus.OK).body(earningsSumList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningGraph>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGGRAPH + "/earningsSum/yearly/{userId}")
    public ResponseEntity<List<EarningGraph>> getAllYearlyExpenseSum(@PathVariable int userId) {
        List<EarningGraph> earningsSumList = new ArrayList<>();
        try {
            earningsSumList = this.earningService.getYearlySumEarnings(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningsSumList)) {
            return status(HttpStatus.OK).body(earningsSumList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningGraph>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGGRAPH + "/earningsSum/currentYear/{userId}")
    public ResponseEntity<List<EarningGraph>> getAllCurrentYearEarningSum(@PathVariable int userId) {
        List<EarningGraph> earningsSumList = new ArrayList<>();
        try {
            earningsSumList = this.earningService.getSumEarningsOfCurrentYear(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningsSumList)) {
            return status(HttpStatus.OK).body(earningsSumList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningGraph>());
        }
    }

}

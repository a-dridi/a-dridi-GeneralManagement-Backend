/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.EarningDevelopment;
import at.adridi.generalmanagement.budgeting.service.earning.EarningDevelopmentService;
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
 * API: Earning - Development. Data for displaying the earnings for the last 24
 * months, but also the last 15 years.
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class EarningDevelopmentController {

    @Autowired
    private EarningDevelopmentService earningDevelopmentService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGDEVELOPMENT + "/last/24/months/{userId}")
    public ResponseEntity<List<EarningDevelopment>> getEarningsOfLast24Months(@PathVariable int userId) {
        List<EarningDevelopment> earningDevelopmentList = new ArrayList<>();
        try {
            earningDevelopmentList = this.earningDevelopmentService.getMonthlyEarningDevelopmentOfLastCertainMonths(24, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningDevelopmentList)) {
            return status(HttpStatus.OK).body(earningDevelopmentList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningDevelopment>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGDEVELOPMENT + "/last/15/years/{userId}")
    public ResponseEntity<List<EarningDevelopment>> getEarningsOfLast15Years(@PathVariable int userId) {
        List<EarningDevelopment> earningsDevelopmentList = new ArrayList<>();
        try {
            earningsDevelopmentList = this.earningDevelopmentService.getYearlyEarningDevelopmentOfLastCertainYears(15, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningsDevelopmentList)) {
            return status(HttpStatus.OK).body(earningsDevelopmentList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningDevelopment>());
        }
    }

}

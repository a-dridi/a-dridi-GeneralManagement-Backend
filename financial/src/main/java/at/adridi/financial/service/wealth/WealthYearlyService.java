/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.wealth;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.wealth.WealthYearly;
import at.adridi.financial.repository.wealth.WealthMonthlyRepository;
import at.adridi.financial.repository.wealth.WealthYearlyRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Wealth Yearly DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class WealthYearlyService {

    @Autowired
    private WealthYearlyRepository wealthYearlyRepository;

    @Autowired
    private WealthMonthlyRepository wealthMonthlyRepository;

    /**
     * Save new wealth yearly.
     *
     * @param newWealthYearly
     * @return saved wealth yearly object. Null if not successful.
     */
    @Transactional
    public WealthYearly save(WealthYearly newWealthYearly) {
        if (newWealthYearly == null) {
            return null;
        }
        return this.wealthYearlyRepository.save(newWealthYearly);
    }

    /**
     * Get certain wealth yearly with the passed id. Throws
     * DataValueNotFoundException if wealth yearly is not available.
     *
     * @param id
     * @return
     */
    public WealthYearly getWealthYearlyById(Long id) {
        return this.wealthYearlyRepository.findByWealthyearlyId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Wealth Yearly Does Not Exist"));
    }

    /**
     * Get a List of all saved wealth yearly items of a user
     *
     * @param userId
     * @return
     */
    public List<WealthYearly> getAllWealthYearly(int userId) {
        try {
            this.updateWealthYearlyState(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.wealthYearlyRepository.getAllWealthYearlyList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Wealth Yearly items or does not exist"));
    }

    /**
     * Creates missing wealth yearly items earning to the current year. The
     * expenses, earnings, and difference is calculated also from all the months
     * of a every year. This ensures that there are wealth yearly items for
     * every year.
     */
    public boolean updateWealthYearlyState(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat formatYYYY = new SimpleDateFormat("yyyy");

        WealthYearly latestWealthYearly = this.wealthYearlyRepository.getLatestWealthYearlyOfUser(userId).orElse(null);
        if (latestWealthYearly == null || latestWealthYearly.getYearDate() == 0) {
            int currentYear = Integer.parseInt(formatYYYY.format(currentDate));
            WealthYearly newWealthYearly = new WealthYearly();
            newWealthYearly.setYearDate(currentYear);
            int expenseCentCurrentYear = this.wealthMonthlyRepository.getExpenseCentSumOfYearDate(currentYear, userId);
            int earningCentCurrentYear = this.wealthMonthlyRepository.getEarningCentSumOfYearDate(currentYear, userId);
            int differenceCentCurrentYear = this.wealthMonthlyRepository.getDifferenceCentSumOfYearDate(currentYear, userId);
            newWealthYearly.setExpenseCent(expenseCentCurrentYear);
            newWealthYearly.setEarningCent(earningCentCurrentYear);
            newWealthYearly.setDifferenceCent(differenceCentCurrentYear);
            newWealthYearly.setUserId(userId);
            this.save(newWealthYearly);
        } else {
            int latestYear = latestWealthYearly.getYearDate();
            int currentYear = Integer.parseInt(formatYYYY.format(currentDate));

            while (currentYear != latestYear) {
                latestYear++;
                WealthYearly newWealthYearly = new WealthYearly();
                newWealthYearly.setYearDate(latestYear);
                int expenseCentLatestYear = this.wealthMonthlyRepository.getExpenseCentSumOfYearDate(latestYear, userId);
                int earningCentLatestYear = this.wealthMonthlyRepository.getEarningCentSumOfYearDate(latestYear, userId);
                int differenceCentLatestYear = this.wealthMonthlyRepository.getDifferenceCentSumOfYearDate(latestYear, userId);
                newWealthYearly.setExpenseCent(expenseCentLatestYear);
                newWealthYearly.setEarningCent(earningCentLatestYear);
                newWealthYearly.setDifferenceCent(differenceCentLatestYear);
                newWealthYearly.setUserId(userId);
                this.save(newWealthYearly);
            }
        }

        return true;
    }

    
    
    /**
     * Get the ImprovementPct value of the latest wealth yearly by calculating
     * the change compared to the previous year of this updated yearly wealth
     * item.
     *
     * @return
     */
    @Transactional
    public double getImprovementPct(int previousYear, WealthYearly updatedWealthMonthly) {
        WealthYearly previousWealthItem = this.wealthYearlyRepository.getWealthYearlyByYearDate(previousYear, updatedWealthMonthly.getUserId()).orElse(null);

        if (previousWealthItem == null) {
            return 0.0;
        } else {
            double improvementPct = ((double) (updatedWealthMonthly.getDifferenceCent() - previousWealthItem.getDifferenceCent()) / (double) previousWealthItem.getDifferenceCent());
            return improvementPct;
        }

    }
    
    /**
     * Update the ImprovementPct value of the latest wealth yearly.
     *
     * @return
     */
    public boolean updateImprovementPct(int userId) {
        List<WealthYearly> latest2WealthYearlyList = this.wealthYearlyRepository.getLatest2WealthYearlyOfUser(userId).orElse(new ArrayList<>());
        if (!latest2WealthYearlyList.isEmpty()) {
            //The latest/current wealth yearly item (of the current month and year)
            WealthYearly latestWealthYearly1 = latest2WealthYearlyList.get(0);
            //The item before the latest 
            WealthYearly latestWealthYearly2 = latest2WealthYearlyList.get(1);
            double calculateImprovementPct = ((latestWealthYearly1.getDifferenceCent() - latestWealthYearly2.getDifferenceCent()) / latestWealthYearly2.getDifferenceCent()) * 100;
            latestWealthYearly1.setImprovementPct(calculateImprovementPct);
            this.save(latestWealthYearly1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update only table data (without attachment info) of an Wealth Yearly item
     *
     * @param yearDate
     * @param expenseCent
     * @param earningCent
     * @param differenceCent
     * @param improvementPct
     * @param notice
     * @param wealthyearlyId
     * @param userId
     * @return
     */
    @Transactional
    public int updateWealthYearlyTableData(int yearDate, int expenseCent, int earningCent, int differenceCent, double improvementPct, String notice, Long wealthyearlyId,
            int userId
    ) {
        try {
            this.wealthYearlyRepository.updateWealthYearlyTableData(yearDate, expenseCent, earningCent, differenceCent, improvementPct, notice, wealthyearlyId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete an existing wealth yearly
     *
     * @param wealthYearlyId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long wealthYearlyId
    ) {
        if (wealthYearlyId == null || wealthYearlyId == 0) {
            return false;
        }

        WealthYearly wealthYearly = null;
        try {
            wealthYearly = this.getWealthYearlyById(wealthYearlyId);
        } catch (DataValueNotFoundException e) {
        }

        if (wealthYearly != null) {
            try {
                this.wealthYearlyRepository.delete(wealthYearly);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

}

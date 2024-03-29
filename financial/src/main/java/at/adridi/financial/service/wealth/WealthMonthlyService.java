/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.wealth;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.wealth.WealthMonthly;
import at.adridi.financial.repository.wealth.WealthMonthlyRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Wealth Monthly DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class WealthMonthlyService {

    @Autowired
    private WealthMonthlyRepository wealthMonthlyRepository;

    /**
     * Save new wealth monthly.
     *
     * @param newWealthMonthly
     * @return saved wealth monthly object. Null if not successful.
     */
    @Transactional
    public WealthMonthly save(WealthMonthly newWealthMonthly) {
        if (newWealthMonthly == null) {
            return null;
        }
        return this.wealthMonthlyRepository.save(newWealthMonthly);
    }

    /**
     * Get certain wealth monthly with the passed id. Throws
     * DataValueNotFoundException if wealth monthly is not available.
     *
     * @param id
     * @return
     */
    public WealthMonthly getWealthMonthlyById(Long id) {
        return this.wealthMonthlyRepository.findByWealthmonthlyId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Wealth Monthly Does Not Exist"));
    }

    /**
     * Get a List of all saved wealth monthly items of a user
     *
     * @param userId
     * @return
     */
    public List<WealthMonthly> getAllWealthMonthly(int userId) {
        try {
            this.updateWealthMonthlyState(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.wealthMonthlyRepository.getAllWealthMonthlyList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Wealth Monthly items or does not exist"));
    }

    /**
     * Creates missing wealth monthly items to the current month and year. This
     * ensures that there are wealth monthly items for every month and year.
     */
    @Transactional
    public boolean updateWealthMonthlyState(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat formatMM = new SimpleDateFormat("MM");
        SimpleDateFormat formatYYYY = new SimpleDateFormat("yyyy");

        WealthMonthly latestWealthMonthly = this.wealthMonthlyRepository.getLatestWealthMonthlyOfUser(userId).orElse(null);
        if (latestWealthMonthly == null || latestWealthMonthly.getMonthDate() == 0) {
            WealthMonthly newWealthMonthly = new WealthMonthly();
            newWealthMonthly.setMonthDate(Integer.parseInt(formatMM.format(currentDate)));
            newWealthMonthly.setYearDate(Integer.parseInt(formatYYYY.format(currentDate)));
            newWealthMonthly.setUserId(userId);
            this.save(newWealthMonthly);
        } else {
            int latestMonth = latestWealthMonthly.getMonthDate();
            int latestYear = latestWealthMonthly.getYearDate();
            int currentMonth = Integer.parseInt(formatMM.format(currentDate));
            int currentYear = Integer.parseInt(formatYYYY.format(currentDate));

            //Run "add monthly wealth" till to the current month and year
            while (true) {
                if (currentMonth == latestMonth && currentYear == latestYear) {
                    break;
                }

                if (latestMonth == 12) {
                    latestMonth = 1;
                    latestYear++;
                } else {
                    latestMonth++;
                }
                WealthMonthly newWealthMonthly = new WealthMonthly();
                newWealthMonthly.setMonthDate(latestMonth);
                newWealthMonthly.setYearDate(latestYear);
                newWealthMonthly.setUserId(userId);
                this.save(newWealthMonthly);
            }
        }

        return true;
    }

    /**
     * Get the ImprovementPct value of the latest wealth monthly by calculating
     * the change compared to the previous month of this updated monthly wealth
     * item.
     *
     * @return
     */
    @Transactional
    public double getImprovementPct(int previousMonth, int previousYear, WealthMonthly updatedWealthMonthly) {
        WealthMonthly previousWealthItem = this.wealthMonthlyRepository.getByMonthDateAndYearDate(previousMonth, previousYear, updatedWealthMonthly.getUserId()).orElse(null);

        if (previousWealthItem == null) {
            return 0.0;
        } else {
            double improvementPct = ((double) (updatedWealthMonthly.getDifferenceCent() - previousWealthItem.getDifferenceCent()) / (double) previousWealthItem.getDifferenceCent());
            return improvementPct;
        }

    }

    /**
     * Update only table data (without attachment info) of an Wealth Monthly
     * item
     *
     * @param monthDate
     * @param yearDate
     * @param expenseCent
     * @param earningCent
     * @param differenceCent
     * @param improvementPct
     * @param notice
     * @param wealthmonthlyId
     * @param userId
     * @return
     */
    @Transactional
    public int updateWealthMonthlyTableData(int monthDate, int yearDate, int expenseCent, int earningCent, int differenceCent, double improvementPct, String notice, Long wealthmonthlyId,
            int userId
    ) {
        try {
            this.wealthMonthlyRepository.updateWealthMonthlyTableData(monthDate, yearDate, expenseCent, earningCent, differenceCent, improvementPct, notice, wealthmonthlyId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete an existing wealth monthly
     *
     * @param wealthMonthlyId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long wealthMonthlyId
    ) {
        if (wealthMonthlyId == null || wealthMonthlyId == 0) {
            return false;
        }

        WealthMonthly wealthMonthly = null;
        try {
            wealthMonthly = this.getWealthMonthlyById(wealthMonthlyId);
        } catch (DataValueNotFoundException e) {
        }

        if (wealthMonthly != null) {
            try {
                this.wealthMonthlyRepository.delete(wealthMonthly);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

}

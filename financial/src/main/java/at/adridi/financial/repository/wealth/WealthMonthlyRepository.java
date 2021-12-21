/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.wealth;

import at.adridi.financial.model.savings.Savings;
import at.adridi.financial.model.wealth.WealthMonthly;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface WealthMonthlyRepository extends JpaRepository<WealthMonthly, Long> {

    @Query(value = "SELECT * FROM Wealth_Monthly WHERE month_year=?1 AND year_date=?2 AND user_id=?3", nativeQuery = true)
    Optional<WealthMonthly> findByMonthDateAndYearDate(int monthDate, int yearDate, int userId);

    Optional<WealthMonthly> findByWealthmonthlyId(Long wealthmonthlyId);

    @Query(value = "SELECT * FROM Wealth_Monthly WHERE user_id = ?1 ORDER BY wealthmonthly_id DESC LIMIT 1", nativeQuery = true)
    Optional<WealthMonthly> getLatestWealthMonthlyOfUser(int userId);

    @Query(value = "SELECT * FROM Wealth_Monthly WHERE user_id = ?1 ORDER BY wealthmonthly_id DESC LIMIT 2", nativeQuery = true)
    Optional<ArrayList<WealthMonthly>> getLatest2WealthMonthlyOfUser(int userId);

    @Query(value = "SELECT * FROM Wealth_Monthly WHERE user_id=?1", nativeQuery = true)
    Optional<ArrayList<WealthMonthly>> getAllWealthMonthlyList(int userId);

    @Query(value = "SELECT * FROM Wealth_Monthly WHERE year_date=?1 AND user_id=?2 ", nativeQuery = true)
    Optional<ArrayList<WealthMonthly>> getAllWealthMonthlyByYearDate(int yearDate, int userId);

    @Query(value = "SELECT SUM(expense_cent) FROM Wealth_Monthly WHERE year_date=?1 AND user_id=?2 ", nativeQuery = true)
    int getExpenseCentSumOfYearDate(int yearDate, int userId);

    @Query(value = "SELECT SUM(earning_cent) FROM Wealth_Monthly WHERE year_date=?1 AND user_id=?2 ", nativeQuery = true)
    int getEarningCentSumOfYearDate(int yearDate, int userId);

    @Query(value = "SELECT SUM(difference_cent) FROM Wealth_Monthly WHERE year_date=?1 AND user_id=?2 ", nativeQuery = true)
    int getDifferenceCentSumOfYearDate(int yearDate, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Wealth_Monthly SET month_date = ?1, year_date = ?2, expense_cent = ?3, earning_cent = ?4, difference_cent=?5, improvement_pct=?6, notice=?7 WHERE wealthmonthly_id=?8 and user_id=?9", nativeQuery = true)
    void updateWealthMonthlyTableData(int monthDate, int yearDate, int expenseCent, int earningCent, int differenceCent, double improvementPct, String notice, Long wealthmonthlyId, int userId);

}

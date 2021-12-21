/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.wealth;

import at.adridi.financial.model.wealth.WealthYearly;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author A.Dridi
 */
public interface WealthYearlyRepository extends JpaRepository<WealthYearly, Long> {

    Optional<WealthYearly> findByWealthyearlyId(Long wealthmonthlyId);

    @Query(value = "SELECT * FROM Wealth_Yearly WHERE user_id = ?1 ORDER BY wealthyearly_id DESC LIMIT 1", nativeQuery = true)
    Optional<WealthYearly> getLatestWealthYearlyOfUser(int userId);

    @Query(value = "SELECT * FROM Wealth_Yearly WHERE user_id = ?1 ORDER BY wealthyearly_id DESC LIMIT 2", nativeQuery = true)
    Optional<ArrayList<WealthYearly>> getLatest2WealthYearlyOfUser(int userId);

    @Query(value = "SELECT * FROM Wealth_Yearly WHERE user_id=?1", nativeQuery = true)
    Optional<ArrayList<WealthYearly>> getAllWealthYearlyList(int userId);

    @Query(value = "SELECT * FROM Wealth_Yearly WHERE year_date=?1 AND user_id=?2", nativeQuery = true)
    Optional<WealthYearly> getWealthYearlyByYearDate(int yearDate, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Wealth_Yearly SET year_date = ?1, expense_cent = ?2, earning_cent = ?3, difference_cent=?4, improvement_pct=?5, notice=?6 WHERE wealthyearly_id=?7 and user_id=?8", nativeQuery = true)
    void updateWealthYearlyTableData(int yearDate, int expenseCent, int earningCent, int differenceCent, double improvementPct, String notice, Long wealthyearlyId, int userId);

}

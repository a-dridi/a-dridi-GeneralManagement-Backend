/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.earning;

import at.adridi.generalmanagement.budgeting.model.earning.EarningDevelopment;
import java.util.ArrayList;
import java.util.List;
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
public interface EarningDevelopmentRepository extends JpaRepository<EarningDevelopment, Long> {

    Optional<EarningDevelopment> findByEarningDevelopmentId(Long earningDevelopmentId);

    Optional<List<EarningDevelopment>> findByMonthAndYearAndUserId(int month, int year, int userId);

    Optional<List<EarningDevelopment>> findByDateDisplayAndUserId(String dateDisplay, int userId);

    @Query(value = "SELECT * FROM Earning_Development ORDER BY earning_development_id ASC", nativeQuery = true)
    Optional<ArrayList<EarningDevelopment>> getAllEarningDevelopmentList(Integer userId);

    @Query(value = "SELECT * FROM Earning_Development WHERE user_id = ?1 ORDER BY earning_development_id DESC LIMIT 24", nativeQuery = true)
    Optional<List<EarningDevelopment>> getLast24EarningDevelopmentList(int userId);

    @Query(value = "SELECT * FROM Earning_Development WHERE user_id = ?1 ORDER BY earning_development_id DESC LIMIT 1", nativeQuery = true)
    Optional<EarningDevelopment> getLastEarningDevelopmentRow(int userId);

    @Query(value = "SELECT * FROM Earning_Development WHERE month >=?1 AND month <=?2 AND year >=?3 AND year <=?4 AND user_id = ?5 ORDER BY earning_development_id ASC", nativeQuery = true)
    Optional<List<EarningDevelopment>> getEarningDevelopmentItemsForMonthAndYearRange(int monthStart, int monthEnd, int yearStart, int yearEnd, int userId);

    @Query(value = "SELECT SUM(cent_sum) FROM Earning_Development WHERE year = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Integer> getCentSumOfEarningDevelopmentOfCertainYear(Integer year, Integer userId);

    @Query(value = "SELECT SUM(cent_sum) FROM Earning_Development WHERE year >= ?1 AND year <= ?2 user_id = ?3", nativeQuery = true)
    Optional<Integer> getCentSumOfEarningDevelopmentOfYearRange(Integer startYear, Integer endYear, Integer userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Earning_Development SET month=?1, year=?2, date_display=?3, cent_sum=?4 WHERE earning_development_id=?5 and user_id=?6", nativeQuery = true)
    void updateEarningDevelopmentTableData(Integer month, Integer year, String dateDisplay, Integer centSum, Long earningDevelopmentId, int userId);

}

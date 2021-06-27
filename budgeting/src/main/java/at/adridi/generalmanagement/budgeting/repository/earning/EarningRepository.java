/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.earning;

import at.adridi.generalmanagement.budgeting.model.earning.Earning;
import at.adridi.generalmanagement.budgeting.model.earning.EarningCategory;
import at.adridi.generalmanagement.budgeting.model.earning.EarningTimerange;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 *
 * @author A.Dridi
 */
@Repository
public interface EarningRepository extends JpaRepository<Earning, Long> {

    Optional<Earning> findByEarningId(Long earningId);

    Optional<ArrayList<Earning>> findByUserId(int userId);

    Optional<ArrayList<Earning>> findByTitleAndUserId(String title, int userId);

    Optional<ArrayList<Earning>> findByEarningCategoryAndUserId(EarningCategory earningCategory, int userId);

    Optional<ArrayList<Earning>> findByEarningTimerangeAndUserId(EarningTimerange earningTimerange, int userId);

    @Query(value = "SELECT * FROM Earning WHERE user_id=?1 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> getAllEarningList(int userId);

    @Query(value = "SELECT * FROM Earning WHERE ((earning_timerange_timerange_id = 1 AND (EXTRACT(year FROM earning_date)) = ?1) OR (earning_timerange_timerange_id != 1)) AND ((earning_timerange_timerange_id = 12 AND (EXTRACT(year FROM earning_date)) = ?1) OR (earning_timerange_timerange_id != 12)) AND user_id=?2 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> getEarningsOfCertainYear(int year, int userId);

    @Query(value = "SELECT * FROM Earning WHERE ((earning_timerange_timerange_id = 1 AND (EXTRACT(month FROM earning_date)) = ?1 AND (EXTRACT(year FROM earning_date))=?2) OR (earning_timerange_timerange_id != 1)) AND ((earning_timerange_timerange_id = 12 AND (EXTRACT(year FROM earning_date)) = ?1) OR (earning_timerange_timerange_id != 12)) AND user_id=?3 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> getEarningsOfCertainMonthYear(int month, int year, int userId);

    @Query(value = "SELECT * FROM Earning WHERE earning_category_earning_category_id = ?1 AND user_id=?2 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> getEarningsByEarningCategoryId(long earningCategoryId, int userId);

    @Query(value = "SELECT * FROM Earning WHERE earning_timerange_timerange_id = ?1 AND user_id=?2 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> getEarningsByTimerangeIdAndUserId(long earningTimerangeId, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = 5 AND user_id = ?1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getMonthlyEarningSum(int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = 9 AND user_id = ?1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getYearlyEarningSum(int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = ?1 AND user_id = ?2 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerange(long earningTimerangeId, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = ?1 AND (EXTRACT(month from earning_date) = ?2) AND user_id = ?3 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCertainMonth(long earningTimerangeId, int month, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = ?1 AND (EXTRACT(year from earning_date) = ?2) AND user_id = ?3 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCertainYear(long earningTimerangeId, int year, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = ?1 AND (EXTRACT(month from earning_date) = ?2) AND (EXTRACT(year from earning_date) = ?3) AND user_id = ?4 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCertainMonthYear(long earningTimerangeId, int month, int year, int userId);

    @Query(value = "SELECT * FROM Earning WHERE (EXTRACT(month FROM earning_date) = ?1) AND EXTRACT(year FROM earning_date) = ?2 AND user_id=?3 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> findByMonthAndYear(int month, int year, int userId);

    @Query(value = "SELECT * FROM Earning WHERE (EXTRACT(year FROM earning_date) = ?1) AND user_id=?2 AND deleted=false ORDER BY earning_id DESC", nativeQuery = true)
    Optional<ArrayList<Earning>> findByYear(int year, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Earning SET deleted=false WHERE earning_id=?1", nativeQuery = true)
    void restoreDeletedEarning(int earningId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Earning SET earning_category_earning_category_id = ?1 WHERE earning_category_earning_category_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateCategoryOfAllEarning(long newEarningCategoryId, long oldEarningCategoryId, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Earning SET title=?1, earning_category_earning_category_id=?2, cent_value=?3, earning_timerange_timerange_id=?4, earning_date=cast(?5 AS timestamp), information=?6 WHERE earning_id=?7 and user_id=?8", nativeQuery = true)
    void updateEarningTableData(String title, Long earningCategoryId, int centValue, Long timerangeId, String earningDate, String information, Long earningId, int userId);

    @Query(value = "SELECT sum(cent_value) FROM Earning WHERE earning_timerange_timerange_id=?1 AND user_id=?2 AND earning_category_earning_category_id=?3 AND deleted=false GROUP BY earning_category_earning_category_id ", nativeQuery = true)
    Optional<Integer> getSumEarningsByTimerangeIdEarningCategoryId(Long timerangeId, int userId, Long earningCategoryId);

    @Query(value = "SELECT SUM(cent_value) FROM Earning WHERE earning_timerange_timerange_id = ?1 AND earning_category_earning_category_id = ?2 AND (EXTRACT(month from earning_date) = ?3) AND user_id = ?4 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCategoryAndCertainMonth(long earningTimerangeId, long earningCategoriyId, int month, int userId);

    @Query(value = "SELECT sum(cent_value) FROM Earning WHERE (EXTRACT(year FROM earning_date)=?1) AND user_id=?2 AND earning_timerange_timerange_id=1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumSingleEarningByYear(int year, int userId);

    @Query(value = "SELECT sum(cent_value) FROM Earning WHERE  (EXTRACT(month FROM earning_date)=?1) AND user_id=?2 AND earning_timerange_timerange_id=1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumSingleEarningByMonth(int month, int userId);

}

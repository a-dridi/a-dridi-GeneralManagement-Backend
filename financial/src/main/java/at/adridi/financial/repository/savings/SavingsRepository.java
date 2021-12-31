/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.savings;

import at.adridi.financial.model.savings.Savings;
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
public interface SavingsRepository extends JpaRepository<Savings, Long> {

    Optional<Savings> findBySavingsId(Long savingId);

    @Query(value = "SELECT * FROM Savings WHERE user_id=?1 AND deleted=false", nativeQuery = true)
    Optional<ArrayList<Savings>> getAllSavingsList(int userId);

    @Query(value = "SELECT * FROM Savings WHERE savings_frequency=?1 AND user_id=?2  AND deleted=false", nativeQuery = true)
    Optional<ArrayList<Savings>> getAllSavingsBySavingsFrequency(Integer savingsFrequency, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Savings SET deleted=false WHERE savings_id=?1", nativeQuery = true)
    void restoreDeletedSavings(Long savingsId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Savings SET description = ?1, target_cent = ?2, step_amount_cent = ?3, savings_frequency = ?4, saved_till_now_cent=?5, last_savings_update_date=?6, notice=?7 WHERE savings_id=?8 and user_id=?9", nativeQuery = true)
    void updateSavingsTableData(String description, Integer targetCent, Integer stepAmountCent, Integer savingsFrequency, Integer savedTillNowCent, Date lastSavingsUpdateDate, String notice, Long savingsId, int userId);

}

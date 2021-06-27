/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.repository.decision;

import at.adridi.generalmanagement.common.model.decision.CriteriaOption;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author A.Dridi
 */
@Repository
public interface CriteriaOptionRepository extends JpaRepository<CriteriaOption, Long> {

    Optional<CriteriaOption> findByCriteriaoptionId(Long criteriaoptionId);

    @Query(value = "SELECT * FROM Criteria_Option WHERE decision_decision_id = ?1 AND user_id=?2", nativeQuery = true)
    Optional<ArrayList<CriteriaOption>> getAllByDecisionIdAndUserId(int decisionId, int userId);

    @Query(value = "SELECT * FROM Criteria_Option WHERE user_id=?1", nativeQuery = true)
    Optional<ArrayList<CriteriaOption>> getAllCriteriaOptionList(int userId);

    @Query(value = "SELECT * FROM Criteria_Option WHERE LOWER(criteria_title) ILIKE %?1% AND user_id=?2", nativeQuery = true)
    Optional<ArrayList<CriteriaOption>> searchItemByCriteriaTitle(String criteriaTitle, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Criteria_Option WHERE decision_decision_id=?1", nativeQuery = true)
    void deleteByDecisionId(Long decisionId);
}

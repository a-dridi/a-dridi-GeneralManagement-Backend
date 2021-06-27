/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.repository.decision;

import at.adridi.generalmanagement.common.model.decision.Decision;
import at.adridi.generalmanagement.common.model.decision.CriteriaOptionPoint;
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
public interface CriteriaOptionPointRepository extends JpaRepository<CriteriaOptionPoint, Long> {

    Optional<CriteriaOptionPoint> findByOptionpointId(Long optionpointId);

    @Query(value = "SELECT * FROM Criteria_Option_Point WHERE user_id=?1 ORDER BY optionpoint_id ASC", nativeQuery = true)
    Optional<ArrayList<CriteriaOptionPoint>> getAllOptionPointList(int userId);

    @Query(value = "SELECT * FROM Criteria_Option_Point WHERE criteria_id = ?1 AND user_id=?2 ORDER BY optionpoint_id ASC", nativeQuery = true)
    Optional<ArrayList<CriteriaOptionPoint>> getOptionPointListByCriteriaId(int criteriaId, int userId);

    @Query(value = "SELECT SUM(total) FROM Criteria_Option_Point WHERE decision_option_id = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Integer> getSumTotalOfDecisionOptionId(long decisionOptionId, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Criteria_Option_Point WHERE decision_id=?1", nativeQuery = true)
    void deleteByDecisionId(Long decisionId);

}

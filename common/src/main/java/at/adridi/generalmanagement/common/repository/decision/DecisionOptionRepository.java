/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.repository.decision;

import at.adridi.generalmanagement.common.model.decision.DecisionOption;
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
public interface DecisionOptionRepository extends JpaRepository<DecisionOption, Long> {

    Optional<DecisionOption> findByDecisionoptionId(Long decisionoptionId);

    @Query(value = "SELECT * FROM Decision_Option WHERE user_id=?1", nativeQuery = true)
    Optional<ArrayList<DecisionOption>> getAllDecisionOptionList(int userId);

    @Query(value = "SELECT * FROM Decision_Option WHERE decision_decision_id = ?1 AND user_id=?2", nativeQuery = true)
    Optional<ArrayList<DecisionOption>> getAllByDecisionId(int decisionId, int userId);

    @Query(value = "SELECT * FROM Decision_Option WHERE LOWER(title) ILIKE %?1% AND user_id=?2", nativeQuery = true)
    Optional<ArrayList<DecisionOption>> searchItemByTitle(String title, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Decision_Option WHERE decision_decision_id=?1", nativeQuery = true)
    void deleteByDecisionId(Long decisionId);

}

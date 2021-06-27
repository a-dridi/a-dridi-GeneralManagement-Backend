/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.repository.decision;

import at.adridi.generalmanagement.common.model.decision.Decision;
import at.adridi.generalmanagement.common.model.organization.Organization;
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
public interface DecisionRepository extends JpaRepository<Decision, Long> {

    Optional<Decision> findByDecisionId(Long decisionId);

    @Query(value = "SELECT * FROM Decision WHERE user_id=?1", nativeQuery = true)
    Optional<ArrayList<Decision>> getAllDecisionList(int userId);

    @Query(value = "SELECT * FROM Decision WHERE LOWER(title) ILIKE %?1% AND user_id=?2", nativeQuery = true)
    Optional<ArrayList<Decision>> searchItemByTitle(String title, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Decision SET title = ?1, chosen_option = ?2, chosen_option_id = ?3, information=?4 WHERE decision_id=?5 and user_id=?6", nativeQuery = true)
    void updateDecisionTableData(String title, String chosenOption, Long chosenOptionId, String information, Long decisionId, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Decision SET chosen_option_id = ?1, chosen_option = ?2 WHERE decision_id=?3 and user_id=?4", nativeQuery = true)
    void setChosenDecisionOption(Long chosenOptionId, String chosenOption, Long decisionId, int userId);

}

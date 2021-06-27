/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.service.decision;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.decision.Decision;
import at.adridi.generalmanagement.common.repository.decision.CriteriaOptionPointRepository;
import at.adridi.generalmanagement.common.repository.decision.CriteriaOptionRepository;
import at.adridi.generalmanagement.common.repository.decision.DecisionOptionRepository;
import at.adridi.generalmanagement.common.repository.decision.DecisionRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of decision DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class DecisionService {

    @Autowired
    private DecisionRepository decisionRepository;
    @Autowired
    private DecisionOptionRepository decisionOptionRepository;
    @Autowired
    private CriteriaOptionRepository criteriaOptionRepository;
    @Autowired
    private CriteriaOptionPointRepository criteriaOptionPointRepository;

    /**
     * Save new decision.
     *
     * @param newDecision
     * @return saved decision object. Null if not successful.
     */
    @Transactional
    public Decision save(Decision newDecision) {
        if (newDecision == null) {
            return null;
        }
        return this.decisionRepository.save(newDecision);
    }

    /**
     * Get certain Decision with the passed id. Throws
     * DataValueNotFoundException if Decision is not available.
     *
     * @param id
     * @return
     */
    public Decision getDecisionById(Long id) {
        return this.decisionRepository.findByDecisionId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Decision Does Not Exist"));
    }

    /**
     * Get a List of all saved decision items of a user
     *
     * @param userId
     * @return
     */
    public List<Decision> getAllDecision(int userId) {
        return this.decisionRepository.getAllDecisionList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Decision items or does not exist"));
    }

    /**
     * Get a List of the found decision items of a user for your searched title.
     *
     * @param userId
     * @return
     */
    public List<Decision> searchDecisionItemByTitle(String title, int userId) {
        return this.decisionRepository.searchItemByTitle(title, userId).orElseThrow(() -> new DataValueNotFoundException("Decision items with the title '" + title + "' not found "));
    }

    /**
     * Update only table data of an Decision item
     *
     * @param title
     * @param chosen_option_decision_option_id
     * @param information
     * @param decisionId
     * @param userId
     * @return
     */
    @Transactional
    public int updateDecisionTableData(String title, String chosenOption, Long chosenOptionId, String information, Long decisionId, int userId) {
        try {
            this.decisionRepository.updateDecisionTableData(title, chosenOption, chosenOptionId, information, decisionId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

/**
 * Update the chosen option of a certain decision
 * @param chosen_option_decision_option_id
 * @param chosenOption
 * @param decisionId
 * @param userId
 * @return 
 */
    @Transactional
    public int setChosenDecisionOption(Long chosen_option_decision_option_id, String chosenOption, Long decisionId, int userId) {
        try {
            this.decisionRepository.setChosenDecisionOption(chosen_option_decision_option_id, chosenOption, decisionId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete an existing decision
     *
     * @param decisionId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long decisionId) {
        if (decisionId == null || decisionId == 0) {
            return false;
        }
        Decision decision = null;
        try {
            decision = this.getDecisionById(decisionId);
        } catch (DataValueNotFoundException e) {
        }

        if (decision != null) {
            try {
                this.decisionOptionRepository.deleteByDecisionId(decisionId);
                this.criteriaOptionRepository.deleteByDecisionId(decisionId);
                this.criteriaOptionPointRepository.deleteByDecisionId(decisionId);
                this.decisionRepository.delete(decision);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}

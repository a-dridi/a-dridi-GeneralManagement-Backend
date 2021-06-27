/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.service.decisionOption;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.decision.DecisionOption;
import at.adridi.generalmanagement.common.repository.decision.DecisionOptionRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of DecisionOptionOption DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class DecisionOptionService {

    @Autowired
    private DecisionOptionRepository decisionOptionRepository;

    /**
     * Save new decisionOption.
     *
     * @param newDecisionOption
     * @return saved decisionOption object. Null if not successful.
     */
    @Transactional
    public DecisionOption save(DecisionOption newDecisionOption) {
        if (newDecisionOption == null) {
            return null;
        }
        return this.decisionOptionRepository.save(newDecisionOption);
    }

    /**
     * Get certain DecisionOption with the passed id. Throws
     * DataValueNotFoundException if DecisionOption is not available.
     *
     * @param id
     * @return
     */
    public DecisionOption getDecisionOptionById(Long id) {
        return this.decisionOptionRepository.findByDecisionoptionId(id)
                .orElseThrow(() -> new DataValueNotFoundException("DecisionOption Does Not Exist"));
    }

    /**
     * Get a List of all saved decisionOption items of a user
     *
     * @param userId
     * @return
     */
    public List<DecisionOption> getAllDecisionOption(int userId) {
        return this.decisionOptionRepository.getAllDecisionOptionList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have DecisionOption items or does not exist"));
    }

    /**
     * Get a List of saved decisionOption items of a decision and user
     *
     * @param userId
     * @return
     */
    public List<DecisionOption> getDecisionOptionListByDecision(int decisionId, int userId) {
        return this.decisionOptionRepository.getAllByDecisionId(decisionId, userId).orElseThrow(() -> new DataValueNotFoundException("DecisionOption items for decision do not exist"));
    }

    /**
     * Get a List of the found decisionOption items of a user for your searched
     * title.
     *
     * @param userId
     * @return
     */
    public List<DecisionOption> searchDecisionOptionItemByTitle(String title, int userId) {
        return this.decisionOptionRepository.searchItemByTitle(title, userId).orElseThrow(() -> new DataValueNotFoundException("DecisionOption items with the title '" + title + "' not found "));
    }

    /**
     * Delete an existing decisionOption
     *
     * @param decisionOptionId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long decisionOptionId) {
        if (decisionOptionId == null || decisionOptionId == 0) {
            return false;
        }
        DecisionOption decisionOption = null;
        try {
            decisionOption = this.getDecisionOptionById(decisionOptionId);
        } catch (DataValueNotFoundException e) {
        }

        if (decisionOption != null) {
            try {
                this.decisionOptionRepository.delete(decisionOption);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}

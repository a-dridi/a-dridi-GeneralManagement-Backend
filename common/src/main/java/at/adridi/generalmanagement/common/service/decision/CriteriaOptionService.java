/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.service.criteriaOption;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.decision.CriteriaOption;
import at.adridi.generalmanagement.common.model.decision.Decision;
import at.adridi.generalmanagement.common.repository.decision.CriteriaOptionRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CriteriaOption DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class CriteriaOptionService {

    @Autowired
    private CriteriaOptionRepository criteriaOptionRepository;

    /**
     * Save new criteriaOption.
     *
     * @param newCriteriaOption
     * @return saved criteriaOption object. Null if not successful.
     */
    @Transactional
    public CriteriaOption save(CriteriaOption newCriteriaOption) {
        if (newCriteriaOption == null) {
            return null;
        }
        return this.criteriaOptionRepository.save(newCriteriaOption);
    }

    /**
     * Get certain CriteriaOption with the passed id. Throws
     * DataValueNotFoundException if CriteriaOption is not available.
     *
     * @param id
     * @return
     */
    public CriteriaOption getCriteriaOptionById(Long id) {
        return this.criteriaOptionRepository.findByCriteriaoptionId(id)
                .orElseThrow(() -> new DataValueNotFoundException("CriteriaOption Does Not Exist"));
    }

    /**
     * Get a List of the found criteria option items of a user for your searched
     * criteria title.
     *
     * @param userId
     * @return
     */
    public List<CriteriaOption> searchCriteriaOptionItemByTitle(String criteriaTitle, int userId) {
        return this.criteriaOptionRepository.searchItemByCriteriaTitle(criteriaTitle, userId).orElseThrow(() -> new DataValueNotFoundException("CriteriaOption items with the criteria title '" + criteriaTitle + "' not found "));
    }

    /**
     * Get a List of all saved criteriaOption items of a decision and user
     * @param decisionId
     * @param userId
     * @return 
     */
    public List<CriteriaOption> getAllCriteriaOptionOfDecision(int decisionId, int userId) {
        return this.criteriaOptionRepository.getAllByDecisionIdAndUserId(decisionId, userId).orElseThrow(() -> new DataValueNotFoundException("CriteriaOption items for decision do not exist"));
    }

    /**
     * Get a List of all saved criteriaOption items of a user
     *
     * @param userId
     * @return
     */
    public List<CriteriaOption> getAllCriteriaOption(int userId) {
        return this.criteriaOptionRepository.getAllCriteriaOptionList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have CriteriaOption items or does not exist"));
    }

    /**
     * Delete an existing criteriaOption
     *
     * @param criteriaOptionId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long criteriaOptionId) {
        if (criteriaOptionId == null || criteriaOptionId == 0) {
            return false;
        }
        CriteriaOption criteriaOption = null;
        try {
            criteriaOption = this.getCriteriaOptionById(criteriaOptionId);
        } catch (DataValueNotFoundException e) {
        }

        if (criteriaOption != null) {
            try {
                this.criteriaOptionRepository.delete(criteriaOption);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}

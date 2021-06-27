package at.adridi.generalmanagement.common.service.decision;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.decision.CriteriaOptionPoint;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import at.adridi.generalmanagement.common.repository.decision.CriteriaOptionPointRepository;

/**
 * Implementation of optionPoint DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class CriteriaOptionPointService {

    @Autowired
    private CriteriaOptionPointRepository criteriaOptionPointRepository;

    /**
     * Save new optionPoint.
     *
     * @param newOptionPoint
     * @return saved optionPoint object. Null if not successful.
     */
    @Transactional
    public CriteriaOptionPoint save(CriteriaOptionPoint newOptionPoint) {
        if (newOptionPoint == null) {
            return null;
        }
        return this.criteriaOptionPointRepository.save(newOptionPoint);
    }

    /**
     * Get certain OptionPoint with the passed id. Throws
     * DataValueNotFoundException if OptionPoint is not available.
     *
     * @param id
     * @return
     */
    public CriteriaOptionPoint getOptionPointById(Long id) {
        return this.criteriaOptionPointRepository.findByOptionpointId(id)
                .orElseThrow(() -> new DataValueNotFoundException("OptionPoint Does Not Exist"));
    }

    /**
     * Get a List of all saved optionPoint items of a user
     *
     * @param userId
     * @return
     */
    public List<CriteriaOptionPoint> getAllOptionPoint(int userId) {
        return this.criteriaOptionPointRepository.getAllOptionPointList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have OptionPoint items or does not exist"));
    }

    /**
     * Get a List of all saved optionPoint items of a criteria Id and user
     *
     * @param userId
     * @return
     */
    public List<CriteriaOptionPoint> getOptionPointListByCriteriaId(int criteriaId, int userId) {
        return this.criteriaOptionPointRepository.getOptionPointListByCriteriaId(criteriaId, userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have OptionPoint items or does not exist"));
    }

    /**
     * Get the sum points of the decisionoption of a decision item of a user.
     *
     * @param userId
     * @return
     */
    public int getPointsSumOfDecisionOption(long decisionOptionId, int userId) {
        return this.criteriaOptionPointRepository.getSumTotalOfDecisionOptionId(decisionOptionId, userId)
                .orElseThrow(() -> new DataValueNotFoundException("DecisionOption Sum could not be loaded!"));
    }

    /**
     * Delete an existing optionPoint
     *
     * @param optionPointId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long optionPointId) {
        if (optionPointId == null || optionPointId == 0) {
            return false;
        }
        CriteriaOptionPoint optionPoint = null;
        try {
            optionPoint = this.getOptionPointById(optionPointId);
        } catch (DataValueNotFoundException e) {
        }

        if (optionPoint != null) {
            try {
                this.criteriaOptionPointRepository.delete(optionPoint);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}

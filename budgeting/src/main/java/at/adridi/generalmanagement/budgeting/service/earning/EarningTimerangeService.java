/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.EarningTimerange;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import at.adridi.generalmanagement.budgeting.repository.earning.EarningTimerangeRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class EarningTimerangeService {

    @Autowired
    private EarningTimerangeRepository earningTimerangeRepository;

    /**
     * Save new earning timerange.
     *
     * @param newEarningTimerange
     * @return 0 if successful. 1: Passed object is null. 2: Saving failed
     */
    @Transactional()
    public Integer save(EarningTimerange newEarningTimerange) {
        if (newEarningTimerange == null) {
            return 1;
        }

        if (this.earningTimerangeRepository.save(newEarningTimerange) != null) {
            return 0;
        } else {
            return 2;
        }
    }

    /**
     * Get certain earning timerange with the passed id. Throws
     * DataValueNotFoundException if earning timerange is not available.
     *
     * @param id
     * @return
     */
    public EarningTimerange getEarningTimerangeById(Long id) {
        return this.earningTimerangeRepository.findByTimerangeId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Timerange Does Not Exist"));
    }

    /**
     * Get certain earning timerange with the passed title. Throws
     * DataValueNotFoundException if earning timerange is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    public EarningTimerange getEarningTimerangeByTitle(String title) {
        return this.earningTimerangeRepository.findByTimerangeTitle(title)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Timerange Does Not Exist"));
    }

    /**
     * Get a List of all saved earning timeranges
     *
     * @return
     */
    public List<EarningTimerange> getAllEarningTimerange() {
        return this.earningTimerangeRepository.getAllEarningTimerangeList().orElseThrow(() -> new DataValueNotFoundException("Earning Timerange List could not be loaded!"));
    }

    /**
     * Delete an existing earning timerange
     *
     * @param earningTimerangeId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long earningTimerangeId) {
        if (earningTimerangeId == null || earningTimerangeId == 0) {
            return false;
        }
        EarningTimerange earningTimerange = null;
        try {
            earningTimerange = this.getEarningTimerangeById(earningTimerangeId);
        } catch (DataValueNotFoundException e) {
            return false;
        }

        try {
            this.earningTimerangeRepository.delete(earningTimerange);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Delete an existing earning timerange
     *
     * @param timerangeTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String timerangeTitle) {
        if (timerangeTitle == null || timerangeTitle.trim().isEmpty()) {
            return false;
        }

        EarningTimerange earningTimerange = null;
        try {
            earningTimerange = this.getEarningTimerangeByTitle(timerangeTitle);
        } catch (DataValueNotFoundException e) {
            return false;
        }
        try {
            this.earningTimerangeRepository.delete(earningTimerange);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

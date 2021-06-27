/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.EarningCategory;
import at.adridi.generalmanagement.budgeting.repository.earning.EarningCategoryRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of earningcategory DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class EarningCategoryService {

    @Autowired
    private EarningCategoryRepository earningCategoryRepository;

    /**
     * Save new earning category.
     *
     * @param newEarningCategory
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public EarningCategory save(EarningCategory newEarningCategory) {
        if (newEarningCategory == null) {
            return null;
        }
        return this.earningCategoryRepository.save(newEarningCategory);
    }

    /**
     * Get certain earning category with the passed id. Throws
     * DataValueNotFoundException if earning category is not available.
     *
     * @param id
     * @return
     */
    public EarningCategory getEarningCategoryById(Long id) {
        return this.earningCategoryRepository.findByEarningCategoryId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Category Does Not Exist"));
    }

    /**
     * Get certain earning category with the passed title. Throws
     * DataValueNotFoundException if earning category is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    public EarningCategory getEarningCategoryByTitle(String title) {
        return this.earningCategoryRepository.findByCategoryTitle(title)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Category Does Not Exist"));
    }

    /**
     * Get a List of all saved earning categories
     *
     * @return
     */
    public List<EarningCategory> getAllEarningCategory() {
        return this.earningCategoryRepository.getAllEarningCategoryList().orElseThrow(() -> new DataValueNotFoundException("Earning Category List could not be loaded!"));
    }

    /**
     * Delete an existing earning category
     *
     * @param earningCategoryId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long earningCategoryId) {
        if (earningCategoryId == null || earningCategoryId == 0) {
            return false;
        }

        EarningCategory earningCategory = this.getEarningCategoryById(earningCategoryId);
        try {
            this.earningCategoryRepository.delete(earningCategory);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Delete an existing earning category
     *
     * @param categoryTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String categoryTitle) {
        if (categoryTitle == null || categoryTitle.trim().isBlank()) {
            return false;
        }

        EarningCategory earningCategory = this.getEarningCategoryByTitle(categoryTitle);
        try {
            this.earningCategoryRepository.delete(earningCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.service.organization;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.organization.OrganizationCategory;
import at.adridi.generalmanagement.common.repository.organization.OrganizationCategoryRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of OrganizationCategory DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class OrganizationCategoryService {

    @Autowired
    private OrganizationCategoryRepository organizationCategoryRepository;

    /**
     * Save new organization category.
     *
     * @param newOrganizationCategory
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public OrganizationCategory save(OrganizationCategory newOrganizationCategory) {
        if (newOrganizationCategory == null) {
            return null;
        }
        return this.organizationCategoryRepository.save(newOrganizationCategory);
    }

    /**
     * Get certain organization category with the passed id. Throws
     * DataValueNotFoundException if organization category is not available.
     *
     * @param id
     * @return
     */
    public OrganizationCategory getOrganizationCategoryById(Long id) {
        return this.organizationCategoryRepository.findByOrganizationCategoryId(id).orElseThrow(() -> new DataValueNotFoundException("Organization Category Does Not Exist"));
    }

    /**
     * Get certain organization category with the passed category title. Throws
     * DataValueNotFoundException if organization category is not available.
     *
     * @param categoryTitle
     * @return
     */
    public OrganizationCategory getOrganizationCategoryByCategoryTitle(String categoryTitle) {
        return this.organizationCategoryRepository.findByCategoryTitle(categoryTitle).orElseThrow(() -> new DataValueNotFoundException("Organization Category Does Not Exist"));
    }

    /**
     * Get a List of all saved organization categories
     *
     * @return
     */
    public List<OrganizationCategory> getAllOrganizationCategory() {
        return this.organizationCategoryRepository.getAllOrganizationCategoryList().orElseThrow(() -> new DataValueNotFoundException("Organization Category List could not be loaded!"));
    }

    /**
     * Delete an existing organization category
     *
     * @param organizationCategoryId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long organizationCategoryId) {
        if (organizationCategoryId == null || organizationCategoryId == 0) {
            return false;
        }

        OrganizationCategory organizationCategory = this.getOrganizationCategoryById(organizationCategoryId);

        try {
            this.organizationCategoryRepository.delete(organizationCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing organization category
     *
     * @param categoryTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String categoryTitle) {
        if (categoryTitle == null || categoryTitle.trim().isEmpty()) {
            return false;
        }

        OrganizationCategory organizationCategory = this.getOrganizationCategoryByCategoryTitle(categoryTitle);

        try {
            this.organizationCategoryRepository.delete(organizationCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

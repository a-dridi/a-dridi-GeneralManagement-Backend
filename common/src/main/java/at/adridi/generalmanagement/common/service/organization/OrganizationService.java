/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.service.organization;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.organization.Organization;
import at.adridi.generalmanagement.common.model.organization.OrganizationCategory;
import at.adridi.generalmanagement.common.repository.organization.OrganizationCategoryRepository;
import at.adridi.generalmanagement.common.repository.organization.OrganizationRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of organization DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationCategoryRepository organizationCategoryRepository;
    @Autowired
    private OrganizationCategoryService organizationCategoryService;

    /**
     * Save new organization.
     *
     * @param newOrganization
     * @return saved organization object. Null if not successful.
     */
    @Transactional
    public Organization save(Organization newOrganization) {
        if (newOrganization == null) {
            return null;
        }
        return this.organizationRepository.save(newOrganization);
    }

    /**
     * Get certain Organization with the passed id. Throws
     * DataValueNotFoundException if Organization is not available.
     *
     * @param id
     * @return
     */
    public Organization getOrganizationById(Long id) {
        return this.organizationRepository.findByOrganizationId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Organization Does Not Exist"));
    }

    /**
     * Get certain Organization with the passed OrganizationCategory object.
     * Throws DataValueNotFoundException if Organization is not available.
     *
     * @param organizationCategory
     * @param userId
     * @return
     */
    public List<Organization> getOrganizationsByCategoryAndUserId(OrganizationCategory organizationCategory, int userId) {
        return this.organizationRepository.findByOrganizationCategoryAndUserId(organizationCategory, userId).orElseThrow(() -> new DataValueNotFoundException("Organization Does Not Exist"));
    }

    /**
     * Get a List of all saved organization items of a user
     *
     * @param userId
     * @return
     */
    public List<Organization> getAllOrganization(int userId) {
        return this.organizationRepository.getAllOrganizationList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Organization items or does not exist"));
    }

    /**
     * Get a List of the found organization items of a user for your searched
     * description.
     *
     * @param userId
     * @return
     */
    public List<Organization> searchOrganizationItemByDescription(String description, int userId) {
        return this.organizationRepository.searchItemByDescription(description, userId).orElseThrow(() -> new DataValueNotFoundException("Organization items with the description '" + description + "' not found "));
    }

    /**
     * Update only table data (without attachment info) of an Organization item
     *
     * @param description
     * @param organizationCategoryId
     * @param location
     * @param status
     * @param information
     * @param organizationId
     * @param userId
     * @return
     */
    @Transactional
    public int updateOrganizationTableData(String description, Long organizationCategoryId, String location, String status, String information, Long organizationId, int userId) {
        try {
            this.organizationRepository.updateOrganizationTableData(description, organizationCategoryId, location, status, information, organizationId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update organization categories of all organization items with
     * oldOrganizationCategoryId and userId to the new organization category id.
     * And delete old organization category object.
     *
     * @param oldOrganizationCategoryId
     * @param newOrganizationCategoryId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateOrganizationItemsCategoryId(long oldOrganizationCategoryId, long newOrganizationCategoryId, int userId) {
        if ((oldOrganizationCategoryId > 0 && newOrganizationCategoryId > 0 && userId > 0)) {
            OrganizationCategory oldOrganizationCategory = this.organizationCategoryService.getOrganizationCategoryById(oldOrganizationCategoryId);
            OrganizationCategory newOrganizationCategory = this.organizationCategoryService.getOrganizationCategoryById(newOrganizationCategoryId);

            if ((oldOrganizationCategory != null && !oldOrganizationCategory.getCategoryTitle().equals("")) && (newOrganizationCategory != null && !newOrganizationCategory.getCategoryTitle().equals(""))) {
                try {
                    this.organizationRepository.updateCategoryOfAllOrganizationItems(newOrganizationCategoryId, oldOrganizationCategoryId, userId);
                    this.organizationCategoryRepository.delete(oldOrganizationCategory);
                    return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Delete an existing organization
     *
     * @param organizationId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long organizationId) {
        if (organizationId == null || organizationId == 0) {
            return false;
        }
        Organization organization = null;
        try {
            organization = this.getOrganizationById(organizationId);
        } catch (DataValueNotFoundException e) {
        }

        if (organization != null) {
            organization.setDeleted(true);
            try {
                if (this.organizationRepository.save(organization) != null) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Set deleted parameter of certain data row. This makes it available again.
     *
     * @param organizationId
     */
    @Transactional
    public void restoreDeletedOrganization(Long organizationId) {
        this.organizationRepository.restoreDeletedOrganization(organizationId);
    }

}

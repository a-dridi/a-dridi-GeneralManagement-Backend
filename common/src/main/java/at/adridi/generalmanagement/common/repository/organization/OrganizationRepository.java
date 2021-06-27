/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.repository.organization;

import at.adridi.generalmanagement.common.model.organization.Organization;
import at.adridi.generalmanagement.common.model.organization.OrganizationCategory;
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
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByOrganizationId(Long organizationId);

    Optional<ArrayList<Organization>> findByOrganizationCategoryAndUserId(OrganizationCategory organizationCategory, int userId);

    @Query(value = "SELECT * FROM Organization WHERE user_id=?1 AND deleted=false", nativeQuery = true)
    Optional<ArrayList<Organization>> getAllOrganizationList(int userId);

    @Query(value="SELECT * FROM Organization WHERE LOWER(description) ILIKE %?1% AND user_id=?2 AND deleted=false", nativeQuery=true)
    Optional<ArrayList<Organization>>searchItemByDescription(String description, int userId);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Organization SET deleted=false WHERE organization_id=?1", nativeQuery = true)
    void restoreDeletedOrganization(Long organizationId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Organization SET description = ?1, organization_category_organization_category_id = ?2, location = ?3, status=?4, information=?5 WHERE organization_id=?6 and user_id=?7", nativeQuery = true)
    void updateOrganizationTableData(String description, Long organizationCategoryId, String location, String status, String information, Long organizationId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Organization SET organization_category_organization_category_id = ?1 WHERE organization_category_organization_category_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateCategoryOfAllOrganizationItems(long newOrganizationCategoryId, long oldOrganizationCategoryId, int userId);

}

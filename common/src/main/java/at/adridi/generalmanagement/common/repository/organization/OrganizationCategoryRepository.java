/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.repository.organization;

import at.adridi.generalmanagement.common.model.organization.OrganizationCategory;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface OrganizationCategoryRepository extends JpaRepository<OrganizationCategory, Long> {

    Optional<OrganizationCategory> findByOrganizationCategoryId(Long organizationCategoryId);

    Optional<OrganizationCategory> findByCategoryTitle(String categoryTitle);

    @Query(value = "SELECT * FROM Organization_Category ORDER BY category_title ASC", nativeQuery = true)
    Optional<ArrayList<OrganizationCategory>> getAllOrganizationCategoryList();

}

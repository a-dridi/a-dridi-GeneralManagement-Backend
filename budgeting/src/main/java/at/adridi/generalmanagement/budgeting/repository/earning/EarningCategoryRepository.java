/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.earning;

import at.adridi.generalmanagement.budgeting.model.earning.EarningCategory;
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
public interface EarningCategoryRepository extends JpaRepository<EarningCategory, Long> {

    Optional<EarningCategory> findByEarningCategoryId(Long earningCategoryId);

    Optional<EarningCategory> findByCategoryTitle(String categoryTitle);

    @Query(value = "SELECT * FROM Earning_Category ORDER BY category_title ASC", nativeQuery = true)
    Optional<ArrayList<EarningCategory>> getAllEarningCategoryList();

}

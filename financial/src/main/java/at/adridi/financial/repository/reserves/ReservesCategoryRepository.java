/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.reserves;

import at.adridi.financial.model.reserves.ReservesCategory;
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
public interface ReservesCategoryRepository extends JpaRepository<ReservesCategory, Long> {

    Optional<ReservesCategory> findByReservesCategoryId(Long reservesCategoryId);

    Optional<ReservesCategory> findByCategoryTitle(String categoryTitle);

    @Query(value = "SELECT * FROM Reserves_Category ORDER BY category_title ASC", nativeQuery = true)
    Optional<ArrayList<ReservesCategory>> getAllReservesCategoryList();

}

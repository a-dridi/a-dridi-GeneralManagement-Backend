/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.reserves;

import at.adridi.financial.model.reserves.Reserves;
import at.adridi.financial.model.reserves.ReservesCategory;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 *
 * @author A.Dridi
 */
@Repository
public interface ReservesRepository extends JpaRepository<Reserves, Long> {

    Optional<Reserves> findByReservesId(Long earningId);

    Optional<ArrayList<Reserves>> findByDescriptionAndUserId(String title, int userId);

    Optional<ArrayList<Reserves>> findByCategoryAndUserId(ReservesCategory reservesCategory, int userId);

    @Query(value = "SELECT * FROM Reserves WHERE user_id=?1 AND deleted=false", nativeQuery = true)
    Optional<ArrayList<Reserves>> getAllReservesList(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Reserves SET deleted=false WHERE reserves_id=?1", nativeQuery = true)
    void restoreDeletedReserves(int reservesId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Reserves SET category_reserves_category_id=?1, description=?2, amount=?3, currency=?4, storage_location=?5, notice=?6 WHERE reserves_id=?7 and user_id=?8", nativeQuery = true)
    void updateReservesTableData(Long reservesCategoryId, String description, float amount, String currency, String storageLocation, String notice, Long reservesId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Reserves SET category_reserves_category_id = ?1 WHERE category_reserves_category_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateCategoryOfAllReserves(long newReservesCategoryId, long oldReservesCategoryId, int userId);

}

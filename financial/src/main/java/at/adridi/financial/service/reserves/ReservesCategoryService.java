/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.reserves;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.reserves.ReservesCategory;
import at.adridi.financial.repository.reserves.ReservesCategoryRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of reservescategory DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ReservesCategoryService {

    @Autowired
    private ReservesCategoryRepository reservesCategoryRepository;

    /**
     * Save new reserves category.
     *
     * @param newReservesCategory
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public ReservesCategory save(ReservesCategory newReservesCategory) {
        if (newReservesCategory == null) {
            return null;
        }
        return this.reservesCategoryRepository.save(newReservesCategory);
    }

    /**
     * Get certain reserves category with the passed id. Throws
     * DataValueNotFoundException if reserves category is not available.
     *
     * @param id
     * @return
     */
    public ReservesCategory getReservesCategoryById(Long id) {
        return this.reservesCategoryRepository.findByReservesCategoryId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Reserves Category Does Not Exist"));
    }

    /**
     * Get certain reserves category with the passed title. Throws
     * DataValueNotFoundException if reserves category is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    public ReservesCategory getReservesCategoryByTitle(String title) {
        return this.reservesCategoryRepository.findByCategoryTitle(title)
                .orElseThrow(() -> new DataValueNotFoundException("Reserves Category Does Not Exist"));
    }

    /**
     * Get a List of all saved reserves categories
     *
     * @return
     */
    public List<ReservesCategory> getAllReservesCategory() {
        return this.reservesCategoryRepository.getAllReservesCategoryList().orElseThrow(() -> new DataValueNotFoundException("Reserves Category List could not be loaded!"));
    }

    /**
     * Delete an existing reserves category
     *
     * @param reservesCategoryId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long reservesCategoryId) {
        if (reservesCategoryId == null || reservesCategoryId == 0) {
            return false;
        }

        ReservesCategory reservesCategory = this.getReservesCategoryById(reservesCategoryId);
        try {
            this.reservesCategoryRepository.delete(reservesCategory);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Delete an existing reserves category
     *
     * @param categoryTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String categoryTitle) {
        if (categoryTitle == null || categoryTitle.trim().isEmpty()) {
            return false;
        }

        ReservesCategory reservesCategory = this.getReservesCategoryByTitle(categoryTitle);
        try {
            this.reservesCategoryRepository.delete(reservesCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}

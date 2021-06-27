/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.reserves;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.reserves.Reserves;
import at.adridi.financial.model.reserves.ReservesCategory;
import at.adridi.financial.repository.reserves.ReservesRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Implementation of reserves DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ReservesService {

    @Autowired
    private ReservesRepository reservesRepository;
    @Autowired
    private ReservesCategoryService reservesCategoryService;

    /**
     * Save new Reserves.
     *
     * @param newReserves
     * @return saved Reserves object. Null if not successful.
     */
    @Transactional
    public Reserves save(Reserves newReserves) {
        if (newReserves == null) {
            return null;
        }
        return this.reservesRepository.save(newReserves);

    }

    /**
     * Get certain reserves with the passed id. Throws
     * DataValueNotFoundException if reserves is not available.
     *
     * @param id
     * @return
     */
    public Reserves getReservesById(Long id) {
        return this.reservesRepository.findByReservesId(id).orElseThrow(() -> new DataValueNotFoundException("Reserves Item Does Not Exist"));
    }

    /**
     * Get certain reserves with the passed description. Throws
     * DataValueNotFoundException if reserves is not available.
     *
     * @param description
     * @param userId
     * @return
     */
    public List<Reserves> getReservesByDescription(String description, int userId) {
        return this.reservesRepository.findByDescriptionAndUserId(description, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Reserves Item Does Not Exist"));
    }

    /**
     * Get certain reserves with the passed category object. Throws
     * DataValueNotFoundException if reserves is not available.
     *
     * @param reservesCategory
     * @param userId
     * @return
     */
    public List<Reserves> getReservesByCategory(ReservesCategory reservesCategory, int userId) {
        return this.reservesRepository.findByCategoryAndUserId(reservesCategory, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Reserves Item Does Not Exist"));
    }

    /**
     * Get a List of all saved reserves items of a user
     *
     * @param userId
     * @return
     */
    public List<Reserves> getAllReserves(int userId) {
        return this.reservesRepository.getAllReservesList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have reserves or does not exist"));
    }

    /**
     * Delete an existing reserves item
     *
     * @param reservesId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long reservesId) {
        if (reservesId == null || reservesId == 0) {
            return false;
        }
        Reserves reserves = null;
        try {
            reserves = this.getReservesById(reservesId);
        } catch (DataValueNotFoundException e) {
        }

        if (reserves != null) {
            reserves.setDeleted(true);
            try {
                if (this.reservesRepository.save(reserves) != null) {
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
     * @param reservesId
     */
    @Transactional
    public void restoreDeletedReserves(int reservesId) {
        this.reservesRepository.restoreDeletedReserves(reservesId);
    }

    /**
     * Update reserves categories of all earnings with oldReservesCategoryId and
     * userId to the new reserves category id. And delete old reserves category
     * object.
     *
     * @param oldReservesCategoryId
     * @param newReservesCategoryId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateReservesCategoryId(long oldReservesCategoryId, long newReservesCategoryId, int userId) {
        if ((oldReservesCategoryId > 0 && newReservesCategoryId > 0 && userId > 0)) {
            ReservesCategory oldReservesCategory = this.reservesCategoryService.getReservesCategoryById(oldReservesCategoryId);
            ReservesCategory newReservesCategory = this.reservesCategoryService.getReservesCategoryById(newReservesCategoryId);

            if ((oldReservesCategory != null && !oldReservesCategory.getCategoryTitle().equals("")) && (newReservesCategory != null && !newReservesCategory.getCategoryTitle().equals(""))) {
                try {
                    this.reservesRepository.updateCategoryOfAllReserves(newReservesCategoryId, oldReservesCategoryId, userId);
                    this.reservesCategoryService.deleteById(oldReservesCategoryId);
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
     * Update only table data (without attachment info) of an Reserves item
     *
     * @param reservesCategoryId
     * @param description
     * @param amount
     * @param currency
     * @param storageLocation
     * @param notice
     * @param reservesId
     * @param userId
     * @return
     */
    @Transactional
    public int updateReservesTableData(Long reservesCategoryId, String description, float amount, String currency, String storageLocation, String notice, Long reservesId, int userId) {
        try {
            this.reservesRepository.updateReservesTableData(reservesCategoryId, description, amount, currency, storageLocation, notice, reservesId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}

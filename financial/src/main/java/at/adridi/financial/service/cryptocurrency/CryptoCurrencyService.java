/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.cryptocurrency;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.cryptocurrency.CryptoCurrency;
import at.adridi.financial.repository.cryptocurrency.CryptoCurrencyRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CryptoCurrency DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class CryptoCurrencyService {

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

   
    /**
     * Save new cryptocurrencies.
     *
     * @param newCryptoCurrency
     * @return saved cryptocurrencies object. Null if not successful.
     */
    @Transactional
    public CryptoCurrency save(CryptoCurrency newCryptoCurrency) {
        if (newCryptoCurrency == null) {
            return null;
        }
        return this.cryptoCurrencyRepository.save(newCryptoCurrency);
    }

    /**
     * Get certain CryptoCurrency with the passed id. Throws DataValueNotFoundException
     * if CryptoCurrency is not available.
     *
     * @param id
     * @return
     */
    public CryptoCurrency getCryptoCurrencyById(Long id) {
        return this.cryptoCurrencyRepository.findByCryptocurrencyId(id)
                .orElseThrow(() -> new DataValueNotFoundException("CryptoCurrency Does Not Exist"));
    }


    /**
     * Get a List of all saved cryptocurrencies items of a user
     *
     * @param userId
     * @return
     */
    public List<CryptoCurrency> getAllCryptoCurrency(int userId) {
        return this.cryptoCurrencyRepository.getAllCryptoCurrencyList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have CryptoCurrency items or does not exist"));
    }


   /**
    * Update only table data (without attachment info) of an CryptoCurrency item
    * @param amount
    * @param currency
    * @param storageLocation
    * @param notice
    * @param cryptocurrencyId
    * @param userId
    * @return 
    */
    @Transactional
    public int updateCryptoCurrencyTableData(float amount, String currency, String storageLocation, String notice, Long cryptocurrencyId, int userId
    ) {
        try {
            this.cryptoCurrencyRepository.updateCryptoCurrencyTableData(amount, currency, storageLocation, notice, cryptocurrencyId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete an existing cryptocurrencies
     *
     * @param cryptocurrenciesId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long cryptocurrenciesId
    ) {
        if (cryptocurrenciesId == null || cryptocurrenciesId == 0) {
            return false;
        }
        CryptoCurrency cryptocurrencies = null;
        try {
            cryptocurrencies = this.getCryptoCurrencyById(cryptocurrenciesId);
        } catch (DataValueNotFoundException e) {
        }

        if (cryptocurrencies != null) {
            cryptocurrencies.setDeleted(true);
            try {
                if (this.cryptoCurrencyRepository.save(cryptocurrencies) != null) {
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
     * @param cryptocurrencyId
     */
    @Transactional
    public void restoreDeletedCryptoCurrency(int cryptocurrencyId
    ) {
        this.cryptoCurrencyRepository.restoreDeletedCryptoCurrency(cryptocurrencyId);
    }

}

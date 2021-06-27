/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.cryptocurrency;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.cryptocurrency.CryptoCurrencyTransactions;
import at.adridi.financial.repository.cryptocurrency.CryptoCurrencyTransactionsRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CryptoCurrencyTransaction DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class CryptoCurrencyTransactionsService {

    @Autowired
    private CryptoCurrencyTransactionsRepository cryptoCurrencyRepository;

    /**
     * Save new cryptocurrencies.
     *
     * @param newCryptoCurrencyTransactions
     * @return saved cryptocurrencies object. Null if not successful.
     */
    @Transactional
    public CryptoCurrencyTransactions save(CryptoCurrencyTransactions newCryptoCurrencyTransactions) {
        if (newCryptoCurrencyTransactions == null) {
            return null;
        }
        return this.cryptoCurrencyRepository.save(newCryptoCurrencyTransactions);
    }

    /**
     * Get certain CryptoCurrencyTransactions with the passed id. Throws DataValueNotFoundException
     * if CryptoCurrencyTransaction is not available.
     *
     * @param id
     * @return
     */
    public CryptoCurrencyTransactions getCryptoCurrencyTransactionsById(Long id) {
        return this.cryptoCurrencyRepository.findByCryptocurrencytransactionId(id).orElseThrow(() -> new DataValueNotFoundException("CryptoCurrencyTransactions Does Not Exist"));
    }


    /**
     * Get a List of all saved cryptocurrencies items of a user
     *
     * @param userId
     * @return
     */
    public List<CryptoCurrencyTransactions> getAllCryptoCurrencyTransaction(int userId) {
        return this.cryptoCurrencyRepository.getAllCryptoCurrencyTransactionsList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have CryptoCurrencyTransactions items or does not exist"));
    }


    /**
     * Update only table data (without attachment info) of an CryptoCurrencyTransaction item
     * @param senderFrom
     * @param currencyFrom
     * @param destinationTo
     * @param currencyTo
     * @param amount
     * @param storageLocation
     * @param notice
     * @param cryptocurrencytransactionId
     * @param userId
     * @return 
     */
    @Transactional
    public int updateCryptoCurrencyTransactionsTableData(String senderFrom, String currencyFrom, String destinationTo, String currencyTo, float amount, String storageLocation, String notice, Long cryptocurrencytransactionId, int userId
    ) {
        try {
            this.cryptoCurrencyRepository.updateCryptoCurrencyTransactionsTableData(senderFrom, currencyFrom, destinationTo, currencyTo, amount, storageLocation, notice, cryptocurrencytransactionId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete an existing cryptocurrencies
     *
     * @param cryptocurrenciestransactionId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long cryptocurrenciestransactionId
    ) {
        if (cryptocurrenciestransactionId == null || cryptocurrenciestransactionId == 0) {
            return false;
        }
        CryptoCurrencyTransactions cryptoCurrencyTransactions = null;
        try {
            cryptoCurrencyTransactions = this.getCryptoCurrencyTransactionsById(cryptocurrenciestransactionId);
        } catch (DataValueNotFoundException e) {
        }

        if (cryptoCurrencyTransactions != null) {
            cryptoCurrencyTransactions.setDeleted(true);
            try {
                if (this.cryptoCurrencyRepository.save(cryptoCurrencyTransactions) != null) {
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
     * @param cryptocurrenciestransactionId
     */
    @Transactional
    public void restoreDeletedCryptoCurrencyTransaction(int cryptocurrenciestransactionId
    ) {
        this.cryptoCurrencyRepository.restoreDeletedCryptoCurrencyTransactions(cryptocurrenciestransactionId);
    }

}

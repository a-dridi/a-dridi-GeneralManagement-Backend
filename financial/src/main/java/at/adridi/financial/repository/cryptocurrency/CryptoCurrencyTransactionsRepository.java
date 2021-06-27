/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.cryptocurrency;

import at.adridi.financial.model.cryptocurrency.CryptoCurrencyTransactions;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface CryptoCurrencyTransactionsRepository extends JpaRepository<CryptoCurrencyTransactions, Long> {

    Optional<CryptoCurrencyTransactions> findByCryptocurrencytransactionId(Long cryptocurrencytransactionId);

    Optional<ArrayList<CryptoCurrencyTransactions>> findBySenderFromAndUserId(String currency, int userId);

    @Query(value = "SELECT * FROM Crypto_Currency_Transactions WHERE user_id=?1 AND deleted=false", nativeQuery = true)
    Optional<ArrayList<CryptoCurrencyTransactions>> getAllCryptoCurrencyTransactionsList(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Crypto_Currency_Transactions SET deleted=false WHERE cryptocurrencytransaction_id=?1", nativeQuery = true)
    void restoreDeletedCryptoCurrencyTransactions(int cryptocurrencyId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Crypto_Currency_Transactions SET sender_from=?1, currency_from=?2, destination_to=?3, currency_to=?4, amount=?5, storage_location=?6, notice=?7 WHERE cryptocurrencytransaction_id=?8 and user_id=?9", nativeQuery = true)
    void updateCryptoCurrencyTransactionsTableData(String senderFrom, String currencyFrom, String destinationTo, String currencyTo, float amount, String storageLocation, String notice, Long cryptocurrencytransactionId, int userId);

}

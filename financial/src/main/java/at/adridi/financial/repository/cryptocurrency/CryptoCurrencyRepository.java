/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.repository.cryptocurrency;

import at.adridi.financial.model.cryptocurrency.CryptoCurrency;
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
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

    Optional<CryptoCurrency> findByCryptocurrencyId(Long cryptocurrencyId);

    Optional<ArrayList<CryptoCurrency>> findByCurrencyAndUserId(String currency, int userId);

    @Query(value = "SELECT * FROM Crypto_Currency WHERE user_id=?1 AND deleted=false", nativeQuery = true)
    Optional<ArrayList<CryptoCurrency>> getAllCryptoCurrencyList(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Crypto_Currency SET deleted=false WHERE cryptocurrency_id=?1", nativeQuery = true)
    void restoreDeletedCryptoCurrency(int cryptocurrencyId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Crypto_Currency SET amount=?1, currency=?2, storage_location=?3, notice=?4 WHERE cryptocurrency_id=?5 and user_id=?6", nativeQuery = true)
    void updateCryptoCurrencyTableData(float amount, String currency, String storageLocation, String notice, Long cryptocurrencyId, int userId);

}

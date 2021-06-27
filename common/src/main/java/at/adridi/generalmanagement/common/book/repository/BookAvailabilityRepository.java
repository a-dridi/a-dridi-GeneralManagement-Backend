/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.repository;

import at.adridi.generalmanagement.common.book.model.BookAvailability;
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
public interface BookAvailabilityRepository extends JpaRepository<BookAvailability, Long> {

    Optional<BookAvailability> findByBookavailabilityId(Long bookAvailabilityId);

    Optional<BookAvailability> findByAvailabilityTitle(String availabilityTitle);

    @Query(value = "SELECT * FROM Book_Availability ORDER BY availability_title ASC", nativeQuery = true)
    Optional<ArrayList<BookAvailability>> getAllBookAvailabilityList();

}

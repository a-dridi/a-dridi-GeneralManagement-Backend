/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.service;

import at.adridi.generalmanagement.common.book.model.BookAvailability;
import at.adridi.generalmanagement.common.book.repository.BookAvailabilityRepository;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of BookAvailability DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class BookAvailabilityService {

    @Autowired
    private BookAvailabilityRepository bookAvailabilityRepository;

    /**
     * Save new book availability.
     *
     * @param newBookAvailability
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public BookAvailability save(BookAvailability newBookAvailability) {
        if (newBookAvailability == null) {
            return null;
        }
        return this.bookAvailabilityRepository.save(newBookAvailability);
    }

    /**
     * Get certain book availability with the passed id. Throws
     * DataValueNotFoundException if book availability is not available.
     *
     * @param id
     * @return
     */
    public BookAvailability getBookAvailabilityById(Long id) {
        return this.bookAvailabilityRepository.findByBookavailabilityId(id).orElseThrow(() -> new DataValueNotFoundException("Book Availability Does Not Exist"));
    }

    /**
     * Get certain book availability with the passed availability title. Throws
     * DataValueNotFoundException if book availability is not available.
     *
     * @param availabilityTitle
     * @return
     */
    public BookAvailability getBookAvailabilityByAvailabilityTitle(String availabilityTitle) {
        return this.bookAvailabilityRepository.findByAvailabilityTitle(availabilityTitle).orElseThrow(() -> new DataValueNotFoundException("Book Availability Does Not Exist"));
    }

    /**
     * Get a List of all saved book availabilities
     *
     * @return
     */
    public List<BookAvailability> getAllBookAvailability() {
        return this.bookAvailabilityRepository.getAllBookAvailabilityList().orElseThrow(() -> new DataValueNotFoundException("Book Availability List could not be loaded!"));
    }

    /**
     * Delete an existing book availability
     *
     * @param bookAvailabilityId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long bookAvailabilityId) {
        if (bookAvailabilityId == null || bookAvailabilityId == 0) {
            return false;
        }

        BookAvailability bookAvailability = this.getBookAvailabilityById(bookAvailabilityId);

        try {
            this.bookAvailabilityRepository.delete(bookAvailability);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing book availability
     *
     * @param availabilityTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String availabilityTitle) {
        if (availabilityTitle == null || availabilityTitle.trim().isEmpty()) {
            return false;
        }
        BookAvailability bookAvailability = this.getBookAvailabilityByAvailabilityTitle(availabilityTitle);

        try {
            this.bookAvailabilityRepository.delete(bookAvailability);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

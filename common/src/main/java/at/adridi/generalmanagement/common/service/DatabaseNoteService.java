/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.service;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.AppDatabaseNote;
import at.adridi.generalmanagement.common.repository.AppDatabaseNoteRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of expense DAO
 *
 * @author A.Dridi
 */
@Service
@Transactional
@NoArgsConstructor
public class DatabaseNoteService {

    @Autowired
    private AppDatabaseNoteRepository databaseNoteRepository;

    /**
     * Save new database note. Or update database note if it's already existing.
     *
     * @param newDatabaseNote
     * @return 0 if successful. 1: Passed object is null. 2: Saving failed.
     */
    public Integer saveOrUpdate(AppDatabaseNote newDatabaseNote) {
        if (newDatabaseNote == null) {
            return 1;
        }

        try {
            AppDatabaseNote updatedDatabaseNote = this.getDatabaseNoteByTable(newDatabaseNote.getAppTable(), newDatabaseNote.getUserId());
            updatedDatabaseNote.setNoteText(newDatabaseNote.getNoteText());
            this.databaseNoteRepository.save(updatedDatabaseNote);
            return 0;
        } catch (DataValueNotFoundException e) {
            //Create new database note
            AppDatabaseNote savedObject = this.databaseNoteRepository.save(newDatabaseNote);
            if (savedObject != null) {
                return 0;
            } else {
                return 2;
            }
        }

    }

    /**
     * Get certain database note with the passed table name and user id. Throws
     * DataValueNotFoundException if expense is not available.
     *
     * @param tablename
     * @return
     */
    @Transactional(readOnly = true)
    public AppDatabaseNote getDatabaseNoteByTable(String tablename, Integer userId) {
        return this.databaseNoteRepository.findByAppTableAndUserId(tablename, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Datbase Note with the table name " + tablename + " Does Not Exist"));
    }

    /**
     * Get certain database note with the passed id. Throws
     * DataValueNotFoundException if expense is not available.
     *
     * @param tablename
     * @return
     */
    @Transactional(readOnly = true)
    public AppDatabaseNote getDatabaseNoteById(Long id) {
        return this.databaseNoteRepository.findById(id)
                .orElseThrow(() -> new DataValueNotFoundException("Datbase Note with the id " + id + " Does Not Exist"));
    }

    /**
     * Get a List of all saved expenses of a user
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<AppDatabaseNote> getAllDatabaseNote(Integer userId) {
        return this.databaseNoteRepository.findByUserId(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have notes or does not exist"));
    }

    /**
     * Delete an existing database note
     *
     * @param databaseNoteId
     * @return true if successful
     */
    public boolean deleteById(Long databaseNoteId) {
        if (databaseNoteId == null || databaseNoteId == 0) {
            return false;
        }

        AppDatabaseNote databaseNote = this.getDatabaseNoteById(databaseNoteId);
        if (databaseNote != null) {
            this.databaseNoteRepository.delete(databaseNote);
            return true;
        } else {
            return false;
        }
    }

}

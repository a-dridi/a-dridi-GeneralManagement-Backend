/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.AppDatabaseNote;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.service.DatabaseNoteService;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Endpoints for note data of every table (expenses, earnings, etc.)
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class DatabaseNoteController {

    @Autowired
    private DatabaseNoteService databaseNoteService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DATABASENOTE + "/all/{userId}")
    public ResponseEntity<List<AppDatabaseNote>> getAllDatabaseNote(@PathVariable int userId) {
        List<AppDatabaseNote> databaseNoteList = this.databaseNoteService.getAllDatabaseNote(userId);
        if (!CollectionUtils.isEmpty(databaseNoteList)) {
            return status(HttpStatus.OK).body(databaseNoteList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<AppDatabaseNote>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DATABASENOTE + "/get/byId/{id}")
    public ResponseEntity<AppDatabaseNote> getDatabaseNoteById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.databaseNoteService.getDatabaseNoteById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new AppDatabaseNote());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DATABASENOTE + "/get/byTable/byUserId/{table}/{userId}")
    public ResponseEntity<AppDatabaseNote> getDatabaseNoteByTable(@PathVariable String table, @PathVariable int userId) {
        try {
            return status(HttpStatus.OK).body(this.databaseNoteService.getDatabaseNoteByTable(table, userId));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new AppDatabaseNote());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DATABASENOTE + "/add")
    public ResponseEntity<ResponseMessage> addDatabaseNote(@RequestBody String newDatabaseNoteJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        AppDatabaseNote newDatabaseNote;
        int resultCode;
        try {
            newDatabaseNote = objectMapper.readValue(newDatabaseNoteJson, AppDatabaseNote.class);
            resultCode = this.databaseNoteService.saveOrUpdate(newDatabaseNote);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseNoteController.class.getName()).log(Level.SEVERE, null, ex);
            resultCode = 4;
        }

        switch (resultCode) {
            case 0:
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Database Note was added successfully."));
            case 1:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Database Note cannot be null!"));
            case 2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Database Note could not be saved!"));
            case 4:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. The JSON object string could not be processed!"));
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Database Note could not be saved!"));
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DATABASENOTE + "/update")
    public ResponseEntity<ResponseMessage> updateDatabaseNote(@RequestBody String updatedDatabaseNoteJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        AppDatabaseNote updatedDatabaseNote;
        try {
            updatedDatabaseNote = objectMapper.readValue(updatedDatabaseNoteJson, AppDatabaseNote.class);
            int resultCode = this.databaseNoteService.saveOrUpdate(updatedDatabaseNote);
            switch (resultCode) {
                case 0:
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Database Note update was successful."));
                case 1:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Database Note cannot be null!"));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Update failed!"));
            }
        } catch (IOException ex) {
            Logger.getLogger(DatabaseNoteController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Please check the passed JSON object!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_DATABASENOTE + "/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteDatabaseNote(@PathVariable Long databaseNoteId) {
        if (this.databaseNoteService.deleteById(databaseNoteId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your Database Note was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Database Note does not exists!"));
        }
    }
}

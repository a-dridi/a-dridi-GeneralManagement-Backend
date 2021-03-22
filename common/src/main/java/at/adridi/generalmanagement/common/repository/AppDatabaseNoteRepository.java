/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.repository;

import at.adridi.generalmanagement.model.AppDatabaseNote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author A.Dridi
 */
public interface AppDatabaseNoteRepository extends JpaRepository<AppDatabaseNote, Long> {

    Optional<AppDatabaseNote> findById(Integer id);
    Optional<List<AppDatabaseNote>> findByUserId(Integer userId);
    Optional<AppDatabaseNote> findByTableAndUserId(String table, Integer userId);

}

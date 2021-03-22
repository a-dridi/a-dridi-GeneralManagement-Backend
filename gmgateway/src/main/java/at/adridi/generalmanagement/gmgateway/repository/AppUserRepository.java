/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.repository;

import at.adridi.generalmanagement.gmgateway.model.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Users. Used for authentication.
 * 
 * @author A.Dridi
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    
    Optional<AppUser> findByUserId(Long userId);
    Optional<AppUser> findByEmail(String email);
    
}

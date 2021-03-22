/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.service;

import at.adridi.generalmanagement.gmgateway.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.gmgateway.model.AppUser;
import at.adridi.generalmanagement.gmgateway.repository.AppUserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Implementation of DAO to manage user accounts. Registration, delete Users,
 * etc.
 *
 * @author A.Dridi
 */
@Service
@Transactional
@AllArgsConstructor
@NoArgsConstructor
public class UserService {

    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Save new AppUser.
     *
     * @param newUser
     * @return AppUser. null if failed
     */
    public AppUser save(AppUser newUser) {
        if (newUser == null) {
            return null;
        }
        return this.userRepository.save(newUser);
    }

    /**
     * Get certain User with the passed user id. Throws
     * DataValueNotFoundException if User is not available.
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AppUser getUserByUserId(long userId) {
        return this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new DataValueNotFoundException("User Does Not Exist"));
    }

    /**
     * Get certain User with the passed email. Throws DataValueNotFoundException
     * if User is not available.
     *
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public AppUser getUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new DataValueNotFoundException("User Does Not Exist"));
    }

    /**
     * Get a List of all AppUser
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AppUser> getAllUser() {
        return this.userRepository.findAll();
    }

    /**
     * Delete an existing user by user id
     *
     * @param userId
     * @return true if successful
     */
    public boolean deleteById(Long userId) {
        if (userId == null || userId == 0) {
            return false;
        }
        AppUser user = this.getUserByUserId(userId);
        if (user != null) {
            this.userRepository.delete(user);
            try {
                this.getUserByUserId(user.getUserId());
                return true;
            } catch (DataValueNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean deleteByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        AppUser user = this.getUserByEmail(email);
        if (user != null) {
            this.userRepository.delete(user);
            try {
                this.getUserByEmail(user.getEmail());
                return true;
            } catch (DataValueNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }

}

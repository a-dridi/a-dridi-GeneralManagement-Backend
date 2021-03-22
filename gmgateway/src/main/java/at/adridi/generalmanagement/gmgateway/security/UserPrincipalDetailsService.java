/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.security;

import at.adridi.generalmanagement.gmgateway.model.AppUser;
import at.adridi.generalmanagement.gmgateway.repository.AppUserRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * Load existing user from database.
 *
 * @author A.Dridi
 */
@Service
@AllArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    /**
     * Load user by email. The username is the used email of the respective user
     * account.
     *
     * @param s
     * @return
     * @throws UsernameNotFoundException If user does not exist, then this
     * exception will be trhown
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> user = this.userRepository.findByEmail(email);
        UserPrincipal userPrincipal = new UserPrincipal(user.orElseThrow(() -> new UsernameNotFoundException("User with this email does not exist!")));
        return userPrincipal;
    }

}

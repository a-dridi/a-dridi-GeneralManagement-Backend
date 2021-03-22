/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Save the user that is currently logged in. Needed for JWT authentication.
 * 
 * @author A.Dridi
 */
@Data
@Getter
@Setter
public class LoginUser {
    
    private String email;
    private String password;
}

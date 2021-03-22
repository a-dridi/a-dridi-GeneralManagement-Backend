/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author A.Dridi
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "useridgenerator")
    @TableGenerator(name = "useridgenerator", initialValue = 150, allocationSize = 2000, table = "sequence_user_id")
    private Long userId;
    @Column(unique=true)
    private String email;
    private String username;
    private String password;
    private boolean isAdmin;
    private String roles;
    private String permissions;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList() {
        if (this.permissions.length() > 0) {
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}

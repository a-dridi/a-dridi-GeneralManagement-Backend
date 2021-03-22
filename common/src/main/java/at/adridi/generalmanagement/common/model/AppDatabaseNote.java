/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * Save notes for expenses, books, earnings, etc. (major tables).
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class AppDatabaseNote {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "databasenoteidgenerator")
    @TableGenerator(name = "databasenoteidgenerator", initialValue = 1000, allocationSize = 2000, table = "sequence_databasenoteid")
    private Long id;
    @Column(unique = true)
    private String table;
    private String noteText;

    //Auto. vom Programm zu gewiesen
    private Date date = new Date();

    private Integer userId;
}

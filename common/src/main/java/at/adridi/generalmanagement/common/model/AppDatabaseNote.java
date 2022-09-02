/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"app_table", "user_id"})})
public class AppDatabaseNote {

    @Id
    @SequenceGenerator(name = "pk_appdatabase_sequence", sequenceName = "appdatabase_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_appdatabase_sequence")
    private Long id;
    @Column(name = "app_table") 
    private String appTable;
    private String noteText;

    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date;

    @Column(name = "user_id")
    private Integer userId;
}

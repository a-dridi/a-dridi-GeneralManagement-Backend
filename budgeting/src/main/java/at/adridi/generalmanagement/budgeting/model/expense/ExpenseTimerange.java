/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.expense;

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
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class ExpenseTimerange {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "timerangeidgenerator")
    @TableGenerator(name = "timerangeidgenerator", initialValue = 1, allocationSize = 2000, table = "sequence_timerangeidgenerator")
    private Long timerangeId;

    @Column(unique = true)
    private String timerangeTitle;

}

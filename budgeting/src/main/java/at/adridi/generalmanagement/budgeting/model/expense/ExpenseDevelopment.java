/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.expense;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Display the expenses sum for certain months and years
 *
 * @author A.Dridi
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class ExpenseDevelopment implements Serializable {

    @Id
    @SequenceGenerator(name="pk_expensebudget_sequence", sequenceName="expensebudget_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk_expensebudget_sequence")
    private Long expenseDevelopmentId;

    private Integer month;
    private Integer year;
    private String dateDisplay;
    private Integer centSum;

    private int userId;

    public ExpenseDevelopment(Integer month, Integer year, String dateDisplay, Integer centSum, int userId) {
        this.month = month;
        this.year = year;
        this.dateDisplay = dateDisplay;
        this.centSum = centSum;
        this.userId = userId;
    }
 
}

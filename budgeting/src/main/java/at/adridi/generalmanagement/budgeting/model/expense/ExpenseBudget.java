/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.expense;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class ExpenseBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "expensebudgetidgenerator")
    @TableGenerator(name = "expensebudgetidgenerator", initialValue = 1, allocationSize = 2000, table = "sequence_expensebudget_id")
    private Long expensesbudgetId;

    @ManyToOne(cascade = CascadeType.ALL)
    private ExpenseCategory expenseCategory;
    private Integer centBudgetValue = 0;
    private Integer centActualExpenses;
    private Integer centDifference = 0;
    //Signs for Difference: + Surplus | - Loss
    private String s = "0";
    private String notice;
    private Integer userId;

}

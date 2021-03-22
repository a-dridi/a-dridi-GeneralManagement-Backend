/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created non database model to display graphs for expenses
 * 
 * @author A.Dridi
 */
@Data
@Getter
@Setter
@AllArgsConstructor
public class ExpenseGraph {
    private String categoryTitle;
    private Integer centValue;
}

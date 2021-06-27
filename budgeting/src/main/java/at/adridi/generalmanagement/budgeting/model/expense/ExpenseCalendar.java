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
 * Model class for tab Expense Calendar to display expenses and payment date in
 * a calendar view.
 *
 * @author A.Dridi
 */
@Getter
@Setter
@Data
@AllArgsConstructor
public class ExpenseCalendar {

    private int id;
    //starts with character to show the timerange (monthly, yearly, etc.) of this expense
    private String title;
    
    private String start;
    
    //Colors adjusted to the timerange of expense
    private String backgroundColor;
    private String textColor;

}

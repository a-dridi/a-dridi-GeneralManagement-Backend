/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.sql.Date;
import java.util.concurrent.ScheduledFuture;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Saves the reminders of expenses. This manage when expense has to be paid and was paid.
 * The date it was paid is updated.
 *
 * @author A.Dridi
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name="expense_reminder")
public class ExpenseReminder implements Serializable {
    @Id
    @SequenceGenerator(name = "pk_expensereminder_sequence", sequenceName = "expensereminder_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_expensereminder_sequence")
    private Long expensereminderId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "expense_expense_id", referencedColumnName="expense_id")
    private Expense expense;

    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date dueDate;
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date payedDate;
    
    public ExpenseReminder(Expense expense, Date dueDate, Date payedDate) {
        this.expense = expense;
        this.dueDate = dueDate;
        this.payedDate = payedDate;
    }
        
}

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
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class ExpenseTimerange implements Serializable {

    /**
     * The following Expense Timerange IDs are used: 1 for "one time" expense,
     * 2->daily, 3-> weekly, 4-> biweekly, 5-> monthly, 6->every 2 months,
     * 7->every quartal, 8-> every 6 months, 9->yearly, 10->every 2 years,
     * 11->every 5 years, 12->custom.
     */
    @Id
    @SequenceGenerator(name = "pk_expensetimerange_sequence", sequenceName = "expensetimerange_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_expensetimerange_sequence")
    private Long timerangeId;

    @Column(unique = true)
    private String timerangeTitle;

}

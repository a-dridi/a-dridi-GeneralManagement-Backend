/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Expense implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_expense_sequence", sequenceName = "expense_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_expense_sequence")
    @Column(name = "expense_id")
    private Long expenseId;
    private String title;
    @ManyToOne(cascade = CascadeType.MERGE)
    private ExpenseCategory expenseCategory;
    private Integer centValue;
    @ManyToOne(cascade = CascadeType.MERGE)
    private ExpenseTimerange expenseTimerange;
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private Date paymentDate;
    @Column(length = 10000)
    private String information;

    @Column(columnDefinition = "boolean default false")
    @JsonProperty(value = "isReminding")
    private boolean isReminding = false;

    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    private boolean deleted;
    private Integer userId;

}

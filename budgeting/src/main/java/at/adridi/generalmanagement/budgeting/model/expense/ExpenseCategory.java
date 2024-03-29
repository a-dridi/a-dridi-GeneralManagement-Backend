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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"category_title", "user_id"})})
public class ExpenseCategory implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_expensecategory_sequence", sequenceName = "expensecategory_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_expensecategory_sequence")
    private Long expenseCategoryId;

    @Column(name = "category_title")
    private String categoryTitle;

    @Column(name = "user_id")
    private Integer userId;

}

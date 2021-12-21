/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.model.wealth;

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
 * Yearly wealth containing the difference of expeneses and earnings of a year
 * calculated from Wealth Monthly.
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"year_date", "user_id"}))
public class WealthYearly implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_wealthyearly_sequence", sequenceName = "wealthyearly_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_wealthyearly_sequence")
    private Long wealthyearlyId;

    @Column(name="year_date")
    private int yearDate;

    private int expenseCent;
    private int earningCent;
    private int differenceCent;

    //Calculate the difference in percentage of wealth difference compared to the previous month
    private double improvementPct;

    //If user did edit the certain wealth yearly row(item), then the expenses/earnings of this row will not be updated automatically.
    private boolean manual;

    @Column(length = 10000)
    private String notice;
    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    @Column(name="user_id")
    private Integer userId;
}

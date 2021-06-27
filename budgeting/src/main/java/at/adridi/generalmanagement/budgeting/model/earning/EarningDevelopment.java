/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.model.earning;

import java.io.Serializable;
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
 * Display the earning sum for certain months and years
 *
 * @author A.Dridi
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class EarningDevelopment implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_earningdevelopment_sequence", sequenceName = "earningdevelopment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_earningdevelopment_sequence")
    private Long earningDevelopmentId;

    private Integer month;
    private Integer year;
    private String dateDisplay;
    private Integer centSum;

    private int userId;

    public EarningDevelopment(Integer month, Integer year, String dateDisplay, Integer centSum, int userId) {
        this.month = month;
        this.year = year;
        this.dateDisplay = dateDisplay;
        this.centSum = centSum;
        this.userId = userId;
    }

}

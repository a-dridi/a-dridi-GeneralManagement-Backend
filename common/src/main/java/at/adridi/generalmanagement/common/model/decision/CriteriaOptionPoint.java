/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.model.decision;

import javax.persistence.CascadeType;
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
 * Save the points and total the an option got in a criteria of a decision.
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class CriteriaOptionPoint {

    @Id
    @SequenceGenerator(name = "pk_optionpoint_sequence", sequenceName = "optionpoint_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_optionpoint_sequence")
    private Long optionpointId;
    private int decisionOptionId;
    private int criteriaId;
    private int decisionId;
    private int points;
    private int total;
    private Integer userId;

}

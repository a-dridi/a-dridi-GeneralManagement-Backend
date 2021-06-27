/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.model.decision;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Save the criterias of a decision. Contains also the option points of a
 * criteria.
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class CriteriaOption implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_criteriaoption_sequence", sequenceName = "criteriaoption_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_criteriaoption_sequence")
    private Long criteriaoptionId;

    private String criteriaTitle;
    private int criteriaWeighting;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Decision decision;
    private Integer userId;
}

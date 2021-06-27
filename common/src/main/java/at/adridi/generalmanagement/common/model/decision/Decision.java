/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.model.decision;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 *
 * Save a decision (matrix) containing the decision options that are compared in
 * this decision matrix. The chosen option is saved as well.
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class Decision implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_decision_sequence", sequenceName = "decision_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_decision_sequence")
    private Long decisionId;
    
    private String title;
    
    private String chosenOption;
    private Long chosenOptionId;
    @Column(length = 10000)
    private String information;
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date addedDate = new Date();

    private Integer userId;
}

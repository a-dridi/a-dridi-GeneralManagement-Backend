/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.model.savings;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
/**
 * Savings accounts. A saving account shows information about for example: the
 * savings target and the date when the savings target will be reached.
 */
public class Savings implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_savings_sequence", sequenceName = "savings_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_savings_sequence")
    private Long savingsId;

    private String description;
    //The target value for a savings account
    private Integer targetCent;
    //The amount that will be saved regularly to reach the target
    private Integer stepAmountCent;
    //1 -> custom 2-> daily 3-> monthly 4-> yearly 
    private Integer savingsFrequency;
    private Integer savedTillNowCent;
    //The date where a new amount was added to the savings account
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date lastSavingsUpdateDate;
    
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate = new Date();
    
    
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //The date where the saving target will we reached/was reached
    private Date targetCalculatedDate = new Date();
    
    @Column(length = 10000)
    private String notice;
    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    private boolean deleted;
    private Integer userId;
}

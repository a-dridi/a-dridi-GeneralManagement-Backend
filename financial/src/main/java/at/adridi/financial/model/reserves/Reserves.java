/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.model.reserves;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
 * Reserves of money, financial and other non-financial items.
 */
public class Reserves implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_reserves_sequence", sequenceName = "reserves_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_reserves_sequence")
    private Long reservesId;
    @ManyToOne(cascade = CascadeType.MERGE)
    private ReservesCategory category;
    private String description;
    private Float amount;
    private String currency;
    private String storageLocation;
    @Column(length = 20000)
    private String notice;

    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdDate = new Date();

    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    private boolean deleted;
    private Integer userId;
}

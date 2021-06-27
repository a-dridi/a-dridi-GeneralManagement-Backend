/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.model.software;

import java.io.Serializable;
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
public class Software implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_software_sequence", sequenceName = "software_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_software_sequence")
    private Long softwareId;

    private String title;
    @ManyToOne(cascade = CascadeType.MERGE)
    private SoftwareOs softwareOs;
    private String manufacturer;
    private String language;
    private String version;
    private String notice;
    private String linkValue;
    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    private boolean deleted;
    private Integer userId;

}

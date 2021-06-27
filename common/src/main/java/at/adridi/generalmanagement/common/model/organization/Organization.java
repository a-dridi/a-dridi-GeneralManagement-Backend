/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.model.organization;

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
 * Save information where a certain physical object (document, device, cable,
 * tool, etc.) is located in a place. Also digital pbjects.
 *
 * @author A.Dridi
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
public class Organization implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_organization_sequence", sequenceName = "organization_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_organization_sequence")
    private Long organizationId;

    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    private OrganizationCategory organizationCategory;
    private String location;
    private String status;
    @Column(length = 10000)
    private String information;
    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    private boolean deleted;
    private Integer userId;

}

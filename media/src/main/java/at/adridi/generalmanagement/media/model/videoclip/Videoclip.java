/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.model.videoclip;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
public class Videoclip implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_videoclip_sequence", sequenceName = "videoclip_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_videoclip_sequence")
    private Long videoclipId;

    private String interpreter;
    private String videoTitle;
    @ManyToOne(cascade = CascadeType.MERGE)
    private VideoclipLanguage videoclipLanguage;
    private Integer yearDate;
    private String nativeTitle;
    private String linkValue;
    private boolean deleted;
    private Integer userId;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.model.video;

import java.io.Serializable;
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
public class VideoLanguage implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_videolanguage_sequence", sequenceName = "videolanguage_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_videolanguage_sequence")
    private Long videolanguageId;

    @Column(unique = true)
    private String languageTitle;

}

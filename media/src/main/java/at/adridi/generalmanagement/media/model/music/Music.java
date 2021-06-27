/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.model.music;

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
public class Music implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_music_sequence", sequenceName = "music_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_music_sequence")
    private Long musicId;

    private String interpreter;
    private String songtitle;
    private Integer yearDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    private MusicGenre musicGenre;
    private String codeValue;
    private String linkValue;

    @Column(length = 10000)
    private String notice;
    private boolean deleted;
    private Integer userId;

}

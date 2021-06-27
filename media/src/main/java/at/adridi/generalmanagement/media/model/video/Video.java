/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.model.video;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Video implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_video_sequence", sequenceName = "video_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_video_sequence")
    private Long videoId;

    private String title;
    @JsonProperty(value = "isOwnProduction")
    private boolean isOwnProduction;
    @ManyToOne(cascade = CascadeType.MERGE)
    private VideoLanguage videoLanguage;
    @JsonProperty(value = "isHd")
    private boolean isHd;
    @ManyToOne(cascade = CascadeType.MERGE)
    private VideoGenre videoGenre;
    private Integer durationLength;
    private Integer yearDate;
    @JsonProperty(value = "isSeries")
    private boolean isSeries;
    private String nativeTitle;
    private String linkValue;
    private boolean deleted;
    private Integer userId;
}

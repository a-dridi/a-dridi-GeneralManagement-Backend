/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.model.video;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/**
 * Display the amount for a certain video genre
 *
 * @author A.Dridi
 */
@Value
@Getter
@Setter
public class VideoGraph {

    String title;
    Long amount;

    public VideoGraph(String title, Long amount) {
        this.title = title;
        this.amount = amount;
    }

    public VideoGraph() {
        this.title = "";
        this.amount = 0L;
    }

}

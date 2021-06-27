/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.videoclip;

import at.adridi.generalmanagement.media.model.video.VideoGraph;
import at.adridi.generalmanagement.media.repository.videoclip.VideoclipRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create data list for the video graph. Video clips.
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class VideoclipGraphService {

    @Autowired
    private VideoclipRepository videoclipRepository;

    /**
     * Get a list containing video clip language and their respective amounts.
     *
     * @return
     */
    public List<VideoGraph> getAllVideoclipLanguageAmountList(int userId) {
        return this.videoclipRepository.getVideoclipListGroupedByLanguage(userId);
    }
}

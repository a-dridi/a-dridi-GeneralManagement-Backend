/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.service.video;

import at.adridi.generalmanagement.media.model.video.VideoGraph;
import at.adridi.generalmanagement.media.repository.video.VideoRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create data list for the video graph. Videos.
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class VideoGraphService {

    @Autowired
    private VideoRepository videoRepository;

    /**
     * Get a list containing video genre and their respective amounts.
     *
     * @return
     */
    public List<VideoGraph> getAllVideoGenreAmountList(int userId) {
        return this.videoRepository.getVideoListGroupedByGenre(userId);
    }

    /**
     * Get a list containing video language and their respective amounts.
     *
     * @return
     */
    public List<VideoGraph> getAllVideoLanguageAmountList(int userId) {
        return this.videoRepository.getVideoListGroupedByLanguage(userId);
    }
}

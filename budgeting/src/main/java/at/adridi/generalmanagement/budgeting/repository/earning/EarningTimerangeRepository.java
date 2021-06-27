/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.earning;

import at.adridi.generalmanagement.budgeting.model.earning.EarningTimerange;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface EarningTimerangeRepository extends JpaRepository<EarningTimerange, Long> {

    Optional<EarningTimerange> findByTimerangeId(Long timerangeId);
    Optional<EarningTimerange> findByTimerangeTitle(String timerangeTitle);
    
    @Query(value = "SELECT * FROM Earning_Timerange ORDER BY timerange_id ASC", nativeQuery = true)
    Optional<ArrayList<EarningTimerange>> getAllEarningTimerangeList();

}

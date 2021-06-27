/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.media.repository.software;

import at.adridi.generalmanagement.media.model.software.SoftwareOs;
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
public interface SoftwareOsRepository extends JpaRepository<SoftwareOs, Long> {

    Optional<SoftwareOs> findBySoftwareosId(Long softwareosId);

    Optional<SoftwareOs> findByOsTitle(String osTitle);

    @Query(value = "SELECT * FROM Software_Os ORDER BY os_title ASC", nativeQuery = true)
    Optional<ArrayList<SoftwareOs>> getAllSoftwareOsList();

}

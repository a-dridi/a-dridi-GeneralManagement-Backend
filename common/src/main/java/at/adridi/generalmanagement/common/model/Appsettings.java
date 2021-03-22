/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
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
public class Appsettings {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "appsettingsidgenerator")
    @TableGenerator(name = "appsettingsidgenerator", initialValue = 1, allocationSize = 2000, table = "sequence_appssetings_id")
    private Long appssetingsId;

    /*Ausgabenaufteilung */
    //Name von Person 1
    @Column(name = "person1name")
    private String person1name;
    //Aufteilungschlussel in Prozent
    private Integer person1split;

    //Name von Person 2
    @Column(name = "person2name")
    private String person2name;
    private Integer person2split;
    
    private Integer userId;

}

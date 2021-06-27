/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



/**
 *
 * Class used to create text response for API requests
 * 
 * @author A.Dridi
 */
@Getter
@Setter
@Data
@AllArgsConstructor
public class ResponseMessage {
    private String message;
}

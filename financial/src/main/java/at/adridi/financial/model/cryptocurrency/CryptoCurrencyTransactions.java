/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.model.cryptocurrency;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Basic;
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
public class CryptoCurrencyTransactions {

    @Id
    @SequenceGenerator(name = "pk_cryptocurrencytransaction_sequence", sequenceName = "cryptocurrencytransaction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_cryptocurrencytransaction_sequence")
    private Long cryptocurrencytransactionId;
    private String senderFrom;
    private String currencyFrom;
    private String destinationTo;
    private String currencyTo;

    private Float amount;

    private String storageLocation;
    @Column(length = 20000)
    private String notice;

    private boolean attachment = false;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentType;
    private boolean deleted;
    private Integer userId;

}

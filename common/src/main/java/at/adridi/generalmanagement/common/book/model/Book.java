/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
public class Book {

    @Id
    @SequenceGenerator(name = "pk_book_sequence", sequenceName = "book_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_book_sequence")
    private Long bookId;
    private String title;
    @ManyToOne(cascade = CascadeType.MERGE)
    private BookCategory bookCategory;
    private String location;
    @ManyToOne(cascade = CascadeType.MERGE)
    private BookAvailability bookAvailability;
    private String bookLanguage;
    private String yearDate;
    private String isbn;
    @Column(length = 10000)
    private String information;
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date addedDate = new Date();

    private boolean deleted;
    private Integer userId;

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/**
 *
 * Display the number of certain types of books. Etc.: Amount of books by
 * language, category...
 *
 * @author A.Dridi
 */
@Value
@Getter
@Setter
public class BookGraph implements Serializable {

    String title;

    Long amount;

    public BookGraph(String title, Long amount) {
        this.title = title;
        this.amount = amount;
    }

    public BookGraph() {
        this.title="";
        this.amount=0L;
    }

}

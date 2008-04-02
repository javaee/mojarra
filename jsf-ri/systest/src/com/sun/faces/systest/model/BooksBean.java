/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BooksBean {


    private List<BookBean> books;

    // ------------------------------------------------------------ Constructors


    public BooksBean() {

        books = new ArrayList<BookBean>();
        books.add(new BookBean("Harry Potter and the Sorcerer's Stone",
                               "J.K. Rowling",
                               "10009001",
                               12.99));
        books.add(new BookBean("Dune",
                               "Frank Herbert",
                               "98111012",
                               15.99));
        books.add(new BookBean("The Hitchhiker's Guide to the Galaxy",
                               "Douglas Adams",
                               "11001199",
                               13.99));

    }

    // ---------------------------------------------------------- Public Methods


    public List<BookBean> getBooks() {

        return books;

    }


    public double getTotalCost() {

        double cost = 0.0;
        for (Iterator<BookBean> i = books.iterator(); i.hasNext();) {
            BookBean book = i.next();
            cost += (book.getQuantity() * book.getPrice());
        }

        return cost;

    }

}

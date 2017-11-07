/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.test.javaee6web.facelets;

import java.io.Serializable;
import java.util.*;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class BasicViewForIssue4142 implements Serializable {

  private static final long serialVersionUID = -1304416932952283709L;

  private final List<Car> cars;
  private transient HtmlDataTable carsDataTable;

  private final static String[] colors;

  private final static String[] brands;

  static {
    colors = new String[10];
    colors[0] = "Black";
    colors[1] = "White";
    colors[2] = "Green";
    colors[3] = "Red";
    colors[4] = "Blue";
    colors[5] = "Orange";
    colors[6] = "Silver";
    colors[7] = "Yellow";
    colors[8] = "Brown";
    colors[9] = "Maroon";

    brands = new String[10];
    brands[0] = "BMW";
    brands[1] = "Mercedes";
    brands[2] = "Volvo";
    brands[3] = "Audi";
    brands[4] = "Renault";
    brands[5] = "Fiat";
    brands[6] = "Volkswagen";
    brands[7] = "Honda";
    brands[8] = "Jaguar";
    brands[9] = "Ford";
  }

  public BasicViewForIssue4142() {
    // System.out.println("new basicview");
    cars = createCars(10);
  }

  private List<Car> createCars(int size) {
    List<Car> list = new ArrayList<Car>();
    for (int i = 0; i < size; i++) {
      list.add(new Car(getRandomId(), getRandomBrand(), getRandomYear(), getRandomColor(), getRandomPrice(),
          getRandomSoldState()));
    }
    return list;
  }

  private String getRandomId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  private int getRandomYear() {
    return (int) (Math.random() * 50 + 1960);
  }

  private String getRandomColor() {
    return colors[(int) (Math.random() * 10)];
  }

  private String getRandomBrand() {
    return brands[(int) (Math.random() * 10)];
  }

  public int getRandomPrice() {
    return (int) (Math.random() * 100000);
  }

  public boolean getRandomSoldState() {
    return (Math.random() > 0.5) ? true : false;
  }

  public List<String> getColors() {
    return Arrays.asList(colors);
  }

  public List<String> getBrands() {
    return Arrays.asList(brands);
  }

  public List<Car> getCars() {
    return cars;
  }

  public HtmlDataTable getCarsDataTable() {
    return carsDataTable;
  }

  public void setCarsDataTable(HtmlDataTable carsDataTable) {
    this.carsDataTable = carsDataTable;
  }

}

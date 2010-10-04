/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.faces.component.UIForm;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * This bean has methods to retrieve stock information from
 * the Yahoo quote service.
 */ 
public class Bean {

    private static final String SERVICE_URL =
            "http://quote.yahoo.com/d/quotes.csv";

    /**
     * Action method that is used to retrieve stock information.
     * This method uses two helper methods - one to get the
     * stock information, and the other to dynamically build
     * the "data" components for the UI.
     */
    public void getStockInfo(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("myform");
        UISelectOne select = (UISelectOne)form.findComponent("connection");
        String connection = (String)select.getValue();
        if (form != null) {
            if (connection.equals("Remote")) {
                setProxyIfNeeded(form);
            }
            UIInput component = (UIInput)form.findComponent("symbol");
            if (component != null) {
                String symbolInput = (String)component.getValue();
                if (symbolInput != null) {
                    String[] symbols = symbolInput.split("\\s");
                    String[] stockData = null;
                    if (connection.equals("Local")) {
                        stockData = getLocalStockData(symbols);
                        buildUI(stockData);
                        return;
                    }
                    try {
                        stockData = getStockData(symbols);
                        buildUI(stockData);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * Helper method to get the stock data (remotely).
     */
    private String[] getStockData(String[] symbols)
        throws IOException, MalformedURLException {
        String[] data = new String[symbols.length];
        for (int i=0; i<symbols.length; i++) {
            StringBuffer sb = new StringBuffer(SERVICE_URL);
            sb.append("?s=");
            sb.append(symbols[i]);
            sb.append("&f=snol1cp2v=.csv");
            String url = sb.toString();
            URLConnection urlConn = null;
            InputStreamReader inputReader = null;
            BufferedReader buff = null;
            try {
                urlConn = new URL(url).openConnection();
                inputReader = new InputStreamReader(
                    urlConn.getInputStream());
                buff = new BufferedReader(inputReader);
                data[i] = buff.readLine();
                data[i] = data[i].replace( "\"", "" );
            } catch (MalformedURLException e){
            } catch (IOException ioe) {
            } finally {
                if (inputReader != null) {
                    try {
                        inputReader.close();
                        buff.close();
                    } catch (Exception e) {}
                }
            }
        }
        return data;
    }

    private String[] getLocalStockData(String[] symbols) {
        String[] data = new String[symbols.length];
        Random generator1 = new Random(1459678L);
        Random generator2 = new Random(System.currentTimeMillis());
        for (int i=0; i<symbols.length; i++) {
            // generate an open price between 1 and 100
            double openPrice = round(generator1.nextDouble() * 100, 2);
            // generate a last price such that:
            // lastPrice is between 1 and (openPrice + 20)
            double low = 1;
            double high = openPrice + 20;
            double lastPrice = generator2.nextDouble() * (high - low) + low;
            lastPrice = round(lastPrice, 2);
            // change
            double change = round(lastPrice - openPrice, 2);
            // calculate percent change
            double percentChange = 0.00;
            if (openPrice != lastPrice) {
                percentChange = 100 * ((lastPrice - openPrice)/openPrice);
                percentChange = round(percentChange, 2);
            }
            // generate volume between 10000 and 100000
            int volume = generator2.nextInt(90001) + 10000;
            // now build the string to pass to buildUI routine
            data[i] = symbols[i].toUpperCase()+","+
                symbols[i].toUpperCase() + "," +
                new Double(openPrice).toString()+","+
                new Double(lastPrice).toString()+","+
                new Double(change).toString()+","+
                new Double(percentChange).toString()+"%,"+
                new Integer(volume).toString();
        }
        return data;
    }

    /**
     * Helper method to dynamically add JSF components to display
     * the data.
     */
    private void buildUI(String[] stockData) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm)context.getViewRoot().findComponent("myform");
        UIPanel dataPanel = (UIPanel)form.findComponent("stockdata");
        dataPanel.getChildren().clear();
        UIPanel titlePanel1 = new UIPanel();
        UIOutput output = new UIOutput();
        output.setValue("Symbol");
        titlePanel1.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel1);
        UIPanel titlePanel2 = new UIPanel();
        output = new UIOutput();
        output.setValue("Name");
        titlePanel2.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel2);
        UIPanel titlePanel3 = new UIPanel();
        output = new UIOutput();
        output.setValue("Open");
        titlePanel3.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel3);
        UIPanel titlePanel4 = new UIPanel();
        output = new UIOutput();
        output.setValue("Last");
        titlePanel4.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel4);
        UIPanel titlePanel5 = new UIPanel();
        output = new UIOutput();
        output.setValue("");
        titlePanel5.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel5);
        UIPanel titlePanel6 = new UIPanel();
        output = new UIOutput();
        output.setValue("Change");
        titlePanel6.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel6);
        UIPanel titlePanel7 = new UIPanel();
        output = new UIOutput();
        output.setValue("Change %");
        titlePanel7.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel7);
        UIPanel titlePanel8 = new UIPanel();
        output = new UIOutput();
        output.setValue("Volume");
        titlePanel8.getChildren().add(output);
        dataPanel.getChildren().add(titlePanel8);

        for (int i=0; i<stockData.length; i++) {
            String[] data = stockData[i].split("\\,");
            UIOutput outputComponent = null;
            UIGraphic imageComponent = null;
            double openPrice = 0;
            double lastPrice = 0;
            double change = 0;
            boolean openPriceAvailable = true;

            // Create and add components wth data values

            // Symbol

            outputComponent = new UIOutput();
            outputComponent.setValue(data[0]);
            dataPanel.getChildren().add(outputComponent);

            // Name

            outputComponent = new UIOutput();
            outputComponent.setValue(data[1]);
            dataPanel.getChildren().add(outputComponent);

            // Open Price (if any)

            outputComponent = new UIOutput();
            try {
                openPrice = new Double(data[2]).doubleValue();
            } catch (NumberFormatException nfe) {
                openPriceAvailable = false;
            }
            outputComponent.setValue(data[2]);
            dataPanel.getChildren().add(outputComponent);

            // Last Price

            outputComponent = new UIOutput();
            if (openPriceAvailable) {
                lastPrice = new Double(data[3]).doubleValue();
                lastPrice = round(lastPrice, 2);
                change = lastPrice - openPrice;
                change = round(change, 2);
            }
            outputComponent.setValue(lastPrice);
            dataPanel.getChildren().add(outputComponent);

            // Arrow (up or down) Graphic

            if (change < 0) {
                imageComponent = new UIGraphic();
                imageComponent.setUrl("resources/down_r.gif");
                dataPanel.getChildren().add(imageComponent);
            } else if (change > 0) {
                imageComponent = new UIGraphic();
                imageComponent.setUrl("resources/up_g.gif");
                dataPanel.getChildren().add(imageComponent);
            } else {
                outputComponent = new UIOutput();
                outputComponent.setValue("");
                dataPanel.getChildren().add(outputComponent);
            }

            // Price Change

            outputComponent = new UIOutput();
            if (change < 0) {
                outputComponent.getAttributes().put("styleClass",
                    "down-color");
            } else if (change > 0) {
                outputComponent.getAttributes().put("styleClass",
                    "up-color");
            }
            outputComponent.setValue(String.valueOf(change));
            dataPanel.getChildren().add(outputComponent);

            // Percent Change

            outputComponent = new UIOutput();
            if (change < 0) {
                outputComponent.getAttributes().put("styleClass",
                    "down-color");
            } else if (change > 0) {
                outputComponent.getAttributes().put("styleClass",
                    "up-color");
            }
            outputComponent.setValue(data[5]);
            dataPanel.getChildren().add(outputComponent);

            // Volume

            outputComponent = new UIOutput();
            outputComponent.setValue(data[6]);
            dataPanel.getChildren().add(outputComponent);
        }
    }

    private void setProxyIfNeeded(UIForm form) {
        UIInput component = (UIInput)form.findComponent("proxyHost");
        String proxyHost = (String)component.getValue();
        component = (UIInput)form.findComponent("proxyPort");
        String proxyPort = (String)component.getValue();
        if ((proxyHost != null && proxyHost.length() > 0) &&
            (proxyPort != null && proxyPort.length() > 0)) {
            try {
                System.setProperty("http.proxyHost", proxyHost);
                System.setProperty("http.proxyPort", proxyPort);
            } catch (SecurityException e) {
            }
        }
    }

    private double round(double val, int places) {
        long factor = (long)Math.pow(10,places);

        // Shift the decimal the correct number of places
        // to the right.
        val = val * factor;

        // Round to the nearest integer.
        long tmp = Math.round(val);

        // Shift the decimal the correct number of places
        // back to the left.
        return (double)tmp / factor;
    }

}

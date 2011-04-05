package com.sun.faces.systest;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponentBase;
import java.io.IOException;
import java.util.Date;

public class MessageOutput extends UIComponentBase {
  public MessageOutput() {
      System.out.println("MessageOutput initialized...");
  }

    @Override
    public String getFamily() {
        return "HelloFamily";
    }


}
package com.sun.faces.composite;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "bb")
@SessionScoped
public class Issue1238Bean implements Serializable {

   public static class DataWrapper {

      private Object value;
      private String datatype;

      public DataWrapper(Object value, String datatype) {
         this.value = value;
         this.datatype = datatype;
      }

      public String toString() {
         return value + "~" + datatype;
      }
      
      public Object getValue() {
         return value;
      }

      public String getDatatype() {
         return datatype;
      }

   }

   private List<DataWrapper> data = new ArrayList<DataWrapper>();

   public Issue1238Bean() {
      //
      data.add(new DataWrapper(3.14159, "double"));
      data.add(new DataWrapper("approximately 3 comma 14", "text"));
   }

   public double getPi() {
      return 3.14159;
   }

   public List<DataWrapper> getList() {
      return data;
   }

}

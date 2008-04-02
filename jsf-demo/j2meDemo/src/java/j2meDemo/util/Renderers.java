package j2meDemo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;

public class Renderers {
   public static Object getConvertedValue(FacesContext context, UIComponent component,
         Object submittedValue) throws ConverterException {
      if (submittedValue instanceof String) {
         Converter converter = getConverter(context, component);
         if (converter != null) {
            return converter.getAsObject(context, component,
                  (String) submittedValue);
         }
      }
      return submittedValue;
   }
   
   public static Converter getConverter(FacesContext context, UIComponent component) {
      if (!(component instanceof ValueHolder)) return null;
      ValueHolder holder = (ValueHolder) component;

      Converter converter = holder.getConverter();
      if (converter != null) return converter;

      ValueBinding valueBinding = component.getValueBinding("value");
      if (valueBinding == null) return null;

      Class targetType = valueBinding.getType(context);
      if (targetType == null) return null;
      // Version 1.0 of the reference implementation will not apply a converter
      // if the target type is String or Object, but that is a bug.

      Application app = context.getApplication();
      return app.createConverter(targetType);
   }
  
   public static String getFormId(FacesContext context, UIComponent component) {
      UIComponent parent = component;
      while (!(parent instanceof UIForm)) parent = parent.getParent();      
      return parent.getClientId(context); 
   }

   public static List getSelectItems(UIComponent component) {
      ArrayList list = new ArrayList();
      Iterator children = component.getChildren().iterator();
      while (children.hasNext()) {
         UIComponent child = (UIComponent) children.next();

         if (child instanceof UISelectItem) {
            Object value = ((UISelectItem) child).getValue();
            if (value == null) {
               UISelectItem item = (UISelectItem) child;
               list.add(new SelectItem(item.getItemValue(),
                     item.getItemLabel(), 
                     item.getItemDescription(), 
                     item.isItemDisabled()));
            } else if (value instanceof SelectItem) {
               list.add(value);
            }
         } else if (child instanceof UISelectItems) {
            Object value = ((UISelectItems) child).getValue();
            if (value instanceof SelectItem)
               list.add(value);
            else if (value instanceof SelectItem[])
               list.addAll(Arrays.asList((SelectItem[]) value));
            else if (value instanceof Collection)
               list.addAll((Collection) value);
            else if (value instanceof Map) {                              
               Iterator entries = ((Map) value).entrySet().iterator();
               while (entries.hasNext()) {
                  Map.Entry entry = (Map.Entry) entries.next();                  
                  list.add(new SelectItem(entry.getKey(), 
                        "" + entry.getValue()));
               }
            }
         }
      }
      return list;
   }
}

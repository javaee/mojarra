package j2meDemo.taglib;


public class SelectOneTag extends J2meComponentTag {   
   public String getComponentType() { return "javax.faces.SelectOne"; }  
   public String getRendererType() { return "j2meDemo.renderkit.Choice"; } 
}

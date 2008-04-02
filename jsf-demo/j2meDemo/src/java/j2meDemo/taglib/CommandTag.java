package j2meDemo.taglib;

public class CommandTag extends J2meComponentTag {   
   public String getComponentType() { return "javax.faces.Command"; }  
   public String getRendererType() { return "j2meDemo.renderkit.Command"; } 
}

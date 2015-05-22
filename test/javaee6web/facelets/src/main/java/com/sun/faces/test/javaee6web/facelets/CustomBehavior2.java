package com.sun.faces.test.javaee6web.facelets;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

@FacesBehavior("my.customBehavior2")
public class CustomBehavior2
  extends ClientBehaviorBase
{
  public CustomBehavior2()
  {
    super();
  }

  @Override
  public String getScript(
    ClientBehaviorContext clientBehaviorContext)
  {
    return "document.getElementById('textForBehavior2').innerHTML='CustomBehavior2 called'; ";
  }
}

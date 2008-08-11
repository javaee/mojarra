/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.webapp.pdl;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.util.List;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

/**
 *
 * @author edburns
 */
public class PDLUtils {

    /**
     * 
     * 
     * @param context
     * @param topLevelComponent The UIComponent in the view to which the attached
     * objects must be attached.  This UIComponent must have its component metadata
     * already associated and available from via the JavaBeans API.
     * @param handlers specified by the page author in the consuming page, 
     * provided to this method by the PDL implementation, this is a list of 
     * implementations of {@link AttachedObjectHandler}, each one of which 
     * represents a relationship between an attached object and the UIComponent 
     * to which it is attached.
     */
    
    
    public static void retargetAttachedObjects(FacesContext context,
            UIComponent topLevelComponent,
            List<AttachedObjectHandler> handlers) {
        
        BeanInfo componentBeanInfo = (BeanInfo) 
                topLevelComponent.getAttributes().get(UIComponent.BEANINFO_KEY);
        // PENDING(edburns): log error message if componentBeanInfo is null;
        if (null == componentBeanInfo) {
            return;
        }
        BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
        // There is an entry in targetList for each attached object in the 
        // <composite:interface> section of the composite component.
        List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
        // Each entry in targetList will vend one or more UIComponent instances
        // that is to serve as the target of an attached object in the consuming
        // page.
        List<UIComponent> targetComponents = null;
        String forAttributeValue, curTargetName, handlerTagId, componentTagId;
        boolean foundMatch = false;
        
        // For each of the attached object handlers...
        for (AttachedObjectHandler curHandler : handlers) {
            // Get the name given to this attached object by the page author
            // in the consuming page.
            forAttributeValue = curHandler.getFor();
            // For each of the attached objects in the <composite:interface> section
            // of this composite component...
            foundMatch = false;
            for (AttachedObjectTarget curTarget : targetList) {
                if (foundMatch) {
                    break;
                }
                // Get the name given to this attached object target by the
                // composite component author
                curTargetName = curTarget.getName();
                targetComponents = curTarget.getTargets();

                if (curHandler instanceof ActionSource2AttachedObjectHandler &&
                    curTarget instanceof ActionSource2AttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
                else if (curHandler instanceof EditableValueHolderAttachedObjectHandler &&
                         curTarget instanceof EditableValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
                else if (curHandler instanceof ValueHolderAttachedObjectHandler &&
                         curTarget instanceof ValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
            }
        }
    }

}

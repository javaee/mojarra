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
        List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
        List<UIComponent> targetComponents = null;
        String handlerTagId, componentTagId;
        
        // For each of the attached objects in this composite component...
        for (AttachedObjectTarget curTarget : targetList) {
            targetComponents = curTarget.getTargets();
            // ...get the list of targets for this attached object target...
            for (UIComponent curTargetComponent : targetComponents) {
                // ...if the current target is an ActionSource2...
                if (curTargetComponent instanceof ActionSource2) {
                    // ...search the handlers list for a handler with an
                    // ID attribute equal to the componentId of curTargetComponent, and
                    // that is an instanceof ActionListenerAttachedObjectHandler
                    for (AttachedObjectHandler curHandler : handlers) {
                        if (null != (handlerTagId = curHandler.getFor())) {
                            

                            
                            (null != (componentTagId = curTargetComponent.getId())) &&
                                componentTagId.equals(handlerTagId) &&
                                curHandler instanceof ActionSource2AttachedObjectHandler) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                        }
                    }
                }
                if (curTargetComponent instanceof EditableValueHolder) {
                    // ...search the handlers list for a handler with an
                    // ID attribute equal to the componentId of curTargetComponent, and
                    // that is an instanceof EditableValueHolderAttachedObjectHandler.
                    for (AttachedObjectHandler curHandler : handlers) {
                        if ((null != (handlerTagId = curHandler.getFor())) &&
                                (null != (componentTagId = curTargetComponent.getId())) &&
                                componentTagId.equals(handlerTagId)) {
                            if (curHandler instanceof EditableValueHolderAttachedObjectHandler) {
                                curHandler.applyAttachedObject(context, curTargetComponent);
                            }
                        }
                    }
                }
                if (curTargetComponent instanceof ValueHolder) {
                // ...search the handlers list for a handler with an
                // ID attribute equal to the componentId of curTargetComponent, and
                // that is an instanceof ConvertHandler.
                    for (AttachedObjectHandler curHandler : handlers) {
                        if ((null != (handlerTagId = curHandler.getFor())) &&
                                (null != (componentTagId = curTargetComponent.getId())) &&
                                componentTagId.equals(handlerTagId)) {
                            if (curHandler instanceof ValueHolderAttachedObjectHandler) {
                                curHandler.applyAttachedObject(context, curTargetComponent);
                            }
                        }
                    }
                }
            }
        }
    }

}

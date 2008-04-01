/*
 * $Id: FacesTag.java,v 1.16 2002/08/09 19:00:54 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTag.java

package com.sun.faces.taglib;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletRequest;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: FacesTag.java,v 1.16 2002/08/09 19:00:54 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class FacesTag extends javax.faces.webapp.FacesTag
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

protected String key = null;
protected String imageKey = null;
protected String bundle = null;

protected String accept = null;
protected String acceptCharSet = null;
protected String accessKey = null;
protected String action = null;
protected String alt = null;
protected String charSet = null;
protected String checked = null;
protected String htmlClass = null;
protected String cols = null;
protected String coords = null;
protected String dir = null;
protected String disabled = null;
protected String encType = null;
protected String htmlFor = null;
protected String height = null;
protected String href = null;
protected String hrefLang = null;
protected String hspace = null;
protected String isMap = null;
protected String label = null;
protected String lang = null;
protected String longDesc = null;
protected String maxLength = null;
protected String method = null;
protected String multiple = null;
protected String name = null;
protected String onBlur = null;
protected String onChange = null;
protected String onClick = null;
protected String onDblClick = null;
protected String onFocus = null;
protected String onKeyDown = null;
protected String onKeyPress = null;
protected String onKeyUp = null;
protected String onMouseDown = null;
protected String onMouseMove = null;
protected String onMouseOut = null;
protected String onMouseOver = null;
protected String onMouseUp = null;
protected String onReset = null;
protected String onSelect = null;
protected String onSubmit = null;
protected String readonly = null;
protected String rel = null;
protected String rev = null;
protected String rows = null;
protected String selected = null;
protected String shape = null;
protected String size = null;
protected String src = null;
protected String style = null;
protected String tabIndex = null;
protected String target = null;
protected String title = null;
protected String type = null;
protected String useMap = null;
protected String value = null;

 
// Relationship Instance Variables

//
// Constructors and Initializers    
//

public FacesTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    public String getKey()
    {
	return key;
    }
    
    public void setKey(String newKey)
    {
	key = newKey;
    }

    public String getImageKey()
    {
	return imageKey;
    }
    
    public void setImageKey(String newImageKey)
    {
	imageKey = newImageKey;
    }

    public String getBundle()
    {
	return bundle;
    }
    
    public void setBundle(String newBundle)
    {
	bundle = newBundle;
    }


    // List of MIME types for file upload - 
    // For FORM/INPUT HTML elements

    public String getAccept()
    {
        return accept;
    }

    public void setAccept(String newAccept) 
    {
        accept = newAccept;
    }

    // List of supported char sets - For FORM HTML element

    public String getAcceptCharSet()
    {
        return acceptCharSet;
    }

    public void setAcceptCharSet(String newAcceptCharSet) 
    {
        acceptCharSet = newAcceptCharSet;
    }
    
    // Accessibility key char -
    // For "A" (Hyperlink)/BUTTON/INPUT/LABEL/TEXTAREA HTML elements

    public String getAccessKey()
    {
        return accessKey;
    }

    public void setAccessKey(String newAccessKey) 
    {
        accessKey = newAccessKey;
    }

    // Server-side form handler - For FORM HTML element

    public String getAction()
    {
        return action;
    }

    public void setAction(String newAction) 
    {
        action = newAction;
    }

    // Short description - For INPUT HTML element

    public String getAlt()
    {
        return alt;
    }

    public void setAlt(String newAlt)
    {
        alt = newAlt;
    }

    // Char encoding for linked resource -
    // For "A" (Hyperlink) HTML element

    public String getCharSet() 
    {
        return charSet;
    }

    public void setCharSet(String newCharSet) 
    {
        charSet = newCharSet;
    }

    // For radio buttons and checkboxes - For INPUT element

    public String getChecked()
    {
        return checked;
    }

    public void setChecked(String newChecked) 
    {
        checked = newChecked;
    }

    // Space seperated list of classes -
    // For all HTML elements pertaining to Faces

    public String getHtmlClass()
    {
        return htmlClass;
    }

    public void setHtmlClass(String newHtmlClass) 
    {
        htmlClass = newHtmlClass;
    }

    // For TEXTAREA HTML element

    public String getCols()
    {
        return cols;
    }

    public void setCols(String newCols)
    {
        cols = newCols;
    }

    // For use with client side image maps - 
    // For "A" (Hyperlink) HTML element

    public String getCoords()
    {
        return coords;
    }

    public void setCoords(String newCoords)
    {
        coords = newCoords;
    }
  
    // Direction for weak/neutral text - For all HTML elements 
    // pertaining to Faces

    public String getDir() 
    {
        return dir;
    }
    
    public void setDir(String newDir) 
    {
        dir = newDir;
    }

    // Means "unavailable" in this context -
    // For BUTTON/INPUT/OPTGROUP/OPTION/SELECT/TEXTAREA HTML elements 

    public String getDisabled()
    {
        return disabled;
    }

    public void setDisabled(String newDisabled)
    {
        disabled = newDisabled;
    }

    // For FORM HTML element

    public String getEncType() 
    {
        return encType;
    }

    public void setEncType(String newEncType)
    {
        encType = newEncType;
    }

    // Matches field "id" value - For LABEL HTML element

    public String getHtmlFor()
    {
        return htmlFor;
    }

    public void setHtmlFor(String newHtmlFor)
    {
        htmlFor = newHtmlFor;
    }

    // Override height - For IMG HTML element

    public String getHeight() 
    {
        return height;
    }

    public void setHeight(String newHeight)
    {
        height = newHeight;
    }

    // For "A" (Hyperlink) HTML element

    public String getHref() 
    {
        return href;
    }

    public void setHref(String newHref)
    {
        href = newHref;
    }

    // Language Code - For "A" (Hyperlink) HTML element

    public String getHrefLang() 
    {
        return hrefLang;
    }

    public void setHrefLang(String newHrefLang)
    {
        hrefLang = newHrefLang;
    }

    // Horizontal space - For IMG HTML element

    public String getHspace()
    {
        return hspace;
    }

    public void setHspace(String newHspace)
    {
        hspace = newHspace;
    }

    // Use server side image map - For IMG/INPUT HTML elements

    public String getIsMap() 
    {
        return isMap;
    }

    public void setIsMap(String newIsMap)
    {
        isMap = newIsMap;
    }

    // For OPTION/OPTGROUP HTML elements

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String newLabel)
    {
        label = newLabel;
    }

    // Language Code - For all HTML elements pertaining to Faces

    public String getLang() 
    {
        return lang;
    }

    public void setLang(String newLang)
    {
        lang = newLang;
    }

    // Link to long description - For IMG HTML element

    public String getLongDesc()
    {
        return longDesc;
    }

    public void setLongDesc(String newLongDesc)
    {
        longDesc = newLongDesc;
    }

    // Maximum chars for text fields - For INPUT HTML elements

    public String getMaxLength() 
    {
        return maxLength;
    }

    public void setMaxLength(String newMaxLength) 
    {
        maxLength = newMaxLength;
    }

    // HTTP method used to submit the form -
    // For FORM HTML element

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String newMethod)
    {
        method = newMethod;
    }

    // Default is single selection - For SELECT HTML element

    public String getMultiple()
    {
        return multiple;
    }

    public void setMultiple(String newMultiple)
    {
        multiple = newMultiple;
    }
 
    // Element name - For BUTTON/TEXTAREA/SELECT/FORM/
    // IMG/"A" (Hyperlink)/INPUT HTML elements 

    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    // Element lost focus - For "A" (Hyperlink)/BUTTON/INPUT/
    // LABEL/SELECT/TEXTAREA HTML elements

    public String getOnBlur() 
    {
        return onBlur;
    }

    public void setOnBlur(String newOnBlur)
    {
        onBlur = newOnBlur;
    }

    // Element value was changed - For INPUT/SELECT/TEXTAREA HTML elements
    public String getOnChange() 
    {
        return onChange;
    }

    public void setOnChange(String newOnChange)
    {
        onChange = newOnChange;
    }

    // A pointer button was clicked - For all HTML elements pertaining
    // to Faces

    public String getOnClick() 
    {
        return onClick;
    }

    public void setOnClick(String newOnClick)
    {
        onClick = newOnClick;
    }

    // A pointer button was double clicked - For all HTML 
    // elements pertaining to Faces

    public String getOnDblClick() 
    {
        return onDblClick;
    }

    public void setOnDblClick(String newOnDblClick)
    {
        onDblClick = newOnDblClick;
    }
    
    // Element got the focus - For "A" (Hyperlink)/BUTTON/
    // INPUT/LABEL/SELECT/TEXTAREA HTML elements

    public String getOnFocus() 
    {
        return onFocus;
    }

    public void setOnFocus(String newOnFocus)
    {
        onFocus = newOnFocus;
    }

    // A key was pressed down - For all HTML elements pertaining
    // to Faces

    public String getOnKeyDown() 
    {
        return onKeyDown;
    }

    public void setOnKeyDown(String newOnKeyDown)
    {
        onKeyDown = newOnKeyDown;
    }
    
    // A key was pressed and released - For all HTML elements
    // pertaining to Faces.

    public String getOnKeyPress() 
    {
        return onKeyPress;
    }

    public void setOnKeyPress(String newOnKeyPress)
    {
        onKeyPress = newOnKeyPress;
    }

    // A key was released - For all HTML elements pertaining
    // to Faces

    public String getOnKeyUp() 
    {
        return onKeyUp;
    }

    public void setOnKeyUp(String newOnKeyUp)
    {
        onKeyUp = newOnKeyUp;
    }

    // A pointer button was pressed down - For all HTML
    // elements pertaining to Faces.

    public String getOnMouseDown() 
    {
        return onMouseDown;
    }

    public void setOnMouseDown(String newOnMouseDown)
    {
        onMouseDown = newOnMouseDown;
    }

    // A pointer was moved within - For all HTML elements
    // pertaining to Faces.

    public String getOnMouseMove() 
    {
        return onMouseMove;
    }

    public void setOnMouseMove(String newOnMouseMove)
    {
        onMouseMove = newOnMouseMove;
    }

    // A pointer was moved away - For all HTML elements
    // pertaining to Faces.

    public String getOnMouseOut() 
    {
        return onMouseOut;
    }

    public void setOnMouseOut(String newOnMouseOut)
    {
        onMouseOut = newOnMouseOut;
    }

    // A pointer was moved onto - For all HTML elements
    // pertaining to Faces.

    public String getOnMouseOver() 
    {
        return onMouseOver;
    }

    public void setOnMouseOver(String newOnMouseOver)
    {
        onMouseOver = newOnMouseOver;
    }

    // A pointer button was released - For all HTML elements
    // pertaining to Faces.

    public String getOnMouseUp() 
    {
        return onMouseUp;
    }

    public void setOnMouseUp(String newOnMouseUp)
    {
        onMouseUp = newOnMouseUp;
    }

    // The form was reset - For FORM HTML element

    public String getOnReset() 
    {
        return onReset;
    }

    public void setOnReset(String newOnReset)
    {
        onReset= newOnReset;
    }

    // Some text was selected - For SELECT HTML element.

    public String getOnSelect() 
    {
        return onSelect;
    }

    public void setOnSelect(String newOnSelect)
    {
        onSelect = newOnSelect;
    }

    // The form was submitted - For FORM HTML element

    public String getOnSubmit() {
        return onSubmit;
    }

    public void setOnSubmit(String newOnSubmit)
    {
        onSubmit = newOnSubmit;
    }

    // For TEXTAREA/INPUT HTML elements

    public String getReadonly() 
    {
        return readonly;
    }

    public void setReadonly(String newReadonly)
    {
        readonly = newReadonly;
    }

    // Forward link types - For "A" (Hyperlink) HTML element

    public String getRel() 
    {
        return rel;
    }

    public void setRel(String newRel)
    {
        rel = newRel;
    }

    // Reverse link types - For "A" (Hyperlink) HTML element

    public String getRev() 
    {
        return rev;
    }

    public void setRev(String newRev)
    {
        rev = newRev;
    }

    // For TEXTAREA HTML element

    public String getRows()
    {
        return rows;
    }

    public void setRows(String newRows)
    {
        rows = newRows;
    }
    
    // For OPTION HTML element

    public String getSelected() 
    {
        return selected;
    }

    public void setSelected(String newSelected)
    {
        selected = newSelected;
    }

    // For use with client-side image maps -
    // For "A" (Hyperlink) HTML element

    public String getShape()
    {
        return shape;
    }

    public void setShape(String newShape)
    {
        shape = newShape;
    }

    // For INPUT/SELECT HTML elements - (means rows visible
    // for SELECT)

    public String getSize() 
    {
        return size;
    }

    public void setSize(String newSize)
    {
        size = newSize;
    }

    // For INPUT/IMG HTML elements

    public String getSrc() 
    {
        return src;
    }

    public void setSrc(String newSrc)
    {
        src = newSrc;
    }

    // Associated style info - For all HTML elements pertaining
    // to Faces.

    public String getStyle() 
    {
        return style;
    }

    public void setStyle(String newStyle)
    {
        style = newStyle;
    }

    // Position in tabbing order - For "A" (Hyperlink)/
    // BUTTON/INPUT/SELECT/TEXTAREA HTML elements.

    public String getTabIndex() 
    {
        return tabIndex;
    }

    public void setTabIndex(String newTabIndex)
    {
        tabIndex = newTabIndex;
    }

    // Render in this frame - For "A" (Hyperlink)/ FORM
    // HTML elements

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String newTarget)
    {
        target = newTarget;
    }

    // Advisory title - For all HTML elements pertaining
    // to Faces.

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }

    // For "A" (Hyperlink)/INPUT/BUTTON HTML elements

    public String getType() 
    {
        return type;
    }

    public void setType(String newType)
    {
        type = newType;
    }

    // Use client-side image map - For IMG/INPUT HTML elements.

    public String getUseMap() 
    {
        return useMap;
    }

    public void setUseMap(String newUseMap)
    {
        useMap = newUseMap;
    }

    // For INPUT/OPTION/BUTTON HTML elements.
    // Required for INPUT type = radio/checkbox

    public String getValue()
    {
        return value;
    }

    public void setValue(String newValue)
    {
        value = newValue;
    }

    /**

    * PENDING(edburns): for some VERY strange reason, sometimes the id
    * gets set with double double quotes, even though the jsp has single
    * double quotes.  WHAAAAT!

    */ 
    public void setId(String newId) { 
	int i = 0;

	if (-1 != newId.indexOf("\"")) {
	    id = newId.substring(1, newId.length() - 1);
	}
	else {
	    id = newId;
	}
    }
    
    public String getId() {
	return id;
    }



//
// General Methods
//

/**

* This is implemented by faces subclasses to allow globally turing off
* the render kit.

*/

public abstract String getLocalRendererType();

//
// Methods from Superclass
// 

public final String getRendererType()
{
    String disableRenderers =System.getProperty(RIConstants.DISABLE_RENDERERS);
    
    if (null != disableRenderers &&
	disableRenderers.equals(RIConstants.DISABLE_RENDERERS)) {
	return null;
    }
    
    return getLocalRendererType();
}

public abstract UIComponent createComponent();

protected void overrideProperties(UIComponent component) 
{
    super.overrideProperties(component);
    String keyAttr = null;

    if (null == component.getAttribute("key")) {
	component.setAttribute("key", getKey());
    }
    if (null == component.getAttribute("imageKey")) {
	component.setAttribute("imageKey", getImageKey());
    }
    if (null == component.getAttribute("bundle")) {
	component.setAttribute("bundle", getBundle());
    }
}

// 
// Methods From TagSupport
//

} // end of class FacesTag

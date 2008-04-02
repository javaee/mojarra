/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.inputhtmlexample;

/**
 * @author Sylvain Vieujot (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:21 $
 */
public class EditorFace {
    
    private String text = "Default unformated text.";
    
    // Options
    private boolean allowEditSource = true;
    private boolean showPropertiesToolBox = false;
    private boolean showLinksToolBox = false;
    private boolean showImagesToolBox = false;
    private boolean showTablesToolBox = false;
	private boolean showCleanupExpressionsToolBox = false;
    private boolean showDebugToolBox = false;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public boolean isAllowEditSource() {
        return allowEditSource;
    }
    public void setAllowEditSource(boolean allowEditSource) {
        this.allowEditSource = allowEditSource;
    }
    public boolean isShowImagesToolBox() {
        return showImagesToolBox;
    }
    public void setShowImagesToolBox(boolean showImagesToolBox) {
        this.showImagesToolBox = showImagesToolBox;
    }
    public boolean isShowLinksToolBox() {
        return showLinksToolBox;
    }
    public void setShowLinksToolBox(boolean showLinksToolBox) {
        this.showLinksToolBox = showLinksToolBox;
    }
    public boolean isShowPropertiesToolBox() {
        return showPropertiesToolBox;
    }
    public void setShowPropertiesToolBox(boolean showPropertiesToolBox) {
        this.showPropertiesToolBox = showPropertiesToolBox;
    }
    public boolean isShowTablesToolBox(){
        return showTablesToolBox;
    }
    public void setShowTablesToolBox(boolean showTablesToolBox){
        this.showTablesToolBox = showTablesToolBox;
    }
    public boolean isShowCleanupExpressionsToolBox() {
		return showCleanupExpressionsToolBox;
	}
	public void setShowCleanupExpressionsToolBox(boolean showCleanupExpressionsToolBox) {
		this.showCleanupExpressionsToolBox = showCleanupExpressionsToolBox;
	}
	public boolean isShowDebugToolBox(){
        return showDebugToolBox;
    }
    public void setShowDebugToolBox(boolean showDebugToolBox){
        this.showDebugToolBox = showDebugToolBox;
    }
}

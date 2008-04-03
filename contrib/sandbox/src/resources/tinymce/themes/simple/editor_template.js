/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

var TinyMCE_SimpleTheme={_buttonMap:'bold,bullist,cleanup,italic,numlist,redo,strikethrough,underline,undo',getEditorTemplate:function(){var html='';html+='<table class="mceEditor" border="0" cellpadding="0" cellspacing="0" width="{$width}" height="{$height}">';html+='<tr><td align="center">';html+='<span id="{$editor_id}">IFRAME</span>';html+='</td></tr>';html+='<tr><td class="mceToolbar" align="center" height="1">';html+=tinyMCE.getButtonHTML('bold','lang_bold_desc','{$themeurl}/images/{$lang_bold_img}','Bold');html+=tinyMCE.getButtonHTML('italic','lang_italic_desc','{$themeurl}/images/{$lang_italic_img}','Italic');html+=tinyMCE.getButtonHTML('underline','lang_underline_desc','{$themeurl}/images/{$lang_underline_img}','Underline');html+=tinyMCE.getButtonHTML('strikethrough','lang_striketrough_desc','{$themeurl}/images/strikethrough.gif','Strikethrough');html+='<img src="' + tinyMCE.mungeURL('{$themeurl}/images/separator.gif') + '" width="2" height="20" class="mceSeparatorLine" />';html+=tinyMCE.getButtonHTML('undo','lang_undo_desc','{$themeurl}/images/undo.gif','Undo');html+=tinyMCE.getButtonHTML('redo','lang_redo_desc','{$themeurl}/images/redo.gif','Redo');html+='<img src="' + tinyMCE.mungeURL('{$themeurl}/images/separator.gif') + '" width="2" height="20" class="mceSeparatorLine" />';html+=tinyMCE.getButtonHTML('cleanup','lang_cleanup_desc','{$themeurl}/images/cleanup.gif','mceCleanup');html+='<img src="' + tinyMCE.mungeURL('{$themeurl}/images/separator.gif') + '" width="2" height="20" class="mceSeparatorLine" />';html+=tinyMCE.getButtonHTML('bullist','lang_bullist_desc','{$themeurl}/images/bullist.gif','InsertUnorderedList');html+=tinyMCE.getButtonHTML('numlist','lang_numlist_desc','{$themeurl}/images/numlist.gif','InsertOrderedList');html+='</td></tr></table>';return{delta_width:0,delta_height:20,html:html}},handleNodeChange:function(editor_id,node){tinyMCE.switchClass(editor_id+'_bold','mceButtonNormal');tinyMCE.switchClass(editor_id+'_italic','mceButtonNormal');tinyMCE.switchClass(editor_id+'_underline','mceButtonNormal');tinyMCE.switchClass(editor_id+'_strikethrough','mceButtonNormal');tinyMCE.switchClass(editor_id+'_bullist','mceButtonNormal');tinyMCE.switchClass(editor_id+'_numlist','mceButtonNormal');do{switch(node.nodeName.toLowerCase()){case"b":case"strong":tinyMCE.switchClass(editor_id+'_bold','mceButtonSelected');break;case"i":case"em":tinyMCE.switchClass(editor_id+'_italic','mceButtonSelected');break;case"u":tinyMCE.switchClass(editor_id+'_underline','mceButtonSelected');break;case"strike":tinyMCE.switchClass(editor_id+'_strikethrough','mceButtonSelected');break;case"ul":tinyMCE.switchClass(editor_id+'_bullist','mceButtonSelected');break;case"ol":tinyMCE.switchClass(editor_id+'_numlist','mceButtonSelected');break}}while((node=node.parentNode)!=null)}};tinyMCE.addTheme("simple",TinyMCE_SimpleTheme);tinyMCE.addButtonMap(TinyMCE_SimpleTheme._buttonMap);
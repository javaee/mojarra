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

// /JsfRiSandboxDemo-jsp/static/tiny_mce/tiny_mce.js.jsf
TinyMCE_Engine.prototype.JSF_SUFFIX_MAPPING = ".jsf";

TinyMCE_Engine.prototype.mungeURL = function (url) {
        if (this.JSF_SUFFIX_MAPPING != null) {
            return url + this.JSF_SUFFIX_MAPPING;
        } else {
            document.write(url.split("/")); 
        }
    };

TinyMCE_Engine.prototype.loadScript = function(url) {
        url = this.mungeURL(url);
        var i;

        for (i=0; i<this.loadedFiles.length; i++) {
            if (this.loadedFiles[i] == url)
                return;
        }

        if (tinyMCE.settings.strict_loading_mode)
            this.pendingFiles[this.pendingFiles.length] = url;
        else
            document.write('<sc'+'ript language="javascript" type="text/javascript" src="' + url + '"></script>');

        this.loadedFiles[this.loadedFiles.length] = url;
    };

TinyMCE_Engine.prototype.loadCSS = function(url) {
        url = this.mungeURL(url);
        var ar = url.replace(/\s+/, '').split(',');
        var lflen = 0, csslen = 0;
        var skip = false;
        var x = 0, i = 0, nl, le;

        for (x = 0,csslen = ar.length; x<csslen; x++) {
            if (ar[x] != null && ar[x] != 'null' && ar[x].length > 0) {
                /* Make sure it doesn't exist. */
                for (i=0, lflen=this.loadedFiles.length; i<lflen; i++) {
                    if (this.loadedFiles[i] == ar[x]) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    if (tinyMCE.settings.strict_loading_mode) {
                        nl = document.getElementsByTagName("head");

                        le = document.createElement('link');
                        le.setAttribute('href', ar[x]);
                        le.setAttribute('rel', 'stylesheet');
                        le.setAttribute('type', 'text/css');

                        nl[0].appendChild(le);          
                    } else
                        document.write('<link href="' + ar[x] + '" rel="stylesheet" type="text/css" />');

                    this.loadedFiles[this.loadedFiles.length] = ar[x];
                }
            }
        }
    };
    
TinyMCE_Engine.prototype.importCSS = function(doc, css) {
        var css_ary = css.replace(/\s+/, '').split(',');
        var csslen, elm, headArr, x, css_file;

        for (x = 0, csslen = css_ary.length; x<csslen; x++) {
            css_file = css_ary[x];

            if (css_file != null && css_file != 'null' && css_file.length > 0) {
                // Is relative, make absolute
                if (css_file.indexOf('://') == -1 && css_file.charAt(0) != '/')
                    css_file = this.documentBasePath + "/" + css_file;

                if (typeof(doc.createStyleSheet) == "undefined") {
                    elm = doc.createElement("link");

                    elm.rel = "stylesheet";
                    elm.href = this.mungeURL(css_file);

                    if ((headArr = doc.getElementsByTagName("head")) != null && headArr.length > 0)
                        headArr[0].appendChild(elm);
                } else
                    doc.createStyleSheet(this.mungeURL(css_file));
            }
        }
    }
    
    
TinyMCE_Engine.prototype.getButtonHTML = function(id, lang, img, cmd, ui, val) {
        var h = '', m, x, io = '';
        img = this.mungeURL(img);

        cmd = 'tinyMCE.execInstanceCommand(\'{$editor_id}\',\'' + cmd + '\'';

        if (typeof(ui) != "undefined" && ui != null)
            cmd += ',' + ui;

        if (typeof(val) != "undefined" && val != null)
            cmd += ",'" + val + "'";

        cmd += ');';

        // Patch for IE7 bug with hover out not restoring correctly
        if (tinyMCE.isRealIE)
            io = 'onmouseover="tinyMCE.lastHover = this;"';

        // Use tilemaps when enabled and found and never in MSIE since it loads the tile each time from cache if cahce is disabled
        if (tinyMCE.getParam('button_tile_map') && (!tinyMCE.isIE || tinyMCE.isOpera) && (m = this.buttonMap[id]) != null && (tinyMCE.getParam("language") == "en" || img.indexOf('$lang') == -1)) {
            // Tiled button
            x = 0 - (m * 20) == 0 ? '0' : 0 - (m * 20);
            h += '<a id="{$editor_id}_' + id + '" href="javascript:' + cmd + '" onclick="' + cmd + 'return false;" onmousedown="return false;" ' + io + ' class="mceTiledButton mceButtonNormal" target="_self">';
            h += '<img src="{$themeurl}/images/spacer.gif" style="background-position: ' + x + 'px 0" title="{$' + lang + '}" />';
            h += '</a>';
        } else {
            // Normal button
            h += '<a id="{$editor_id}_' + id + '" href="javascript:' + cmd + '" onclick="' + cmd + 'return false;" onmousedown="return false;" ' + io + ' class="mceButtonNormal" target="_self">';
            h += '<img src="' + img + '" title="{$' + lang + '}" />';
            h += '</a>';
        }

        return h;
    };

TinyMCE_Engine.prototype.getMenuButtonHTML = function(id, lang, img, mcmd, cmd, ui, val) {
        var h = '', m, x;
        img = this.mungeURL(img);

        mcmd = 'tinyMCE.execInstanceCommand(\'{$editor_id}\',\'' + mcmd + '\');';
        cmd = 'tinyMCE.execInstanceCommand(\'{$editor_id}\',\'' + cmd + '\'';

        if (typeof(ui) != "undefined" && ui != null)
            cmd += ',' + ui;

        if (typeof(val) != "undefined" && val != null)
            cmd += ",'" + val + "'";

        cmd += ');';

        // Use tilemaps when enabled and found and never in MSIE since it loads the tile each time from cache if cahce is disabled
        if (tinyMCE.getParam('button_tile_map') && (!tinyMCE.isIE || tinyMCE.isOpera) && (m = tinyMCE.buttonMap[id]) != null && (tinyMCE.getParam("language") == "en" || img.indexOf('$lang') == -1)) {
            x = 0 - (m * 20) == 0 ? '0' : 0 - (m * 20);

            if (tinyMCE.isRealIE)
                h += '<span id="{$editor_id}_' + id + '" class="mceMenuButton" onmouseover="tinyMCE._menuButtonEvent(\'over\',this);tinyMCE.lastHover = this;" onmouseout="tinyMCE._menuButtonEvent(\'out\',this);">';
            else
                h += '<span id="{$editor_id}_' + id + '" class="mceMenuButton">';

            h += '<a href="javascript:' + cmd + '" onclick="' + cmd + 'return false;" onmousedown="return false;" class="mceTiledButton mceMenuButtonNormal" target="_self">';
            h += '<img src="{$themeurl}/images/spacer.gif" style="width: 20px; height: 20px; background-position: ' + x + 'px 0" title="{$' + lang + '}" /></a>';
            h += '<a href="javascript:' + mcmd + '" onclick="' + mcmd + 'return false;" onmousedown="return false;"><img src="{$themeurl}/images/button_menu.gif" title="{$' + lang + '}" class="mceMenuButton" />';
            h += '</a></span>';
        } else {
            if (tinyMCE.isRealIE)
                h += '<span id="{$editor_id}_' + id + '" dir="ltr" class="mceMenuButton" onmouseover="tinyMCE._menuButtonEvent(\'over\',this);tinyMCE.lastHover = this;" onmouseout="tinyMCE._menuButtonEvent(\'out\',this);">';
            else
                h += '<span id="{$editor_id}_' + id + '" dir="ltr" class="mceMenuButton">';

            h += '<a href="javascript:' + cmd + '" onclick="' + cmd + 'return false;" onmousedown="return false;" class="mceMenuButtonNormal" target="_self">';
            h += '<img src="' + img + '" title="{$' + lang + '}" /></a>';
            h += '<a href="javascript:' + mcmd + '" onclick="' + mcmd + 'return false;" onmousedown="return false;"><img src="{$themeurl}/images/button_menu.gif" title="{$' + lang + '}" class="mceMenuButton" />';
            h += '</a></span>';
        }

        return h;
    };
    
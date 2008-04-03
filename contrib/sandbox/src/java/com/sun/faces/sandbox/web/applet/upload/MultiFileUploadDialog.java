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

package com.sun.faces.sandbox.web.applet.upload;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

class MultiFileUploadDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    protected JFileChooser fc;
    protected File files[] = null;
    protected int result;

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
            files = fc.getSelectedFiles();
            result = JFileChooser.APPROVE_OPTION;
        } else {
            files = null; // Just to make sure
            result = JFileChooser.CANCEL_OPTION;
        }
        fc = null;
        setVisible(false);
    }

    public File[] getFiles() {
        return files;
    }

    /**
     * Display the dialog modally
     *
     */
    public int display() {
        setModal(true);
        setVisible(true);

        return result;
    }

    public MultiFileUploadDialog(String startingDir, String fileFilter) {
        fc = new JFileChooser(startingDir);
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addActionListener(this);

        FileFilter filter = createFilter(fileFilter);
        if (filter != null) {
            fc.setFileFilter(filter);
        }

        add(fc);
        this.setPreferredSize(new Dimension(600,500));
        pack();
    }

    protected MultiFileUploadFileFilter createFilter(String fileFilter) {
        MultiFileUploadFileFilter filter = null;
        String[] parts = fileFilter.split("\\|");
        if (parts.length == 2) {
            filter = new MultiFileUploadFileFilter();
            for (String ext : parts[1].split(",")) {
                filter.addExtension(ext);
            }
            filter.setDescription(parts[0]);
        }

        return filter;
    }
}
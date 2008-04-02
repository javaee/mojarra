/**
 * 
 */
package com.sun.faces.sandbox.web.applet.upload;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * @author Jason Lee
 *
 */
public class ButtonApplet extends BaseApplet implements ActionListener {
    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
        MultiFileUploadDialog dialog = new MultiFileUploadDialog(startDir, fileFilter);
        int returnVal = dialog.display();

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = dialog.getFiles();
            if (files != null) {
                uploadFiles(Arrays.asList(files));
            }
        } else { //if (returnVal == JFileChooser.CANCEL_OPTION) {
            this.destroy();
        }
        dialog = null;
    }

    @Override
    public void init() {
        super.init();
        JButton button = new JButton(buttonText);
        button.addActionListener(this);
        add(button);
    }
}

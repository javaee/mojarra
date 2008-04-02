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
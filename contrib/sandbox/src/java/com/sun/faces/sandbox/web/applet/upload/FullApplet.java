/**
 * 
 */
package com.sun.faces.sandbox.web.applet.upload;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class FullApplet extends BaseApplet {
    private static final long serialVersionUID = 1L;
    protected javax.swing.JButton btnAddFiles;
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnRemove;
    protected javax.swing.JButton btnUpload;
    protected FileTableModel model = new FileTableModel();
    protected javax.swing.JPanel pnlButtons;
    protected javax.swing.JPanel pnlFiles;
    protected javax.swing.JScrollPane spFiles;
    protected javax.swing.JTable tblFiles;

    public boolean getResult() {
        return result;
    }

    public void init() {
        super.init();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void btnAddFilesActionPerformed(java.awt.event.ActionEvent evt) {
        MultiFileUploadDialog dialog = new MultiFileUploadDialog(startDir, fileFilter);
        int returnVal = dialog.display();

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File files[] = dialog.getFiles();
            if (files != null) {
                for (File file : files) {
                    model.addFile(file);
                }
                tblFiles.tableChanged(null);
            }
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        result = CANCEL;
    }

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selectedRows = tblFiles.getSelectedRows();
        for (int selectedRow : selectedRows) {
            model.removeFile(selectedRow);
        }
        tblFiles.tableChanged(null);
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {
        List<File> files = model.getFiles();
        uploadFiles(files.toArray(new File[]{}));
        setVisible(false);
        result = UPLOAD;
    }

    private void initComponents() {
        pnlButtons = new javax.swing.JPanel();
        btnAddFiles = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlFiles = new javax.swing.JPanel();
        spFiles = new javax.swing.JScrollPane();
        tblFiles = new javax.swing.JTable();

//        setBounds(new java.awt.Rectangle(0, 0, 500, 200));
//        setMinimumSize(new java.awt.Dimension(200, 75));
        pnlButtons.setLayout(new java.awt.GridLayout(1, 0));

        btnAddFiles.setText("Add Files");
        btnAddFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFilesActionPerformed(evt);
            }
        });

        pnlButtons.add(btnAddFiles);

        btnRemove.setText("Remove File(s)");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        pnlButtons.add(btnRemove);

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        pnlButtons.add(btnUpload);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

//        pnlButtons.add(btnCancel);

        getContentPane().add(pnlButtons, java.awt.BorderLayout.NORTH);

        pnlFiles.setLayout(new java.awt.CardLayout());

        pnlFiles.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                pnlFilesComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlFilesComponentShown(evt);
            }
        });

        tblFiles.setModel(model);
        tblFiles.setName("Files Tables");
        spFiles.setViewportView(tblFiles);

        pnlFiles.add(spFiles, "card2");

        getContentPane().add(pnlFiles, java.awt.BorderLayout.CENTER);
    }

    private void pnlFilesComponentResized(java.awt.event.ComponentEvent evt) {
        pnlFilesComponentShown(evt);
    }


    private void pnlFilesComponentShown(java.awt.event.ComponentEvent evt) {
        spFiles.setSize(spFiles.getParent().getSize());
    }
    
}

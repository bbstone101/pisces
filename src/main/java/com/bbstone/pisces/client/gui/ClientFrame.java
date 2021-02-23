/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbstone.pisces.client.gui;

import com.bbstone.pisces.comm.gui.TreeNodeBuilder;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.comm.gui.DirChooser;
import java.awt.CardLayout;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author bbstone
 */
public class ClientFrame extends javax.swing.JFrame {

    /**
     * Creates new form ClientFrame
     */
    public ClientFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cardPanel = new javax.swing.JPanel();
        configPanel = new javax.swing.JPanel();
        infoScrollPane = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();
        portLabel = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        hostLabel = new javax.swing.JLabel();
        hostTextField = new javax.swing.JTextField();
        clientDirLabel = new javax.swing.JLabel();
        clientDirTextField = new javax.swing.JTextField();
        selectDirButton = new javax.swing.JButton();
        msgScrollPane = new javax.swing.JScrollPane();
        msgTextArea = new javax.swing.JTextArea();
        resetButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        transferPanel = new javax.swing.JPanel();
        bottomToolBar = new javax.swing.JToolBar();
        fileTreeScrollPane = new javax.swing.JScrollPane();
        fileTree = new javax.swing.JTree(TreeNodeBuilder.buildTreeNodes(Config.clientDir(), "Client Dir"));
        contentPanel = new javax.swing.JSplitPane();
        fileInfoPanel = new javax.swing.JPanel();
        transferLabel = new javax.swing.JLabel();
        transferProgressBar = new javax.swing.JProgressBar();
        filePathLabel = new javax.swing.JLabel();
        fileCatLabel = new javax.swing.JLabel();
        fileSizeLabel = new javax.swing.JLabel();
        checksumLabel = new javax.swing.JLabel();
        filePathTextField = new javax.swing.JTextField();
        fileCatTextField = new javax.swing.JTextField();
        fileSizeTextField = new javax.swing.JTextField();
        checksumTextField = new javax.swing.JTextField();
        reloadButton = new javax.swing.JButton();
        transferButton = new javax.swing.JButton();
        totalProgressLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        existsFilesLabel = new javax.swing.JLabel();
        logPanel = new javax.swing.JScrollPane();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        configMenuItem = new javax.swing.JMenuItem();
        transferMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        docsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cardPanel.setLayout(new java.awt.CardLayout());

        infoTextArea.setEditable(false);
        infoTextArea.setBackground(new java.awt.Color(238, 238, 238));
        infoTextArea.setColumns(20);
        infoTextArea.setLineWrap(true);
        infoTextArea.setRows(5);
        infoTextArea.setText("Configuration Panel setup client received files whiches storage directory, server host, server port for listening, SSL enabled or not.");
        infoTextArea.setAutoscrolls(false);
        infoTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Info:"));
        infoTextArea.setDisabledTextColor(new java.awt.Color(0, 51, 255));
        infoTextArea.setEnabled(false);
        infoScrollPane.setViewportView(infoTextArea);

        portLabel.setText("Port: ");

        portTextField.setText("8899");

        hostLabel.setText("Host: ");

        clientDirLabel.setText("Client Dir: ");

        selectDirButton.setText("Select ...");
        selectDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDirButtonActionPerformed(evt);
            }
        });

        msgTextArea.setEditable(false);
        msgTextArea.setColumns(20);
        msgTextArea.setLineWrap(true);
        msgTextArea.setRows(5);
        msgTextArea.setAutoscrolls(false);
        msgTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Message:"));
        msgTextArea.setDisabledTextColor(new java.awt.Color(255, 51, 51));
        msgTextArea.setEnabled(false);
        msgScrollPane.setViewportView(msgTextArea);

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout configPanelLayout = new javax.swing.GroupLayout(configPanel);
        configPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, configPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configPanelLayout.createSequentialGroup()
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portLabel)
                            .addComponent(hostLabel)
                            .addComponent(clientDirLabel))
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(configPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(clientDirTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(configPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(msgScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, configPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addContainerGap())
        );
        configPanelLayout.setVerticalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPanelLayout.createSequentialGroup()
                .addComponent(infoScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portLabel)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostLabel)
                    .addComponent(hostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientDirLabel)
                    .addComponent(clientDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectDirButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetButton)
                    .addComponent(clearButton)
                    .addComponent(saveButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        cardPanel.add(configPanel, "configCard");
        initConfigFormData();

        bottomToolBar.setRollover(true);

        fileTreeScrollPane.setViewportView(fileTree);

        contentPanel.setDividerLocation(290);
        contentPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        transferLabel.setText("Receiving: ");

        filePathLabel.setText("File Path: ");

        fileCatLabel.setText("File Cat: ");

        fileSizeLabel.setText("File Size: ");

        checksumLabel.setText("CheckSum: ");

        reloadButton.setText("Reload");
        reloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadButtonActionPerformed(evt);
            }
        });

        transferButton.setText("Transfer");

        totalProgressLabel.setText("(2/10)");

        jLabel2.setText("Exists Files: ");

        existsFilesLabel.setText("5");

        javax.swing.GroupLayout fileInfoPanelLayout = new javax.swing.GroupLayout(fileInfoPanel);
        fileInfoPanel.setLayout(fileInfoPanelLayout);
        fileInfoPanelLayout.setHorizontalGroup(
            fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fileInfoPanelLayout.createSequentialGroup()
                .addGap(0, 308, Short.MAX_VALUE)
                .addComponent(reloadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transferButton))
            .addGroup(fileInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fileInfoPanelLayout.createSequentialGroup()
                        .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileSizeLabel)
                            .addComponent(checksumLabel)
                            .addComponent(fileCatLabel)
                            .addComponent(filePathLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filePathTextField)
                            .addComponent(fileCatTextField)
                            .addComponent(fileSizeTextField)
                            .addComponent(checksumTextField)))
                    .addGroup(fileInfoPanelLayout.createSequentialGroup()
                        .addComponent(transferLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(existsFilesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(transferProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        fileInfoPanelLayout.setVerticalGroup(
            fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileInfoPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(transferProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transferLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalProgressLabel)
                    .addComponent(jLabel2)
                    .addComponent(existsFilesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filePathLabel)
                    .addComponent(filePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileCatLabel)
                    .addComponent(fileCatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileSizeLabel)
                    .addComponent(fileSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checksumLabel)
                    .addComponent(checksumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fileInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reloadButton)
                    .addComponent(transferButton))
                .addContainerGap())
        );

        contentPanel.setTopComponent(fileInfoPanel);
        contentPanel.setRightComponent(logPanel);

        javax.swing.GroupLayout transferPanelLayout = new javax.swing.GroupLayout(transferPanel);
        transferPanel.setLayout(transferPanelLayout);
        transferPanelLayout.setHorizontalGroup(
            transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bottomToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(transferPanelLayout.createSequentialGroup()
                .addComponent(fileTreeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
        );
        transferPanelLayout.setVerticalGroup(
            transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transferPanelLayout.createSequentialGroup()
                .addGroup(transferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(transferPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(contentPanel))
                    .addComponent(fileTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        cardPanel.add(transferPanel, "transferCard");

        fileMenu.setText("File");

        configMenuItem.setText("Config");
        configMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(configMenuItem);

        transferMenuItem.setText("Transfer");
        transferMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferActionPerformed(evt);
            }
        });
        fileMenu.add(transferMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        mainMenu.add(fileMenu);

        helpMenu.setText("Help");

        docsMenuItem.setText("Docs");
        docsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docsMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(docsMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void configMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configMenuItemActionPerformed
        // TODO add your handling code here:
        CardLayout cardLayout = (CardLayout)cardPanel.getLayout();
        cardLayout.show(cardPanel, "configCard");
        //cardLayout.show(basePanel, "transferPanel");
    }//GEN-LAST:event_configMenuItemActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferActionPerformed
        // TODO add your handling code here:
        CardLayout cardLayout = (CardLayout)cardPanel.getLayout();
        cardLayout.show(cardPanel, "transferCard");
    }//GEN-LAST:event_transferActionPerformed

    private void docsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docsMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_docsMenuItemActionPerformed

    private void reloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reloadButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // clear msg text
        this.msgTextArea.setText(null);
        //
        this.portTextField.setText(null);
        this.hostTextField.setText(null);
        this.clientDirTextField.setText(null);
    }//GEN-LAST:event_clearButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO read form data from config.properties
        // clear msg text
        this.msgTextArea.setText(null);
        // 
        this.hostTextField.setText(Config.host());
        this.portTextField.setText(String.valueOf(Config.port()));
        this.clientDirTextField.setText(Config.clientDir());
        
    }//GEN-LAST:event_resetButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        //clear message area
        this.msgTextArea.setText(null);
        StringBuilder sbu = new StringBuilder();
        // validate form data
        if (StringUtils.isAnyBlank(this.portTextField.getText(), this.hostTextField.getText(), this.clientDirTextField.getText())) {
            sbu.append("port/host/server dir cannot be empty.");
            this.msgTextArea.setText(sbu.toString());
            return ;
        }
        if (!Files.isDirectory(Paths.get(this.clientDirTextField.getText()))) {
            sbu.append("Client Dir Field input path is not directory.");
            this.msgTextArea.setText(sbu.toString());
            return ;
        }
        // do save procedure
        String host = StringUtils.trim(this.hostTextField.getText());
        String port = StringUtils.trim(this.portTextField.getText());
        String clientDir = StringUtils.trim(this.clientDirTextField.getText());
        Config.setProperty(Config.SERVER_HOST_KEY, host);
        Config.setProperty(Config.SERVER_PORT_KEY, port);
        Config.setProperty(Config.CLIENT_DIR_KEY, clientDir);
        Config.saveUpdate(); // batch save changes to file
        
        this.msgTextArea.setText("configurtion saved OK.");
    }//GEN-LAST:event_saveButtonActionPerformed

    private void selectDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDirButtonActionPerformed
        DirChooser dirChooser = new DirChooser(this.clientDirTextField);
        int val = dirChooser.getDirChooser().showOpenDialog(null);
        dirChooser.setVal(val);
    }//GEN-LAST:event_selectDirButtonActionPerformed

    private void initConfigFormData() {
        this.hostTextField.setText(Config.host());
        this.portTextField.setText(String.valueOf(Config.port()));
        this.clientDirTextField.setText(Config.clientDir());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JToolBar bottomToolBar;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel checksumLabel;
    private javax.swing.JTextField checksumTextField;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel clientDirLabel;
    private javax.swing.JTextField clientDirTextField;
    private javax.swing.JMenuItem configMenuItem;
    private javax.swing.JPanel configPanel;
    private javax.swing.JSplitPane contentPanel;
    private javax.swing.JMenuItem docsMenuItem;
    private javax.swing.JLabel existsFilesLabel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JLabel fileCatLabel;
    private javax.swing.JTextField fileCatTextField;
    private javax.swing.JPanel fileInfoPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel filePathLabel;
    private javax.swing.JTextField filePathTextField;
    private javax.swing.JLabel fileSizeLabel;
    private javax.swing.JTextField fileSizeTextField;
    private javax.swing.JTree fileTree;
    private javax.swing.JScrollPane fileTreeScrollPane;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel hostLabel;
    private javax.swing.JTextField hostTextField;
    private javax.swing.JScrollPane infoScrollPane;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane logPanel;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JScrollPane msgScrollPane;
    private javax.swing.JTextArea msgTextArea;
    private javax.swing.JLabel portLabel;
    private javax.swing.JTextField portTextField;
    private javax.swing.JButton reloadButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton selectDirButton;
    private javax.swing.JLabel totalProgressLabel;
    private javax.swing.JButton transferButton;
    private javax.swing.JLabel transferLabel;
    private javax.swing.JMenuItem transferMenuItem;
    private javax.swing.JPanel transferPanel;
    private javax.swing.JProgressBar transferProgressBar;
    // End of variables declaration//GEN-END:variables
}

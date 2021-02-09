/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbstone.pisces.client.gui;

import com.bbstone.pisces.comm.gui.TreeNodeBuilder;
import com.bbstone.pisces.config.Config;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author bbstone
 */
public class DnDJTree extends JTree implements DropTargetListener {

    public DnDJTree() {
        super();
    }
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        DataFlavor[] dataFlavors = dtde.getCurrentDataFlavors();
        if(dataFlavors[0].match(DataFlavor.javaFileListFlavor)){
            try {
                Transferable tr = dtde.getTransferable();
                Object obj = tr.getTransferData(DataFlavor.javaFileListFlavor);
                List<File> files = (List<File>)obj;
                // TODO update treeModel
                
//                DefaultMutableTreeNode top = new DefaultMutableTreeNode("Client Dir:");
//                DefaultMutableTreeNode dir = new DefaultMutableTreeNode(Config.clientDir);
//                top.add(dir);
                TreeNode root = TreeNodeBuilder.buildTreeNodes(Config.clientDir, "Client DIR");
                
                TreeModel treeModel = new DefaultTreeModel(root);
                this.setModel(treeModel);
                
                /**
                for(int i = 0; i < files.size(); i++){
                    append(files.get(i).getAbsolutePath()+"/r/n");
                }
                */
            } catch (UnsupportedFlavorException ex) {

            } catch (IOException ex) {

            }
        }
    }
    
    

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbstone.pisces.client.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JScrollPane;

/**
 *
 * @author bbstone
 */
public class DragDropScrollPane extends JScrollPane implements DropTargetListener {

    private DropTarget dropTarget;
    
    public DragDropScrollPane() {
        dropTarget = new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE, this, true);
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
                for(int i = 0; i < files.size(); i++){
                    //append(files.get(i).getAbsolutePath()+"/r/n");
                }
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

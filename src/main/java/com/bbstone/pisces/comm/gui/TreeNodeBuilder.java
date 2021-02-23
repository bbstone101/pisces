/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbstone.pisces.comm.gui;

import com.bbstone.pisces.client.base.ClientCache;
import com.bbstone.pisces.comm.BFileCombo;
import com.bbstone.pisces.comm.BFileTreeNode;
import com.bbstone.pisces.config.Config;
import static com.bbstone.pisces.util.BFileUtil.checksum;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.tree.TreeNode;

import com.bbstone.pisces.util.ConstUtil;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author bbstone
 */
@Slf4j
public class TreeNodeBuilder {

    private static final String DISCONNECTED = "disconnected";
    private static final String CONNECTED = "connected";
    
    public static String clientRootDisplayName() {
        if (Files.isDirectory(Paths.get(Config.clientDir()))) {
            String dirName = new File(Config.clientDir()).getName();
            return String.format("[%s, %s]", dirName, DISCONNECTED);
        }
        return String.format("[%s, %s]", "Client Dir", DISCONNECTED);
    }

    // TODO ListRspHandler will get serverDir treeNodes, then transform to list save to clientCache
//    public static String serverRootDisplayName() {
//        BFileCombo combo = ClientCache.getCombo();
//        if (combo == null) {
//            return String.format("[%s, %s]", "Server Dir", DISCONNECTED);
//        }
//        BFileTreeNode treeNode = combo.getTreeNode();
//        return String.format("[%s, %s]", treeNode.getName(), CONNECTED);
//    }

    public static TreeNode buildServerTreeNode() {
        BFileCombo combo = ClientCache.getCombo();
        if (combo == null) {
            return  new MyTreeNode(new MyUserObject("", String.format("[%s, %s]", "Server Dir", DISCONNECTED)));
        }
        BFileTreeNode bftree = combo.getTreeNode();
        MyTreeNode top = new MyTreeNode(new MyUserObject(bftree.getAbsolutePath(), bftree.getName()));
        traversalNode(top, bftree);
        return top;
    }

    private static void traversalNode(MyTreeNode parentNode, BFileTreeNode bftree) {
        if (bftree.getChildren().size() > 0) {
            for (BFileTreeNode bFileTreeNode : bftree.getChildren()) {
                MyTreeNode childNode = new MyTreeNode(new MyUserObject(bFileTreeNode.getAbsolutePath(), bFileTreeNode.getName()));
                traversalNode(childNode, bFileTreeNode);
            }
        }
    }
    /**
     * Build Client TreeNode
     */
    public static TreeNode buildTreeNodes(String root, String displayName) {
//        MyUserObject uo = new MyUserObject("", "Client Dir:");
//        MyTreeNode top = new MyTreeNode(uo);
//        MyTreeNode dir = new MyTreeNode(new MyUserObject(root, ""));
//        top.add(dir);
//        traversalNode(dir);

        MyTreeNode top = new MyTreeNode(new MyUserObject(root, displayName));
        traversalNode(top);
        return top;
    }

    private static void traversalNode(MyTreeNode parentNode) {
        MyUserObject parentUO = (MyUserObject)parentNode.getUserObject();
        log.info("parentPath: {}", parentUO.getFilepath());
        File clientDir = new File(parentUO.getFilepath());
        File[] files = clientDir.listFiles();
        Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            log.info("childPath: {}", file.getName());
            MyTreeNode childNode = new MyTreeNode(new MyUserObject(file.getAbsolutePath(), file.getName()));
            parentNode.add(childNode);
            if (file.isDirectory()) {
                traversalNode(childNode);
            }
        }
    }

    public static void main(String[] args) {
        buildTreeNodes(Config.clientDir(), "Client Directory::");
    }
            
    
    
}

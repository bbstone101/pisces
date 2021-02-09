/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbstone.pisces.comm.gui;

import com.bbstone.pisces.config.Config;
import static com.bbstone.pisces.util.BFileUtil.checksum;

import java.io.File;
import java.util.Arrays;
import javax.swing.tree.TreeNode;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author bbstone
 */
@Slf4j
public class TreeNodeBuilder {

    // root = Config.clientDir
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
        buildTreeNodes(Config.clientDir, "Client Directory::");
    }
            
    
    
}

package com.bbstone.pisces.comm.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyTreeNode extends DefaultMutableTreeNode {

    public MyTreeNode(MyUserObject userObject) {
        super(userObject);
    }

    @Override
    public String toString() {
        if (userObject == null) {
            return "";
        } else {
            return ((MyUserObject)userObject).getFilename();
        }
    }
}

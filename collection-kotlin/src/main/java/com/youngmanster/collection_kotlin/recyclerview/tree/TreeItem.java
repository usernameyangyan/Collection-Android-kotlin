package com.youngmanster.collection_kotlin.recyclerview.tree;

import com.youngmanster.collection_kotlin.recyclerview.tree.base.Tree;

import java.util.ArrayList;

/**
 * @author: yangyan
 */
public abstract class TreeItem implements Tree {

    private  ArrayList children;

    private boolean isOpen;

    public final boolean isOpen() {
        return isOpen;
    }

    public final void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public final boolean isExpand() {
        return isOpen;
    }


    @Override
    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children){
        this.children=children;
    }
}

package com.youngmanster.collection_kotlin.recyclerview.tree.base;

import java.util.ArrayList;

/**
 * @author: yangyan
 */
public interface Tree<T extends Tree> {

    int getLevel();

    ArrayList<T> getChildren();

    boolean isExpand();

}

package ru.sbtqa.tag.swingback.jemmy.components;

import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JTreeOperator;

import javax.swing.tree.TreePath;

public class Tree extends JTreeOperator {

    public Tree(ContainerOperator cont, String text, int row, int index) {
        super(cont, text, row, index);
    }

    /**
     * Select to the specified tree node by path.
     *
     * @param paths The path to node.
     */
    public void chooseTreeNode(String[] paths) {
        TreePath node = findPath(paths[0].trim());
        expandPath(node);
        for (int i = 1; i < paths.length; i++) {
            for (TreePath childPath : getChildPaths(node)) {
                if (childPath.toString().contains(paths[i].trim())) {
                    if (i != paths.length - 1) {
                        node = childPath;
                        expandPath(node);
                        break;
                    } else {
                        selectPath(childPath);
                        return;
                    }
                }
            }
        }
    }
}

package org.cv.test.diff;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by DKulinich on 10/22/2017.
 */
public class ChainLink {
    enum Side { NONE, LEFT, RIGHT }
    private ChainLink left, right;

    public void append(ChainLink rightPart) {
        if (this.right != null)
            throw new IllegalStateException("Link is already connected.");

        this.right = rightPart;
        rightPart.left = this;
    }

    public Side longerSide() {
        if(left == null && right == null) {
            return Side.NONE;
        }
        int leftNdsCnt = countLeftNodes(this);
        int rghtndsCnt = countRightNodes(this);
        if(leftNdsCnt == rghtndsCnt) {
            return Side.NONE;
        }
        if(leftNdsCnt > rghtndsCnt) {
            return Side.LEFT;
        }
        return Side.RIGHT;
    }

    private int countLeftNodes(ChainLink link) {
        if(link.left == null) {
            return 0;
        }
        return 1 + countLeftNodes(link.left);
    }

    private int countRightNodes(ChainLink link) {
        if(link.right == null) {
            return 0;
        }
        return 1 + countRightNodes(link.right);
    }

    public static void main(String[] args) {
        ChainLink left = new ChainLink();
        ChainLink middle = new ChainLink();
        ChainLink right = new ChainLink();
        left.append(middle);
        middle.append(right);

        System.out.println(left.longerSide());
    }

}

package BlockData;

/*
 This class is for declaring the block's data. Various block classes will inherit this abstract class.
 */

import java.awt.*;

public abstract class Block {
    protected int xPixel;
    protected int yPixel;

    public Block(int i, int j) {
        this.xPixel = i;
        this.yPixel = j;
    }

    public int getxPixel() {
        return xPixel;
    }

    public int getyPixel() {
        return yPixel;
    }

    public int getNumber() { return -1; }

    abstract public void MouseClick() throws AWTException;
}


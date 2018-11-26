package BlockData;

/*
 This class is for declaring the block's data. Various block classes will inherit this abstract class.
 */

public abstract class Block {
    protected int number;
    protected int xPixel;
    protected int yPixel;

    public Block(int i, int j, int number) {
        this.xPixel = i;
        this.yPixel = j;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    abstract void MouseClick(int i, int j);
}
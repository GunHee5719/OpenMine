package BlockData;

/*
 This class is for declaring the block's data. Various block classes will inherit this abstract class.
 */

public abstract class Block {
    private int number;
    private int i;
    private int j;

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    abstract void MouseClick(int i, int j);

    public Block(int i, int j, int number) {
        this.i = i;
        this.j = j;
        this.number = number;
    }
}

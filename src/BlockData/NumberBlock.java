package BlockData;

import static MineSweep.Main.completeMineSweep;

public class NumberBlock extends Block {

    @Override
    void MouseClick(int i, int j) {
        if (this.getNumber() == completeMineSweep){

        }
    }

    public NumberBlock(int i, int j, int number) {
        super(i, j, number);
    }
}

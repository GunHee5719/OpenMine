package BlockData;

import static MineSweep.Main.minePosition;

public class MineBlock extends Block {
    @Override
    void MouseClick(int i, int j) {
        if (this.getNumber() == minePosition){

        }
    }

    public MineBlock(int i, int j, int number) {
        super(i, j, number);
    }
}

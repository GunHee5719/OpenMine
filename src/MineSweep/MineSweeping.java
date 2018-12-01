package MineSweep;

import java.awt.*;

public class MineSweeping {

    // define variable to need
    // coordinate mapping with window screen
    private BlockStruct block_map;

    public MineSweeping(BlockStruct block_map){
        this.block_map = block_map;
    }

    public void mineSweeping() throws AWTException {
        boolean gameEndFlag = true;

        while (gameEndFlag){

            // Window update and block map update
            // check game end state screen

            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 30; j++){
                    this.block_map.findMine(i, j);
                }
            }

            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 30; j++){
                    this.block_map.check121(i, j);
                    this.block_map.check1221(i, j);
                    this.block_map.check111(i, j);
                    this.block_map.check1111(i, j);
                }
            }

            this.block_map.randomClickNoneBlock();
        }

    }
}

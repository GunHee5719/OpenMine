package MineSweep;

import BlockData.Block;
import BlockData.CompleteBlock;
import BlockData.NumberBlock;
import ImageProcessing.WindowDetect;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MineSweeping {

    // define variable to need
    // coordinate mapping with window screen
    private BlockStruct block_map;

    public MineSweeping(BlockStruct block_map){
        this.block_map = block_map;
    }

    public void mineSweeping() throws AWTException {
        boolean gameEndFlag = true;

        WindowDetect wd = new WindowDetect();
        wd.initWindowDetect();
        block_map.init(wd);

        int check = 0;
        while (gameEndFlag){
            // Window update and block map update
            // check game end state screen
            HashMap<Point, Integer> changedBlocks = wd.refresh();
            for (Map.Entry<Point, Integer> entry : changedBlocks.entrySet()) {
                int x = entry.getKey().x;
                int y = entry.getKey().y;
                int state = entry.getValue();

                if(state == 0){
                    Point pixelLocation = wd.getBlockPosition(x,y);
                    Block temp = new CompleteBlock(pixelLocation.y,pixelLocation.x);
                    block_map.setBlock(y,x,temp);
                    block_map.setBuffer(y, x, state);
                }
                else if(state >0 && state < 9){
                    Point pixelLocation = wd.getBlockPosition(x,y);
                    Block temp = new NumberBlock(pixelLocation.y,pixelLocation.x,state);
                    block_map.setBlock(y,x,temp);
                    block_map.setBuffer(y,x,state);
                }
            }

            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 30; j++){
                    this.block_map.findMine(i, j);
                }
            }
            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 30; j++){
//                    this.block_map.check121(i, j);
//                    this.block_map.check1221(i, j);
//                    this.block_map.check111(i, j);
//                    this.block_map.check1111(i, j);
                }
            }

            int isChange = 0;

            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 30; j++){
                    isChange += this.block_map.calcBuffer(i, j);
                }
            }

            System.out.println("@@@@ isChange : "+isChange);
            if (isChange == 0) this.block_map.randomClickNoneBlock();

            if (check == 2){
                gameEndFlag = false;
            }
            check++;
        }
    }
}

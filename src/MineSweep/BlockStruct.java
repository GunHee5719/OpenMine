package MineSweep;

import BlockData.Block;
import BlockData.NoneBlock;

import java.util.ArrayList;

import static MineSweep.Main.nonePosition;

public class BlockStruct {
    private volatile static BlockStruct instance = null;
    private Block[][] blocks;
    private int[][] buffers;

    private BlockStruct(){
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                blocks[i][j] = new NoneBlock(i, j, nonePosition);
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                buffers[i][j] = nonePosition;
            }
        }
    }

    public static BlockStruct getInstance(){
        if (instance == null){
            synchronized (BlockStruct.class){
                if (instance == null){
                    instance = new BlockStruct();
                }
            }
        }

        return instance;
    }

    public void init(){
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                blocks[i][j] = new NoneBlock(i, j, nonePosition);
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                buffers[i][j] = nonePosition;
            }
        }
    }

    public Block getBlock(int i, int j) {
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return null;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return null;
        }

        return blocks[i][j];
    }
    public int getBuffer(int i, int j) {
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return Main.Error;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return Main.Error;
        }
        return buffers[i][j];
    }
    public void setBlock(int i, int j, Block block){
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return;
        }

        this.blocks[i][j] = block;
    }
    public void setBuffer(int i, int j, int buffer){
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return;
        }

        this.buffers[i][j] = buffer;
    }

    // check noneBlock and this pattern
    // return mine of position
    public ArrayList<Block> check121(int i, int j){
        return null;
    }
    public ArrayList<Block> check1221(int i, int j){
        return null;
    }
    public ArrayList<Block> check111(int i, int j){
        return null;
    }
    public ArrayList<Block> check1111(int i, int j){
        return null;
    }
}

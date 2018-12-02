package MineSweep;

import BlockData.Block;
import BlockData.CompleteBlock;
import BlockData.MineBlock;
import BlockData.NoneBlock;
import ImageProcessing.WindowDetect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static MineSweep.Main.*;

public class BlockStruct {
    private Block[][] blocks = new Block[16][30];
    private int[][] buffers = new int[16][30];
    private boolean[][] check_buffers = new boolean[16][30];

    public BlockStruct(){
//        for (int i = 0; i < 16; i++){
//            for (int j = 0; j < 30; j++){
//                blocks[i][j] = new NoneBlock(i, j);
//            }
//        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                buffers[i][j] = nonePosition;
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                check_buffers[i][j] = false;
            }
        }
    }

    public void init(WindowDetect wd){
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                Point pixelLocation = wd.getBlockPosition(j,i);
                blocks[i][j] = new NoneBlock(pixelLocation.x, pixelLocation.y);
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                buffers[i][j] = nonePosition;
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                check_buffers[i][j] = false;
            }
        }
    }

    public Block getBlock(int i, int j) {
        if (i < 0 || i > 15){
            System.out.println("BlockStruct getBlock out of Index: i");
            return null;
        }
        if (j < 0 || j > 29){
            System.out.println("BlockStruct getBlock out of Index: j");
            return null;
        }

        return blocks[i][j];
    }
    public int getBuffer(int i, int j) {
        if (i < 0 || i > 15){
            System.out.println("BlockStruct getBlock out of Index: i");
            return Main.Error;
        }
        if (j < 0 || j > 29){
            System.out.println("BlockStruct getBlock out of Index: j");
            return Main.Error;
        }
        return buffers[i][j];
    }
    public void setBlock(int i, int j, Block block){
        if (i < 0 || i > 15){
            System.out.println("BlockStruct getBlock out of Index: i");
            return;
        }
        if (j < 0 || j > 29){
            System.out.println("BlockStruct getBlock out of Index: j");
            return;
        }

        this.blocks[i][j] = block;
    }
    public void setBuffer(int i, int j, int buffer){
        if (i < 0 || i > 15){
            System.out.println("BlockStruct getBlock out of Index: i");
            return;
        }
        if (j < 0 || j > 29){
            System.out.println("BlockStruct getBlock out of Index: j");
            return;
        }

        this.buffers[i][j] = buffer;
    }

    public boolean isComplete(int i, int j){
        int mine = 0;

        for (int x = i - 1; x < i + 2; x++){
            for (int y = j - 1; y < j + 2; y++){
                if (x < 0 || x > 15) continue;
                if (y < 0 || y > 29) continue;
                if (x == i && y == j) continue;

                if (buffers[x][y] == minePosition) mine++;
            }
        }

        if (buffers[i][j] == blocks[i][j].getNumber() - mine) return true;
        else return false;
    }

    // set buffers after finding mine
    // i, j -> mine of position
    public int calcBuffer(int i, int j){
        for (int x = i - 1; x < i + 2; x++){
            for (int y = j - 1; y < j + 2; y++){
                if (x < 0 || x > 15) continue;
                if (y < 0 || y > 29) continue;
                if (x == i && y == j) continue;

                if (buffers[x][y] != nonePosition && buffers[x][y] != minePosition && buffers[x][y] != completeMineSweep){
                    if (!isComplete(x, y)){
                        buffers[x][y]--;
                    }
                }
            }
        }

        return 1;
    }

    public int checkNotMinePosition(int i, int j) throws AWTException {
        if (buffers[i][j] != 1) return 0;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;

        int returnValue = 0;

        if (i <= 14 && buffers[i + 1][j] == 1){
            direction = downDirection;
            i_list.add(i);
            i_list.add(i + 1);
            j_list.add(j);
            j_list.add(j);

            if (checkCompleteBlock(i_list, j_list, direction) == 1){
                if ((i + 2) <= 15 && (i - 1) < 0 && buffers[i + 2][j + 1] == nonePosition){
                    blocks[i + 2][j + 1].MouseClick();
                    returnValue = 1;
                } else if ((i + 2) <= 15 && (i - 1) >= 0 && ((buffers[i - 1][j + 1] > 0 && buffers[i - 1][j + 1] < 9))
                        && buffers[i + 2][j + 1] == nonePosition){
                    blocks[i + 2][j + 1].MouseClick();
                    returnValue = 1;
                } else if ((i - 1) >= 0 && (i + 2) > 15 && buffers[i - 1][j + 1] == nonePosition){
                    blocks[i - 1][j + 1].MouseClick();
                    returnValue = 1;
                } else if ((i - 1) >= 0 && (i + 2) <= 15 && ((buffers[i + 2][j + 1] > 0 && buffers[i + 2][j + 1] < 9))
                        && buffers[i - 1][j + 1] == nonePosition){
                    blocks[i - 1][j + 1].MouseClick();
                    returnValue = 1;
                }
            } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                if ((i + 2) <= 15 && (i - 1) < 0 && buffers[i + 2][j - 1] == nonePosition){
                    blocks[i + 2][j - 1].MouseClick();
                    returnValue = 1;
                } else if ((i + 2) <= 15 && (i - 1) >= 0 && ((buffers[i - 1][j - 1] > 0 && buffers[i - 1][j - 1] < 9))
                        && buffers[i + 2][j - 1] == nonePosition){
                    blocks[i + 2][j - 1].MouseClick();
                    returnValue = 1;
                } else if ((i - 1) >= 0 && (i + 2) > 15 && buffers[i - 1][j - 1] == nonePosition){
                    blocks[i - 1][j - 1].MouseClick();
                    returnValue = 1;
                } else if ((i - 1) >= 0 && (i + 2) <= 15 && ((buffers[i + 2][j - 1] > 0 && buffers[i + 2][j - 1] < 9))
                        && buffers[i - 1][j - 1] == nonePosition){
                    blocks[i - 1][j - 1].MouseClick();
                    returnValue = 1;
                }
            }
        } else if (j <= 28 && buffers[i][j + 1] == 1){
            direction = rightDirection;
            i_list.add(i);
            i_list.add(i);
            j_list.add(j);
            j_list.add(j + 1);

            if (checkCompleteBlock(i_list, j_list, direction) == 1){
                if ((j + 2) <= 29 && (j - 1) < 0 && buffers[i + 1][j + 2] == nonePosition){
                    blocks[i + 1][j + 2].MouseClick();
                    returnValue = 1;
                } else if ((j + 2) <= 29 && (j - 1) >= 0 && ((buffers[i + 1][j - 1] > 0 && buffers[i + 1][j - 1] < 9))
                        && buffers[i + 1][j + 2] == nonePosition){
                    blocks[i + 1][j + 2].MouseClick();
                    returnValue = 1;
                } else if ((j - 1) >= 0 && (j + 2) > 29 && buffers[i + 1][j - 1] == nonePosition){
                    blocks[i + 1][j - 1].MouseClick();
                    returnValue = 1;
                } else if ((j - 1) >= 0 && (j + 2) <= 29 && ((buffers[i + 1][j + 2] > 0 && buffers[i + 1][j + 2] < 9))
                        && buffers[i + 1][j - 1] == nonePosition){
                    blocks[i + 1][j - 1].MouseClick();
                    returnValue = 1;
                }
            } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                if ((j + 2) <= 29 && (j - 1) < 0 && buffers[i - 1][j + 2] == nonePosition){
                    blocks[i - 1][j + 2].MouseClick();
                    returnValue = 1;
                } else if ((j + 2) <= 29 && (j - 1) >= 0 && ((buffers[i - 1][j - 1] > 0 && buffers[i - 1][j - 1] < 9))
                        && buffers[i - 1][j + 2] == nonePosition){
                    blocks[i - 1][j + 2].MouseClick();
                    returnValue = 1;
                } else if ((j - 1) >= 0 && (j + 2) > 29 && buffers[i - 1][j - 1] == nonePosition){
                    blocks[i - 1][j - 1].MouseClick();
                    returnValue = 1;
                } else if ((j - 1) >= 0 && (j + 2) <= 29 && ((buffers[i - 1][j + 2] > 0 && buffers[i - 1][j + 2] < 9))
                        && buffers[i - 1][j - 1] == nonePosition){
                    blocks[i - 1][j - 1].MouseClick();
                    returnValue = 1;
                }
            }
        }

        return returnValue;
    }

    public int findMine(int i, int j) throws AWTException {
        if (buffers[i][j] == minePosition) return 0;
        if (buffers[i][j] == nonePosition) return 0;
        if (buffers[i][j] == completeMineSweep) return 0;

        ArrayList<Integer> click_x = new ArrayList<>();
        ArrayList<Integer> click_y = new ArrayList<>();
        int currentBuffer = blocks[i][j].getNumber();
        int mine = 0;
        int remainPosition = 0;
        int returnValue = 0;

        for (int x = i - 1; x < i + 2; x++){
            for (int y = j - 1; y < j + 2; y++) {
                if (x < 0 || x > 15) continue;
                if (y < 0 || y > 29) continue;
                if (x == i && y == j) continue;

                if (buffers[x][y] == minePosition) {
                    mine++;
                }
            }
        }

        for (int x = i - 1; x < i + 2; x++){
            for (int y = j - 1; y < j + 2; y++) {
                if (x < 0 || x > 15) continue;
                if (y < 0 || y > 29) continue;
                if (x == i && y == j) continue;

                if (mine != currentBuffer) {
                    if (buffers[x][y] == nonePosition) {
                        remainPosition++;
                        click_x.add(x);
                        click_y.add(y);
                    }
                    if (buffers[x][y] == minePosition) {
                        remainPosition++;
                    }
                }
            }
        }

        if (click_x.size() != 0 && currentBuffer == remainPosition){
            // click right button of mouse

            for (int x = 0; x < click_x.size(); x++){
                if (buffers[click_x.get(x)][click_y.get(x)] != minePosition) {
                    buffers[click_x.get(x)][click_y.get(x)] = minePosition;
                    this.calcBuffer(click_x.get(x), click_y.get(x));
                    MineBlock mineBlock = new MineBlock(blocks[click_x.get(x)][click_y.get(x)].getxPixel(), blocks[click_x.get(x)][click_y.get(x)].getyPixel());
                    blocks[click_x.get(x)][click_y.get(x)] = mineBlock;
                    blocks[click_x.get(x)][click_y.get(x)].MouseClick();

                    returnValue = 1;

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return returnValue;
    }

    public int clickCompleteBlock(int i, int j) throws AWTException {
        int returnValue = 0;
        if (buffers[i][j] == minePosition) return returnValue;
        if (buffers[i][j] == nonePosition) return returnValue;

        if (blocks[i][j].getNumber() != -1 && buffers[i][j] == 0){
            buffers[i][j] = completeMineSweep;
            blocks[i][j].MouseClick();
            CompleteBlock completeBlock = new CompleteBlock(blocks[i][j].getxPixel(), blocks[i][j].getyPixel());
            blocks[i][j] = completeBlock;

            returnValue = 1;

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }



    // check noneBlock and this pattern
    // All click mine of position in functions
    public int check121(int i, int j) throws AWTException {
        if (buffers[i][j] != 1) return 0;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;
        int returnValue = 0;

        if (i <= 13 && buffers[i + 1][j] == 2){
            direction = downDirection;
            if (buffers[i + 2][j] == 1){
                i_list.add(i);
                i_list.add(i + 1);
                i_list.add(i + 2);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((j + 1) < 30 && (buffers[i][j + 1] == minePosition || buffers[i][j + 1] == nonePosition)
                            && (buffers[i + 2][j + 1] == minePosition || buffers[i + 2][j + 1] == nonePosition)){

                        // click mouse i j+1, i+2 j+1
                        buffers[i][j + 1] = minePosition;
                        this.calcBuffer(i, j + 1);
                        MineBlock mineBlock1 = new MineBlock(blocks[i][j + 1].getxPixel(), blocks[i][j + 1].getyPixel());
                        blocks[i][j + 1] = mineBlock1;
                        blocks[i][j + 1].MouseClick();

                        buffers[i + 2][j + 1] = minePosition;
                        this.calcBuffer(i + 2, j + 1);
                        MineBlock mineBlock2 = new MineBlock(blocks[i + 2][j + 1].getxPixel(), blocks[i + 2][j + 1].getyPixel());
                        blocks[i + 2][j + 1] = mineBlock2;
                        blocks[i + 2][j + 1].MouseClick();

                        returnValue = 1;
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((j - 1) >= 0 && (buffers[i][j - 1] == minePosition || buffers[i][j - 1] == nonePosition)
                            && (buffers[i + 2][j - 1] == minePosition || buffers[i + 2][j - 1] == nonePosition)){

                        // click mouse i j-1, i+2 j-1
                        buffers[i][j - 1] = minePosition;
                        this.calcBuffer(i, j - 1);
                        MineBlock mineBlock1 = new MineBlock(blocks[i][j - 1].getxPixel(), blocks[i][j - 1].getyPixel());
                        blocks[i][j - 1] = mineBlock1;
                        blocks[i][j - 1].MouseClick();

                        buffers[i + 2][j - 1] = minePosition;
                        this.calcBuffer(i + 2, j - 1);
                        MineBlock mineBlock2 = new MineBlock(blocks[i + 2][j - 1].getxPixel(), blocks[i + 2][j - 1].getyPixel());
                        blocks[i + 2][j - 1] = mineBlock2;
                        blocks[i + 2][j - 1].MouseClick();

                        returnValue = 1;
                    }
                }
            }
        } else if (j <= 27 && buffers[i][j + 1] == 2){
            direction = rightDirection;
            if (buffers[i][j + 2] == 1){
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                j_list.add(j);
                j_list.add(j + 1);
                j_list.add(j + 2);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((i + 1) < 16 && (buffers[i + 1][j] == minePosition || buffers[i + 1][j] == nonePosition)
                            && (buffers[i + 1][j + 2] == minePosition || buffers[i + 1][j + 2] == nonePosition)){

                        // click mouse i+1 j, i+1 j+2
                        buffers[i + 1][j] = minePosition;
                        this.calcBuffer(i + 1, j);
                        MineBlock mineBlock1 = new MineBlock(blocks[i + 1][j].getxPixel(), blocks[i + 1][j].getyPixel());
                        blocks[i + 1][j] = mineBlock1;
                        blocks[i + 1][j].MouseClick();

                        buffers[i + 1][j + 2] = minePosition;
                        this.calcBuffer(i + 1, j + 2);
                        MineBlock mineBlock2 = new MineBlock(blocks[i + 1][j + 2].getxPixel(), blocks[i + 1][j + 2].getyPixel());
                        blocks[i + 1][j + 2] = mineBlock2;
                        blocks[i + 1][j + 2].MouseClick();

                        returnValue = 1;
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((i - 1) >= 0 && (buffers[i - 1][j] == minePosition || buffers[i - 1][j] == nonePosition)
                            && (buffers[i - 1][j + 2] == minePosition || buffers[i - 1][j + 2] == nonePosition)){

                        // click mouse i-1 j, i-1 j+2
                        buffers[i - 1][j] = minePosition;
                        this.calcBuffer(i - 1, j);
                        MineBlock mineBlock1 = new MineBlock(blocks[i - 1][j].getxPixel(), blocks[i - 1][j].getyPixel());
                        blocks[i - 1][j] = mineBlock1;
                        blocks[i - 1][j].MouseClick();

                        buffers[i - 1][j + 2] = minePosition;
                        this.calcBuffer(i - 1, j + 2);
                        MineBlock mineBlock2 = new MineBlock(blocks[i - 1][j + 2].getxPixel(), blocks[i - 1][j + 2].getyPixel());
                        blocks[i - 1][j + 2] = mineBlock2;
                        blocks[i - 1][j + 2].MouseClick();

                        returnValue = 1;
                    }
                }
            }
        }

        return returnValue;
    }
    public int check1221(int i, int j) throws AWTException {
        if (buffers[i][j] != 1) return 0;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;
        int returnValue = 0;

        if (i <= 12 && buffers[i + 1][j] == 2 && buffers[i + 2][j] == 2){
            direction = downDirection;
            if (buffers[i + 3][j] == 1){
                i_list.add(i);
                i_list.add(i + 1);
                i_list.add(i + 2);
                i_list.add(i + 3);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((j + 1) < 30 && (buffers[i + 1][j + 1] == minePosition || buffers[i + 1][j + 1] == nonePosition)
                            && (buffers[i + 2][j + 1] == minePosition || buffers[i + 2][j + 1] == nonePosition)){

                        // click mouse i+1 j+1, i+2 j+1
                        buffers[i + 1][j + 1] = minePosition;
                        this.calcBuffer(i + 1, j + 1);
                        MineBlock mineBlock1 = new MineBlock(blocks[i + 1][j + 1].getxPixel(), blocks[i + 1][j + 1].getyPixel());
                        blocks[i + 1][j + 1] = mineBlock1;
                        blocks[i + 1][j + 1].MouseClick();

                        buffers[i + 2][j + 1] = minePosition;
                        this.calcBuffer(i + 2, j + 1);
                        MineBlock mineBlock2 = new MineBlock(blocks[i + 2][j + 1].getxPixel(), blocks[i + 2][j + 1].getyPixel());
                        blocks[i + 2][j + 1] = mineBlock2;
                        blocks[i + 2][j + 1].MouseClick();

                        returnValue = 1;
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((j - 1) >= 0 && (buffers[i + 1][j - 1] == minePosition || buffers[i + 1][j - 1] == nonePosition)
                            && (buffers[i + 2][j - 1] == minePosition || buffers[i + 2][j - 1] == nonePosition)){

                        // click mouse i+1 j-1, i+2 j-1
                        buffers[i + 1][j - 1] = minePosition;
                        this.calcBuffer(i + 1, j - 1);
                        MineBlock mineBlock1 = new MineBlock(blocks[i + 1][j - 1].getxPixel(), blocks[i + 1][j - 1].getyPixel());
                        blocks[i + 1][j - 1] = mineBlock1;
                        blocks[i + 1][j - 1].MouseClick();

                        buffers[i + 2][j - 1] = minePosition;
                        this.calcBuffer(i + 2, j - 1);
                        MineBlock mineBlock2 = new MineBlock(blocks[i + 2][j - 1].getxPixel(), blocks[i + 2][j - 1].getyPixel());
                        blocks[i + 2][j - 1] = mineBlock2;
                        blocks[i + 2][j - 1].MouseClick();

                        returnValue = 1;
                    }
                }
            }
        } else if (j <= 26 && buffers[i][j + 1] == 2 && buffers[i][j + 2] == 2){
            direction = rightDirection;
            if (buffers[i][j + 3] == 1){
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                j_list.add(j);
                j_list.add(j + 1);
                j_list.add(j + 2);
                j_list.add(j + 3);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((i + 1) < 16 && (buffers[i + 1][j + 1] == minePosition || buffers[i + 1][j + 1] == nonePosition)
                            && (buffers[i + 1][j + 2] == minePosition || buffers[i + 1][j + 2] == nonePosition)){

                        // click mouse i+1 j+1, i+1 j+2
                        buffers[i + 1][j + 1] = minePosition;
                        this.calcBuffer(i + 1, j + 1);
                        MineBlock mineBlock1 = new MineBlock(blocks[i + 1][j + 1].getxPixel(), blocks[i + 1][j + 1].getyPixel());
                        blocks[i + 1][j + 1] = mineBlock1;
                        blocks[i + 1][j + 1].MouseClick();

                        buffers[i + 1][j + 2] = minePosition;
                        this.calcBuffer(i + 1, j + 2);
                        MineBlock mineBlock2 = new MineBlock(blocks[i + 1][j + 2].getxPixel(), blocks[i + 1][j + 2].getyPixel());
                        blocks[i + 1][j + 2] = mineBlock2;
                        blocks[i + 1][j + 2].MouseClick();

                        returnValue = 1;
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((i - 1) >= 0 && (buffers[i - 1][j + 1] == minePosition || buffers[i - 1][j + 1] == nonePosition)
                            && (buffers[i - 1][j + 2] == minePosition || buffers[i - 1][j + 2] == nonePosition)){

                        // click mouse i-1 j+1, i-1 j+2
                        buffers[i - 1][j + 1] = minePosition;
                        this.calcBuffer(i - 1, j + 1);
                        MineBlock mineBlock1 = new MineBlock(blocks[i - 1][j + 1].getxPixel(), blocks[i - 1][j + 1].getyPixel());
                        blocks[i - 1][j + 1] = mineBlock1;
                        blocks[i - 1][j + 1].MouseClick();

                        buffers[i - 1][j + 2] = minePosition;
                        this.calcBuffer(i - 1, j + 2);
                        MineBlock mineBlock2 = new MineBlock(blocks[i - 1][j + 2].getxPixel(), blocks[i - 1][j + 2].getyPixel());
                        blocks[i - 1][j + 2] = mineBlock2;
                        blocks[i - 1][j + 2].MouseClick();

                        returnValue = 1;
                    }
                }
            }
        }

        return returnValue;
    }

    public void randomClickNoneBlock() throws AWTException {
        boolean clickFlag = true;

        while (clickFlag){
            Random rand_x = new Random();
            Random rand_y = new Random();

            int x = rand_x.nextInt(16);
            int y = rand_y.nextInt(30);

            if (buffers[x][y] == nonePosition){
                blocks[x][y].MouseClick();
                clickFlag = false;
            }
        }
    }


    // return 0 : not exist completeblock list
    // return 1 : completeblock list exist at -1
    // return 2 : completeblock list exist at 1
    protected int checkCompleteBlock(ArrayList<Integer> i_list, ArrayList<Integer> j_list, int direction){
        int boundary = 0;

        if (direction == downDirection){
            for (int x = 0; x < i_list.size(); x++){
                if (x == 0){
                    if (buffers[i_list.get(x)][j_list.get(x) + 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) + 1] <= 9){
                        boundary = 1;
                    } else if (buffers[i_list.get(x)][j_list.get(x) - 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) - 1] <= 9){
                        boundary = -1;
                    }
                } else {
                    if (boundary == 0) break;
                    else if (boundary == 1){
                        if (buffers[i_list.get(x)][j_list.get(x) + 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) + 1] <= 9){

                        } else {
                            return 0;
                        }
                    } else {
                        if (buffers[i_list.get(x)][j_list.get(x) - 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) - 1] <= 9){

                        } else {
                            return 0;
                        }
                    }
                }
            }

            if (boundary == -1){
                return 1;
            } else if (boundary == 1) {
                return 2;
            }
        } else if (direction == rightDirection) {
            for (int y = 0; y < j_list.size(); y++){
                if (y == 0){
                    if (buffers[i_list.get(y) + 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) + 1][j_list.get(y)] <= 9){
                        boundary = 1;
                    } else if (buffers[i_list.get(y) - 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) - 1][j_list.get(y)] <= 9){
                        boundary = -1;
                    }
                } else {
                    if (boundary == 0) break;
                    else if (boundary == 1){
                        if (buffers[i_list.get(y) + 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) + 1][j_list.get(y)] <= 9){

                        } else {
                            return 0;
                        }
                    } else {
                        if (buffers[i_list.get(y) - 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) - 1][j_list.get(y)] <= 9){

                        } else {
                            return 0;
                        }
                    }
                }
            }

            if (boundary == -1){
                return 1;
            } else if (boundary == 1) {
                return 2;
            }
        }


        return 0;
    }
}

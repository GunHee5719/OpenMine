package MineSweep;

import BlockData.Block;
import BlockData.CompleteBlock;
import BlockData.NoneBlock;

import java.awt.*;

/*
    This class is just for start the program. Program will be started by calling MineSweeping Class.
*/
public class Main {
    public static int minePosition = 9;
    public static int completeMineSweep = 0;
    public static int nonePosition = -1;
    public static int Error = -2;
    public static int downDirection = 10;
    public static int rightDirection = 20;
  
    public static void main(String[] args) throws AWTException {
        Block a = new CompleteBlock(1700,10);
        Block b = new NoneBlock(1800,10);
        ((NoneBlock) b).MouseClick();
        ((NoneBlock) b).MouseClick();
    }
}

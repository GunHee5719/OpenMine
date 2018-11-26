package MineSweep;

import ImageProcessing.OpenMineGUI;

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
  
    public static void main(String[] args) {
        OpenMineGUI start = new OpenMineGUI();
    }
}

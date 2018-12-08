package ImageProcessing;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseMove {
    private Robot rob;
    public MouseMove() throws AWTException {
        rob = new Robot();
    }

    public void leftClick(int i, int j){
        rob.mouseMove(i, j);
        rob.mousePress(InputEvent.BUTTON1_MASK);
        rob.mouseRelease(InputEvent.BUTTON1_MASK);
        rob.delay(20);
    }

    public void rightClick(int i,int j){
        rob.mouseMove(i, j);
        rob.mousePress(InputEvent.BUTTON3_MASK);
        rob.mouseRelease(InputEvent.BUTTON3_MASK);
        rob.delay(20);
    }

    public void allClick(int i, int j){
        rob.mouseMove(i, j);
        rob.mousePress(InputEvent.BUTTON1_MASK);
        rob.mousePress(InputEvent.BUTTON3_MASK);
        rob.mouseRelease(InputEvent.BUTTON1_MASK);
        rob.mouseRelease(InputEvent.BUTTON3_MASK);
        rob.delay(20);
    }
}


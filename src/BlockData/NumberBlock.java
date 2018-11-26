package BlockData;

import ImageProcessing.MouseMove;

import java.awt.*;

public class NumberBlock extends Block {
    private int number;

    public NumberBlock(int i, int j, int number) {
        super(i, j);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void MouseClick() throws AWTException {
        // 양쪽 클릭
        MouseMove mm = new MouseMove();
        mm.allClick(xPixel, yPixel);
    }
}

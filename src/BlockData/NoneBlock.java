package BlockData;

import ImageProcessing.MouseMove;

import java.awt.*;

public class NoneBlock extends Block {
    public NoneBlock(int i, int j) {
        super(i, j);
    }

    @Override
    public void MouseClick() throws AWTException {
        MouseMove mm = new MouseMove();
        mm.leftClick(xPixel, yPixel);
    }
}

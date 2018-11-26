package BlockData;

import ImageProcessing.MouseMove;

import java.awt.*;

public class MineBlock extends Block {
    public MineBlock(int i, int j) {
        super(i, j);
    }

    @Override
    public void MouseClick() throws AWTException {
        // 우클릭
        MouseMove mm = new MouseMove();
        mm.rightClick(xPixel, yPixel);
    }


}

package ImageProcessing;

import MineSweep.BlockStruct;
import MineSweep.MineSweeping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenMineGUI implements ActionListener{
    public OpenMineGUI(){
        JFrame frame = new JFrame("OpenMine");
        Button button = new Button("Start");

        frame.add(button);

        frame.setSize(200,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocation(0,0);
        frame.setVisible(true);

        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // 화면 인식하는 방향으로 이어줘야함. 이전에 레이아웃 위치 지정도 다시 해야 함!
//        Block testBlock = new NoneBlock(1600,10);
//        try {
//            testBlock.MouseClick();
//        } catch (AWTException e1) {
//            e1.printStackTrace();
//        }

        BlockStruct bs = new BlockStruct();
        MineSweeping start = new MineSweeping(bs);
        try {
            start.mineSweeping();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }
}

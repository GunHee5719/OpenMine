package ImageProcessing;

import BlockData.Block;
import BlockData.NoneBlock;
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

        frame.setSize(240,130);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocation(100,100);
        frame.setVisible(true);

        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // 화면 인식하는 방향으로 이어줘야함. 이전에 레이아웃 위치 지정도 다시 해야 함!
        BlockStruct bs = BlockStruct.getInstance();
        MineSweeping start = new MineSweeping(bs);
    }
}

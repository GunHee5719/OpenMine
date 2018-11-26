package ImageProcessing;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WindowDetect {

    public static void main(String[] args) {
        // Test code
        String windowName = "지뢰 찾기";

        try {
            JFrame frame = new JFrame(windowName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JLabel(new ImageIcon(ImageIO.read(new File("minesweeper_window.png")))));
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage("minesweeper_icon.png"));
            frame.pack();
            frame.setVisible(true);
            Thread.sleep(500);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedImage capturedScreen = WindowCapture.captureWindow(windowName);
        if (capturedScreen == null) {
            return;
        }

        JFrame frame = new JFrame("Captured: " + windowName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(capturedScreen)));
        frame.pack();
        frame.setVisible(true);

        List<ImageProcessing.Point> corners = ImageProcessing.HarrisCorner(capturedScreen);

        int width = capturedScreen.getWidth();
        int height = capturedScreen.getHeight();

        boolean[][] map = new boolean[width][height];
        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[] countVertical = new int[width];
        int[] countHorizontal = new int[height];

        corners.forEach((point) -> {
            map[point.x][point.y] = true;
            countVertical[point.x]++;
            countHorizontal[point.y]++;
            tmp.setRGB(point.x, point.y, 0xFFFFFF);
        });

        int[] countVerticalBlurred = new int[width];
        int paddingSize = 2;
        for (int i = 0; i < width; i++) {
            int sum = 0;
            for (int j = i - paddingSize; j <= i + paddingSize; j++) {
                if (i - paddingSize >= 0 && i + paddingSize < width) {
                    sum += countVertical[j];
                }
            }
            countVerticalBlurred[i] = sum / (paddingSize * 2 + 1);
        }


        boolean increasing = false;
        for (int i = 1; i < width; i++) {
            if (increasing && countVerticalBlurred[i] < countVerticalBlurred[i - 1]) {
                System.out.println("countVerticalBlurred[" + (i - 1) + "] = " + countVerticalBlurred[i - 1]);
                increasing = false;
            }
            else if (countVerticalBlurred[i] > countVerticalBlurred[i - 1]) {
                increasing = true;
            }
        }

        frame = new JFrame("Result: " + windowName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(tmp)));
        frame.pack();
        frame.setVisible(true);

        System.out.println(corners.size() + " corner(s) detected");
        // End of Test code
    }

}

package ImageProcessing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WindowDetect {

    private static Rectangle regionOfInterest = null;
    private static BufferedImage capturedScreen = null;
    private static int columns, rows;
    private static double blockWidth, blockHeight;
    private static String windowName = "지뢰 찾기";

    // Test code
    public static void main(String[] args) {
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

        if (initWindowDetect() != 0) {
            return;
        }

        JFrame frame = new JFrame("Captured: " + windowName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(capturedScreen)));
        frame.pack();
        frame.setVisible(true);

        BufferedImage tmp = new BufferedImage(
                capturedScreen.getColorModel(),
                capturedScreen.copyData(capturedScreen.getRaster().createCompatibleWritableRaster()),
                capturedScreen.getColorModel().isAlphaPremultiplied(),
                null); // clone capturedScreen
        ImageProcessing.drawRectangle(tmp, regionOfInterest, 0xFFFF00);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                ImageProcessing.mark(tmp, getBlockPosition(x, y), 0xFF0000);
            }
        }

        frame = new JFrame("Detected: " + windowName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(tmp)));
        frame.pack();
        frame.setVisible(true);
    }

    public static int initWindowDetect() {
        capturedScreen = WindowCapture.captureWindow(windowName);
        if (capturedScreen == null) {
            return 1;
        }

        int[][] binarized = ImageProcessing.Binarize(capturedScreen);

        List<Rectangle> rectangles = ImageProcessing.Labeling(binarized);
        int size = 0;
        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle rectangle = rectangles.get(i);
            if (size < rectangle.width * rectangle.height) {
                size = rectangle.width * rectangle.height;
                regionOfInterest = rectangle;
            }
        }

        columns = countVerticalLines(binarized);
        rows = countHorizontalLines(binarized);
        blockWidth = (double)regionOfInterest.width / columns;
        blockHeight = (double)regionOfInterest.height / rows;

        // if block is not square
        if (blockWidth / blockHeight > 1.02) {
            blockHeight = blockWidth;
            rows = (int)(regionOfInterest.height / blockHeight + 0.5); // round
            blockHeight = (double)regionOfInterest.height / rows;
        } else if (blockHeight / blockWidth > 1.02) {
            blockWidth = blockHeight;
            columns = (int)(regionOfInterest.width / blockWidth + 0.5); // round
            blockWidth = (double)regionOfInterest.width / columns;
        }

        // Debug
        System.out.println("Region of Interest: " + regionOfInterest.width + " x " + regionOfInterest.height + " pixels");
        System.out.println("columns: " + columns);
        System.out.println("rows: " + rows);
        System.out.println("block size: " + String.format("%.2f", blockWidth) + " x " + String.format("%.2f", blockHeight) + " pixels");

        return 0;
    }

    public static Point getBlockPosition(int x, int y) {
        return new Point((int)(regionOfInterest.x + blockWidth * (x + 0.5)), (int)(regionOfInterest.y + blockHeight * (y + 0.5)));
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }

    private static int countHorizontalLines(int[][] binarized) {
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        boolean isBlack = false;

        for (int x = regionOfInterest.x; x < regionOfInterest.x + regionOfInterest.width; x++) {
            isBlack = binarized[x][regionOfInterest.y] == 0;
            int count = 0;
            for (int y = regionOfInterest.y; y < regionOfInterest.y + regionOfInterest.height; y++) {
                if (isBlack && binarized[x][y] == 0xFF) {
                    count++;
                    isBlack = false;
                } else if (!isBlack && binarized[x][y] == 0) {
                    isBlack = true;
                }
            }
            if (counts.containsKey(count)) {
                counts.replace(count, counts.get(count) + 1);
            } else {
                counts.put(count, 1);
            }
        }

        int maxKey = 0;
        int maxValue = 0;
        Iterator<Integer> iterator = counts.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (maxValue < counts.get(key)) {
                maxKey = key;
                maxValue = counts.get(key);
            }
        }

        return maxKey;
    }

    private static int countVerticalLines(int[][] binarized) {
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        boolean isBlack = false;

        for (int y = regionOfInterest.y; y < regionOfInterest.y + regionOfInterest.height; y++) {
            isBlack = binarized[regionOfInterest.x][y] == 0;
            int count = 0;
            for (int x = regionOfInterest.x; x < regionOfInterest.x + regionOfInterest.width; x++) {
                if (isBlack && binarized[x][y] == 0xFF) {
                    count++;
                    isBlack = false;
                } else if (!isBlack && binarized[x][y] == 0) {
                    isBlack = true;
                }
            }
            if (counts.containsKey(count)) {
                counts.replace(count, counts.get(count) + 1);
            } else {
                counts.put(count, 1);
            }
        }

        int maxKey = 0;
        int maxValue = 0;
        Iterator<Integer> iterator = counts.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (maxValue < counts.get(key)) {
                maxKey = key;
                maxValue = counts.get(key);
            }
        }

        return maxKey;
    }

}

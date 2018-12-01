package ImageProcessing;

import java.awt.Point;
import java.awt.Rectangle;
//import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;

public class WindowDetect {

    private static Rectangle regionOfInterest = null;
    private static BufferedImage capturedScreen = null;
    private static int columns, rows;
    private static double blockWidth, blockHeight;
    private static String windowName = "지뢰 찾기";

    // Test code
    public static void main(String[] args) {
		/* create test window
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
		*/

        // initialize
        if (initWindowDetect() != 0) {
            return;
        }

        // show screen capture result
        ImageProcessing.show(capturedScreen, "Captured: " + windowName);

        // show processing result
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

        ImageProcessing.show(tmp, "Detected: " + windowName);

        // refresh
        while (true) {
            try {
                // wait for enter key
                System.out.println("Press Enter to refresh...");
                System.in.read();
                System.in.read();
            } catch (IOException e) {
            }
            System.out.println("Refresh");

			/* create test window
	        try {
	            JFrame frame = new JFrame(windowName);
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new JLabel(new ImageIcon(ImageIO.read(new File("test.png")))));
		        frame.pack();
		        frame.setVisible(true);
		        Thread.sleep(500);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        */

            HashMap<Point, Integer> changedBlocks = refresh();

            // draw detected rectangles
            tmp = new BufferedImage(
                    capturedScreen.getColorModel(),
                    capturedScreen.copyData(capturedScreen.getRaster().createCompatibleWritableRaster()),
                    capturedScreen.getColorModel().isAlphaPremultiplied(),
                    null); // clone capturedScreen

            final int colors[] = {0x880015, 0xED1C24, 0xFF7F27, 0xFFF200, 0x22B14C, 0x00A2E8, 0x3F48CC, 0xA349A4, 0xFFFFFF, 0x7F7F7F};
            for (Map.Entry<Point, Integer> entry : changedBlocks.entrySet()) {
                int color = colors[entry.getValue()];

                Rectangle targetRectangle = getBlockRectangle(entry.getKey());
                targetRectangle.x += 2;
                targetRectangle.y += 2;
                targetRectangle.width -= 4;
                targetRectangle.height -= 4;
                ImageProcessing.drawRectangle(tmp, targetRectangle, color);

                // additional rectangle
                targetRectangle.x += 1;
                targetRectangle.y += 1;
                targetRectangle.width -= 2;
                targetRectangle.height -= 2;
                ImageProcessing.drawRectangle(tmp, targetRectangle, color);
            }

            ImageProcessing.show(tmp, "ReCaptured: " + windowName);
            System.out.println(changedBlocks.size() + " blocks has been changed");
        }
    }

    public static int initWindowDetect() {
        // capture initial window
        capturedScreen = WindowCapture.captureWindow(windowName);
        if (capturedScreen == null) {
            return -1;
        }

        // binarize image
        int[][] binarized = ImageProcessing.Binarize(capturedScreen);

        // select biggest rectangle in labeled rectangles as a region of interest
        List<Rectangle> rectangles = ImageProcessing.Labeling(binarized);
        int size = 0;
        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle rectangle = rectangles.get(i);
            if (size < rectangle.width * rectangle.height) {
                size = rectangle.width * rectangle.height;
                regionOfInterest = rectangle;
            }
        }

        // cut and add padding in the binarized region of interest
        int[][] binarizedRegionOfInterest = new int[regionOfInterest.width + 2][regionOfInterest.height + 2];
        Arrays.fill(binarizedRegionOfInterest[0], 0xFF);
        for (int x = 0; x < regionOfInterest.width; x++) {
            binarizedRegionOfInterest[x + 1][0] = 0xFF;
            System.arraycopy(binarized[regionOfInterest.x + x], regionOfInterest.y, binarizedRegionOfInterest[x + 1], 1, regionOfInterest.height);
            binarizedRegionOfInterest[x + 1][regionOfInterest.height + 1] = 0xFF;
        }
        Arrays.fill(binarizedRegionOfInterest[regionOfInterest.width + 1], 0xFF);

        // count number of blocks and adjust Zone of Interest
        analyzeZoneOfInterest(binarizedRegionOfInterest);

        // calculate a block size
        blockWidth = (double)regionOfInterest.width / columns;
        blockHeight = (double)regionOfInterest.height / rows;

        /* if a block is not square
        if (blockWidth / blockHeight > 1.02) {
        	blockHeight = blockWidth;
        	rows = (int)(regionOfInterest.height / blockHeight + 0.5); // round
        	blockHeight = (double)regionOfInterest.height / rows;
        } else if (blockHeight / blockWidth > 1.02) {
        	blockWidth = blockHeight;
        	columns = (int)(regionOfInterest.width / blockWidth + 0.5); // round
        	blockWidth = (double)regionOfInterest.width / columns;
        }
        */

        // Debug
        System.out.println("Region of Interest: " + regionOfInterest.width + " x " + regionOfInterest.height + " pixels");
        System.out.println("columns: " + columns);
        System.out.println("rows: " + rows);
        System.out.println("block size: " + String.format("%.2f", blockWidth) + " x " + String.format("%.2f", blockHeight) + " pixels");

        return 0;
    }

    public static HashMap<Point, Integer> refresh() {
        HashMap<Point, Integer> changedBlocks = new HashMap<Point, Integer>();

        // recapture window
        BufferedImage recapturedScreen = WindowCapture.captureWindow(windowName);
        if (recapturedScreen == null) {
            return changedBlocks;
        }

        // read template images
        int numberOfTemplate = 10;
        BufferedImage[] templates = new BufferedImage[numberOfTemplate];
        try {
            for (int i = 0; i < numberOfTemplate; i++) {
                templates[i] = ImageIO.read(new File("templates\\" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int changedPixels = 0;
                Rectangle block = getBlockRectangle(x, y);
                for (int block_y = block.y; block_y < block.y + block.height; block_y++) {
                    for (int block_x = block.x; block_x < block.x + block.width; block_x++) {
                        if (capturedScreen.getRGB(block_x, block_y) != recapturedScreen.getRGB(block_x, block_y)) {
                            changedPixels++;
                        }
                    }
                }
                if (((double)changedPixels / (block.width * block.height)) > 0.36) {
                    Rectangle targetRectangle = getBlockRectangle(x, y);

                    BufferedImage targetImage = ImageProcessing.crop(recapturedScreen, targetRectangle);
                    int templateMatchingResult = ImageProcessing.TemplateMatching(targetImage, templates);

                    changedBlocks.put(new Point(x, y), templateMatchingResult);
                    //System.out.println(String.format("%.2f", ((double)changedPixels / (block.width * block.height) * 100)) + "% of block (" + x + ", " + y + ") has changed to block #" + templateMatchingResult);
                }
            }
        }

        capturedScreen = recapturedScreen;

        return changedBlocks;
    }

    public static Point getBlockPosition(Point p) {
        return getBlockPosition(p.x, p.y);
    }

    public static Point getBlockPosition(int x, int y) {
        return new Point((int)(regionOfInterest.x + blockWidth * (x + 0.5)), (int)(regionOfInterest.y + blockHeight * (y + 0.5)));
    }

    public static Rectangle getBlockRectangle(Point p) {
        return getBlockRectangle(p.x, p.y);
    }

    public static Rectangle getBlockRectangle(int x, int y) {
        return new Rectangle((int)(regionOfInterest.x + blockWidth * x + 0.5), (int)(regionOfInterest.y + blockHeight * y + 0.5), (int)(blockWidth + 0.5), (int)(blockHeight + 0.5));
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }

    private static void analyzeZoneOfInterest(int[][] binarized) {
        int width = binarized.length;
        int height = binarized[0].length;
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> blacks = new HashMap<Integer, Integer>();
        boolean isBlack = false;

        // do Vertical
        for (int y = 0; y < height; y++) {
            isBlack = binarized[0][y] == 0;
            int count = 0;
            int black = 0;

            for (int x = 0; x < width; x++) {
                if (isBlack && binarized[x][y] == 0xFF) {
                    count++;
                    isBlack = false;
                } else if (!isBlack && binarized[x][y] == 0) {
                    isBlack = true;
                }

                if (binarized[x][y] == 0 && count == 0) {
                    black++;
                }
            }

            if (counts.containsKey(count)) {
                counts.replace(count, counts.get(count) + 1);
            } else {
                counts.put(count, 1);
            }

            if (blacks.containsKey(black)) {
                blacks.replace(black, blacks.get(black) + 1);
            } else {
                blacks.put(black, 1);
            }
        }

        // count vertical lines
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
        columns = maxKey - 1;

        // calculate thickness of the leftmost vertical line
        maxKey = 0;
        maxValue = 0;
        iterator = blacks.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (maxValue < blacks.get(key)) {
                maxKey = key;
                maxValue = blacks.get(key);
            }
        }
        regionOfInterest.x += (maxKey + 1) / 2;
        regionOfInterest.width -= maxKey;

        // initialize variables
        counts.clear();
        blacks.clear();
        isBlack = false;

        // do Horizontal
        for (int x = 0; x < width; x++) {
            isBlack = binarized[x][0] == 0;
            int count = 0;
            int black = 0;

            for (int y = 0; y < height; y++) {
                if (isBlack && binarized[x][y] == 0xFF) {
                    count++;
                    isBlack = false;
                } else if (!isBlack && binarized[x][y] == 0) {
                    isBlack = true;
                }

                if (binarized[x][y] == 0 && count == 0) {
                    black++;
                }
            }

            if (counts.containsKey(count)) {
                counts.replace(count, counts.get(count) + 1);
            } else {
                counts.put(count, 1);
            }

            if (blacks.containsKey(black)) {
                blacks.replace(black, blacks.get(black) + 1);
            } else {
                blacks.put(black, 1);
            }
        }

        // count horizontal lines
        maxKey = 0;
        maxValue = 0;
        iterator = counts.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (maxValue < counts.get(key)) {
                maxKey = key;
                maxValue = counts.get(key);
            }
        }
        rows = maxKey - 1;

        // calculate thickness of the topmost horizontal line
        maxKey = 0;
        maxValue = 0;
        iterator = blacks.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (maxValue < blacks.get(key)) {
                maxKey = key;
                maxValue = blacks.get(key);
            }
        }
        regionOfInterest.y += (maxKey + 1) / 2;
        regionOfInterest.height -= maxKey;
    }

}

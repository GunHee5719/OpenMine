package ImageProcessing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageProcessing {

    // Test code
    public static void show(int[][] map) {
        show(map, null);
    }

    public static void show(int[][] map, String title) {
        int width = map.length;
        int height = map[0].length;

        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = (map[x][y] << 16) | (map[x][y] << 8) | map[x][y];
                tmp.setRGB(x, y, rgb);
            }
        }

        show(tmp, title);
    }

    public static void show(BufferedImage image) {
        show(image, null);
    }

    public static void show(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    public static void drawRectangle(BufferedImage image, Rectangle rectangle, int colorRGB) {
        for (int y = rectangle.y - 1; y <= rectangle.y + rectangle.height; y++) {
            image.setRGB(rectangle.x - 1, y, colorRGB);
            image.setRGB(rectangle.x + rectangle.width, y, colorRGB);
        }
        for (int x = rectangle.x - 1; x <= rectangle.x + rectangle.width; x++) {
            image.setRGB(x, rectangle.y - 1, colorRGB);
            image.setRGB(x, rectangle.y + rectangle.height, colorRGB);
        }
    }

    public static void mark(BufferedImage image, Point point, int colorRGB) {
        mark(image, point, colorRGB, 2);
    }

    public static void mark(BufferedImage image, Point point, int colorRGB, int size) {
        for (int i = 1; i <= size; i++) {
            image.setRGB(point.x - i, point.y - i, colorRGB);
            image.setRGB(point.x + i, point.y - i, colorRGB);
            image.setRGB(point.x + i, point.y + i, colorRGB);
            image.setRGB(point.x - i, point.y + i, colorRGB);
        }
        image.setRGB(point.x, point.y, colorRGB);
    }

    public static int[][] Binarize(BufferedImage image) {
        return Binarize(image, 128);
    }

    public static int[][] Binarize(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] binarized = new int[width][height];

        int rgb, red, green, blue;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rgb = image.getRGB(x, y);
                red = (rgb & 0xFF0000) >> 16;
                green = (rgb & 0x00FF00) >> 8;
                blue = rgb & 0x0000FF;

                if (red < threshold && green < threshold && blue < threshold) {
                    binarized[x][y] = 0x00;
                }
                else {
                    binarized[x][y] = 0xFF;
                }
            }
        }

        return binarized;
    }

    private static class RECT{
        public int left, top, right, bottom;

        public RECT(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public static List<Rectangle> Labeling(int[][] binarized) {
        int width = binarized.length;
        int height = binarized[0].length;

        boolean[][] visited = new boolean[width][height];
        List<Rectangle> rectangles = new ArrayList<Rectangle>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (visited[x][y] == false && binarized[x][y] == 0) {
                    RECT rect = new RECT(x, y, x, y);
                    Stack<Point> stack = new Stack<Point>();

                    stack.push(new Point(x, y));

                    while (!stack.isEmpty()) {
                        Point target = stack.pop();
                        visited[target.x][target.y] = true;

                        if (target.x + 1 < width && visited[target.x + 1][target.y] == false && binarized[target.x + 1][target.y] == 0) {
                            if (rect.right < target.x + 1) {
                                rect.right = target.x + 1;
                            }
                            stack.push(new Point(target.x + 1, target.y));
                        }
                        if (target.y + 1 < height && visited[target.x][target.y + 1] == false && binarized[target.x][target.y + 1] == 0) {
                            if (rect.bottom < target.y + 1) {
                                rect.bottom = target.y + 1;
                            }
                            stack.push(new Point(target.x, target.y + 1));
                        }
                        if (target.x - 1 >= 0 && visited[target.x - 1][target.y] == false && binarized[target.x - 1][target.y] == 0) {
                            if (rect.left > target.x - 1) {
                                rect.left = target.x - 1;
                            }
                            stack.push(new Point(target.x - 1, target.y));
                        }
                        if (target.y - 1 >= 0 && visited[target.x][target.y - 1] == false && binarized[target.x][target.y - 1] == 0) {
                            if (rect.top > target.y - 1) {
                                rect.top = target.y - 1;
                            }
                            stack.push(new Point(target.x, target.y - 1));
                        }
                    }
                    rectangles.add(new Rectangle(rect.left, rect.top, rect.right - rect.left + 1, rect.bottom - rect.top + 1));
                }
                visited[x][y] = true;
            }
        }

        return rectangles;
    }

    public static BufferedImage crop(BufferedImage src, Rectangle rect) {
        return src.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private static double compareImageAlpha(BufferedImage target, BufferedImage template) {
        int width = template.getWidth();
        int height = template.getHeight();

        if (target.getWidth() != 18 || target.getHeight() != 18) {
            target = resize(target, 18, 18);
        }

        double minDiff = Double.MAX_VALUE;
        for (int shift_y = 0; shift_y <= target.getHeight() - height; shift_y++) {
            for (int shift_x = 0; shift_x <= target.getWidth() - width; shift_x++) {
                double diff = 0;
                int comparedPixels = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int templateRGB = template.getRGB(x, y);
                        if (((templateRGB & 0xFF000000) >> 24) == 0) {
                            continue;
                        }
                        int templateRed = ((templateRGB & 0xFF0000) >> 16);
                        int templateGreen = ((templateRGB & 0xFF00) >> 8);
                        int templateBlue = templateRGB & 0xFF;

                        int targetRGB = target.getRGB(x + shift_x, y + shift_y);
                        int targetRed = ((targetRGB & 0xFF0000) >> 16);
                        int targetGreen = ((targetRGB & 0xFF00) >> 8);
                        int targetBlue = targetRGB & 0xFF;

                        diff += Math.pow(targetRed - templateRed, 2) + Math.pow(targetGreen - templateGreen, 2) + Math.pow(targetBlue - templateBlue, 2);
                        comparedPixels++;
                    }
                }
                diff /= comparedPixels;
                if (minDiff > diff) {
                    minDiff = diff;
                }
            }
        }

        return minDiff;
    }

    public static int TemplateMatching(BufferedImage target, BufferedImage[] templates) {
        double minDifference = Double.MAX_VALUE, difference;
        int minIndex = templates.length;

        for (int i = 0; i < templates.length; i++) {
            difference = compareImageAlpha(target, templates[i]);
            //System.out.println("difference with templates[" + i + "]: " + String.format("%.2f", difference));
            if (minDifference > difference) {
                minDifference = difference;
                minIndex = i;
            }
        }

		/*
		if (maxSimilarity < 0.8) {
			maxIndex = templates.length;
		}
		*/

        return minIndex;
    }
}

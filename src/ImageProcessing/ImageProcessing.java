package ImageProcessing;

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

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(tmp)));
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
        int size = 2;
        for (int i = 1; i <= size; i++) {
            image.setRGB(point.x - i, point.y - i, colorRGB);
            image.setRGB(point.x + i, point.y - i, colorRGB);
            image.setRGB(point.x + i, point.y + i, colorRGB);
            image.setRGB(point.x - i, point.y + i, colorRGB);
        }
        image.setRGB(point.x, point.y, colorRGB);
    }

    public static int[][] Binarize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] binarized = new int[width][height];

        int rgb, red, green, blue, threshold;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rgb = image.getRGB(x, y);
                red = (rgb & 0xFF0000) >> 16;
                green = (rgb & 0x00FF00) >> 8;
                blue = rgb & 0x0000FF;
                threshold = 128;

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
}

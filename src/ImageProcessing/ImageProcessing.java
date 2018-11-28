package ImageProcessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

    public static class Point {
        public int x;
        public int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static List<Point> HarrisCorner(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // create binary image (Multiband thresholding)
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

        // Test code
        show(binarized, "Binarized");

        // apply gaussian blur
        int[][] blurred = new int[width][height];

        final int[][] gaussisan_filter = {
                { 1, 1, 1 },
                { 1, 1, 1 },
                { 1, 1, 1 } };

        int filtered_sum, filter_sum;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                filtered_sum = filter_sum = 0;

                for (int filter_y = 0; filter_y < gaussisan_filter.length; filter_y++) {
                    if (y + filter_y - 1 >= 0 && y + filter_y - 1 < height) { // check boundary
                        for (int filter_x = 0; filter_x < gaussisan_filter.length; filter_x++) {
                            if (x + filter_x - 1 >= 0 && x + filter_x - 1 < width) { // check boundary
                                filtered_sum += binarized[x + filter_x - 1][y + filter_y - 1] * gaussisan_filter[filter_x][filter_y];
                                filter_sum += gaussisan_filter[filter_x][filter_y];
                            }
                        }
                    }
                }

                blurred[x][y] = filtered_sum / filter_sum;
            }
        }
        binarized = null;

        // calculate intensity difference
        double[][] dx2 = new double[width][height];
        double[][] dy2 = new double[width][height];
        double[][] dxy = new double[width][height];

        double dx, dy;
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                dx = (blurred[x - 1][y + 1] + blurred[x][y + 1] * 2 + blurred[x + 1][y + 1]
                        - blurred[x - 1][y - 1] - blurred[x][y - 1] * 2 - blurred[x + 1][y - 1]) / 8.0;
                dy = (blurred[x + 1][y - 1] + blurred[x + 1][y] * 2 + blurred[x + 1][y + 1]
                        - blurred[x - 1][y - 1] - blurred[x - 1][y] * 2 - blurred[x - 1][y + 1]) / 8.0; // sobel filter

                dx2[x][y] = dx * dx;
                dy2[x][y] = dy * dy;
                dxy[x][y] = dx * dy;
            }
        }
        blurred = null;

        final int[][] gaussisan_filter_large = {
                { 1,  4,  6,  4, 1 },
                { 4, 16, 24, 16, 4 },
                { 6, 24, 36, 24, 6 },
                { 4, 16, 24, 16, 4 },
                { 1,  4,  6,  4, 1 } };

        // apply gaussian blur
        double[][] gdx2 = new double[width][height];
        double[][] gdy2 = new double[width][height];
        double[][] gdxy = new double[width][height];

        double tmp_gdx2, tmp_gdy2, tmp_gdxy;
        for (int y = 2; y < height - 2; y++) {
            for (int x = 2; x < width - 2; x++) {
                tmp_gdx2 = tmp_gdy2 = tmp_gdxy = 0;

                for (int filter_y = 0; filter_y < 5; filter_y++) {
                    for (int filter_x = 0; filter_x < 5; filter_x++) {
                        tmp_gdx2 += (dx2[x + filter_x - 2][y + filter_y - 2] * gaussisan_filter_large[filter_x][filter_y]) / 256.0;
                        tmp_gdy2 += (dy2[x + filter_x - 2][y + filter_y - 2] * gaussisan_filter_large[filter_x][filter_y]) / 256.0;
                        tmp_gdxy += (dxy[x + filter_x - 2][y + filter_y - 2] * gaussisan_filter_large[filter_x][filter_y]) / 256.0;
                    }
                }

                gdx2[x][y] = tmp_gdx2;
                gdy2[x][y] = tmp_gdy2;
                gdxy[x][y] = tmp_gdxy;
            }
        }
        dx2 = dy2 = dxy = null;

        final double k = 0.04;
        double max_crf = 0;

        // calculate Corner Response Function
        double[][] crf = new double[width][height];
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                crf[x][y] = ((gdx2[x][y] * gdy2[x][y] - gdxy[x][y] * gdxy[x][y])
                        - k * (gdx2[x][y] + gdy2[x][y]) * (gdx2[x][y] + gdy2[x][y]));

                if (max_crf < crf[x][y]) {
                    max_crf = crf[x][y];
                }
            }
        }
        gdx2 = gdy2 = gdxy = null;

        final double thresh = max_crf / 100;
        List<Point> corners = new ArrayList<Point>();

        // count number of corners
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (crf[x][y] > thresh) {
                    if (crf[x][y] > crf[x - 1][y - 1] && crf[x][y] > crf[x - 1][y] && crf[x][y] > crf[x - 1][y + 1] &&
                            crf[x][y] > crf[x    ][y - 1]                              && crf[x][y] > crf[x    ][y + 1] &&
                            crf[x][y] > crf[x + 1][y - 1] && crf[x][y] > crf[x + 1][y] && crf[x][y] > crf[x + 1][y + 1]) { // count local maxima
                        corners.add(new Point(x, y));
                    }
                    else if (crf[x][y] == crf[x][y + 1] || crf[x][y] == crf[x + 1][y] || crf[x][y] == crf[x + 1][y + 1]) {
                        crf[x][y]--; // ignore if value is same with neighbors
                    }
                }
            }
        }
        crf = null;

        return corners;
    }

}

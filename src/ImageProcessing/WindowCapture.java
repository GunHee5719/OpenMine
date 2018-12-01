package ImageProcessing;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public class WindowCapture {

    public static BufferedImage captureWindow(String windowName) {

        int hWnd = User32.instance.FindWindowA(null, windowName);
        if (hWnd == 0) {
            System.out.println("Window \"" + windowName + "\" has not found!");
            return null;
        }

        RECT clientArea = new RECT();
        POINT clientOrigin = new POINT();
        User32.instance.ShowWindowAsync(hWnd, User32.SW_SHOW);
        User32.instance.SetForegroundWindow(hWnd);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        User32.instance.GetClientRect(hWnd, clientArea);
        User32.instance.ClientToScreen(hWnd, clientOrigin);

        BufferedImage capturedScreen = null;
        try {
            capturedScreen = new Robot().createScreenCapture(
                    new Rectangle(clientOrigin.x, clientOrigin.y, clientArea.right, clientArea.bottom));
        } catch (AWTException e) {
            System.out.println("The platform configuration does not allow low-level input control");
        }

        return capturedScreen;
    }

    private static interface User32 extends StdCallLibrary {
        public static final int SW_RESTORE = 9;
        public static final int SW_SHOW = 5;

        @SuppressWarnings("deprecation")
        final User32 instance = (User32) Native.loadLibrary("user32", User32.class);

        int FindWindowA(String className, String windowName);

        boolean ShowWindowAsync(int hWnd, int nCmdShow);

        boolean SetForegroundWindow(int hWnd);

        boolean GetClientRect(int hWnd, RECT lpRect);

        boolean ClientToScreen(int hWnd, POINT lpPoint);
    }

    public static class RECT extends Structure {
        public int left, top, right, bottom;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("left", "top", "right", "bottom");
        }
    }

    public static class POINT extends Structure {
        public int x, y;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("x", "y");
        }
    }

}

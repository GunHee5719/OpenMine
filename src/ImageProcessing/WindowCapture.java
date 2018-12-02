package ImageProcessing;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class WindowCapture {

    public static int getHWND(String windowName) {
        return User32.instance.FindWindowA(null, windowName);
    }

    public static BufferedImage captureWindow(String windowName) {
        return captureWindow(getHWND(windowName));
    }

    public static BufferedImage captureWindow(int hWnd) {
        if (hWnd == 0) {
            System.out.println("Window not found!");
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

    public static interface User32 extends StdCallLibrary {
        //public static final int SW_RESTORE = 9;
        public static final int SW_SHOW = 5;

        @SuppressWarnings("deprecation")
        final User32 instance = (User32) Native.loadLibrary("user32", User32.class);

        int FindWindowA(String className, String windowName);

        boolean ShowWindowAsync(int hWnd, int nCmdShow);

        boolean SetForegroundWindow(int hWnd);

        boolean GetClientRect(int hWnd, RECT lpRect);

        boolean ClientToScreen(int hWnd, POINT lpPoint);

        boolean ScreenToClient(int hWnd, POINT lpPoint);

        interface WNDENUMPROC extends StdCallCallback {
            boolean callback(Pointer hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);

        int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
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

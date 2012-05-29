package com.jokeofweek.lib;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.util.HashMap;

import javax.swing.JOptionPane;

import com.jokeofweek.lib.*;
import com.jokeofweek.lib.util.*;
import com.jokeofweek.lib.wswing.StrokeInformer;
import com.jokeofweek.lib.wswing.SwingConsoleFrame;

/**
 * Swing interface for input and output.
 * Returns keystrokes as CharKeys.
 * Shows the characters in a Frame
 * @author Santiago Zapata
 * @author Eben Howard
 */
public class WSwingConsoleInterface implements ConsoleSystemInterface, Runnable, ComponentListener {

    private SwingConsoleFrame targetFrame; //To get the keypresses from the AWT Model
    private StrokeInformer aStrokeInformer; // Object to which strokes are informed

    // Attributes
    private int xpos,  ypos;
    /** Current printing cursor position */
    //private boolean autorefresh; //not currently implemented

    // Static Attributes
    public static Font consoleFont;
    public static int screenWidth = 80;
    public static int screenHeight = 25;
    private CSIColor[][] colors;
    private char[][] chars;
    private CSIColor[][] colorsBuffer;
    private char[][] charsBuffer;
    private CSIColor backColor = CSIColor.BLACK;
    private CSIColor frontColor = CSIColor.WHITE;
    private Position caretPosition = new Position(0, 0);
    private boolean sandboxDeploy;
    private HashMap<CSIColor, Color> colorMap = new HashMap<CSIColor, Color>();
    private FontMetrics fMetric;

    /**
     * Allows for setting the window's name and deploying as a
     * Java WebStart application.
     * @param windowName
     * @param sandboxDeploy true if intended at a Java Webstart application
     */
    public WSwingConsoleInterface(String windowName, boolean sandboxDeploy) {
        this.sandboxDeploy = sandboxDeploy;
        aStrokeInformer = new StrokeInformer();
        targetFrame = new SwingConsoleFrame(windowName);
        java.awt.Dimension initialSize = new java.awt.Dimension(1280, 1024);
        int fontSize = defineFontSize(initialSize.height, initialSize.width);
        consoleFont = loadFont(fontSize);
        targetFrame.init(consoleFont, screenWidth, screenHeight);

        colors = new CSIColor[screenWidth][screenHeight];
        chars = new char[screenWidth][screenHeight];
        colorsBuffer = new CSIColor[screenWidth][screenHeight];
        charsBuffer = new char[screenWidth][screenHeight];

        targetFrame.addKeyListener(aStrokeInformer);
        targetFrame.addComponentListener(this);
        fMetric = targetFrame.getFontMetrics(consoleFont);

        int x, y;
        x = fMetric.getMaxAdvance();
        y = fMetric.getHeight();
        if (!this.sandboxDeploy) {
            targetFrame.setSize((screenWidth * x) + x, (screenHeight * y) + y + y);
        } else {
            x = fMetric.charWidth('W');
            targetFrame.setSize(((screenWidth * x) + x), (screenHeight * y) + y + y);
        }
        targetFrame.setLocationRelativeTo(null); // places window in center of screen
        targetFrame.setResizable(false);
        locate(1, 1);

        targetFrame.setVisible(true);
    }

    /**
     * Flashes the output area a specified color.  Currently inoperable.
     * @param color
     */
    public void flash(int color) {
        //targetPanel.flash(getColorFromCode(color));
    }

    private Color colorPreProcess(CSIColor b) {
        if (!colorMap.containsKey(b)) {
            colorMap.put(b, new Color(b.getColor()));
        }
        return colorMap.get(b);
    }

    public void flushColorTable() {
        colorMap.clear();
    }

    public void cls() {
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                chars[x][y] = ' ';
                colors[x][y] = backColor;
            }
        }
        targetFrame.cls();
        flushColorTable();// might as well clean out the colorTable if the screen is going to be blank

    }

    public void locate(int x, int y) {
        xpos = x;
        ypos = y;
    }

    public void refresh() {
        targetFrame.repaint();
    }

    public void print(int x, int y, String what, int color) {
        print(x, y, what, frontColor.getColorFromCode(color), CSIColor.BLACK);
    }

    public void print(int x, int y, String what, CSIColor color) {
        print(x, y, what, color, CSIColor.BLACK);
    }

    public void print(int x, int y, String what, CSIColor color, CSIColor background) {
        locate(x, y);

        for (int i = 0; i < what.length(); i++) {
            if (xpos >= screenWidth) {
                xpos = 0;
                ypos++;
            }
            if (ypos >= screenHeight) {
                break;
            }
            targetFrame.plot(what.charAt(i), xpos, ypos, colorPreProcess(color), colorPreProcess(background));
            chars[x + i][y] = what.charAt(i);
            colors[x + i][y] = color;
            xpos++;
        }
    }

    public void print(int x, int y, char what, int color) {
        CSIColor front = frontColor.getColorFromCode(color);
        print(x, y, what, front);
    }

    public void print(int x, int y, char what, CSIColor color) {
        print(x, y, what, color, CSIColor.BLACK);
    }

    public void print(int x, int y, char what, CSIColor color, CSIColor back) {
        locate(x, y);
        if (chars[x][y] == what && colors[x][y] == color) {
            return;
        }
        targetFrame.plot(what, xpos, ypos, colorPreProcess(color), colorPreProcess(back));
        colors[x][y] = color;
        chars[x][y] = what;
    }

    public void print(int x, int y, String what) {
        print(x, y, what, frontColor);
    }

    public void locateCaret(int x, int y) {
        caretPosition.x = x;
        caretPosition.y = y;
    }

    public String input() {
        return input(9999);
    }

    public String input(int l) {
        String ret = "";
        CharKey read = new CharKey(CharKey.NONE);
        while (true) {
            while (read.code == CharKey.NONE) {
                read = inkey();
            }
            if (read.isMetaKey()) {
                read.code = CharKey.NONE;
                continue;
            }
            if (read.code == CharKey.ENTER) {
                return ret;
            }
            if (read.code == CharKey.BACKSPACE) {
                if (ret.equals("")) {
                    read.code = CharKey.NONE;
                    continue;
                }
                if (ret.length() > 1) {
                    ret = ret.substring(0, ret.length() - 1);
                } else {
                    ret = "";
                }
                caretPosition.x--;
                print(caretPosition.x, caretPosition.y, " ");

            } else {
                if (ret.length() >= l) {
                    read.code = CharKey.NONE;
                    continue;
                }
                String nuevo = read.toString();
                print(caretPosition.x, caretPosition.y, nuevo);
                ret += nuevo;
                caretPosition.x++;
            }
            refresh();
            read.code = CharKey.NONE;

        }
    //return ret;
    }

    public synchronized void refresh(Thread toNotify) {
        refresh();

        toNotify.interrupt();
    }

    public synchronized CharKey inkey() {
        aStrokeInformer.informKey(Thread.currentThread());
        try {
            this.wait();
        } catch (InterruptedException ie) {
        }
        CharKey ret = new CharKey(aStrokeInformer.getInkeyBuffer());
        return ret;
    }

    public int getColor(String colorName) {
        return frontColor.getColor(colorName);
    }

    public void setAutoRefresh(boolean value) {
    }

    public char peekChar(int x, int y) {
        return targetFrame.peekChar(x, y);
    }

    public int peekColor(int x, int y) {
        return frontColor.getCodeFromColor(colors[x][y]);
    }

    public CSIColor peekCSIColor(int x, int y) {
        return colors[x][y];
    }

    private Font loadFont(int fontSize) {
        
    	for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
        	if (font.equals("Courier New")) return new Font("Courier New", Font.PLAIN, fontSize);
        }
        
        InputStream is = null;
		try {
			is = new FileInputStream("font.ttf");
			
	        return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, fontSize);
	        
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "This game requires the Courier New font. It should either be installed in the system font folder or as a file called font.ttf in the same directory as the executable.");
			System.exit(0);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {System.exit(0);}
		}
		
		return null;
    }

    private int defineFontSize(int scrHeight, int scrWidth) {
        int byHeight = (int) (scrHeight / screenHeight);
        int byWidth = (int) (scrWidth / (screenWidth));

        if (byHeight < byWidth) {
            return byHeight;
        } else {
            return byWidth;
        }
    }

    public void run() {
    }

    public boolean isInsideBounds(Position p) {
        return p.x >= 0 && p.x <= screenWidth && p.y >= 0 && p.y <= screenHeight;
    }

    public boolean isInsideBounds(int x, int y) {
        return x >= 0 && x <= screenWidth - 1 && y >= 0 && y <= screenHeight - 1;
    }

    public void safeprint(int x, int y, char what, int color) {
        if (isInsideBounds(x, y)) {
            print(x, y, what, color);
        }
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        int fontSize = defineFontSize(((Component) e.getSource()).getHeight(), ((Component) e.getSource()).getWidth());
        consoleFont = loadFont(fontSize);
        targetFrame.setFont(consoleFont);

    }

    public void componentShown(ComponentEvent e) {
    }

    public void waitKey(int keyCode) {
        CharKey x = new CharKey(CharKey.NONE);
        while (x.code != keyCode) {
            x = inkey();
        }
    }

    public void restore() {
        colors = colorsBuffer.clone();
        chars = charsBuffer.clone();
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                this.print(x, y, chars[x][y], colors[x][y]);
            }
        }
    }

    public void saveBuffer() {
        colorsBuffer = colors.clone();
        charsBuffer = chars.clone();
    }
}
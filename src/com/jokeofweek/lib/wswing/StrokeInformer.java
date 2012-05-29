package com.jokeofweek.lib.wswing;

import java.awt.event.*;
import java.util.HashMap;

import com.jokeofweek.lib.CharKey;

/**
 * Gets keyboard input.
 * @author Santiago Zapata
 * @author Eben Howard
 */
public class StrokeInformer implements KeyListener, java.io.Serializable {

	private static final long serialVersionUID = -2436944219202115262L;
	public static HashMap<Character, Integer> specialMappings = new HashMap<Character, Integer>(25);
    private int bufferCode;
    private transient Thread keyListener;

    {
	    specialMappings.put('?',CharKey.QUESTION);
	    specialMappings.put('>',CharKey.MORETHAN);
	    specialMappings.put('<',CharKey.LESSTHAN);
	    specialMappings.put('|',CharKey.PIPE);
	    specialMappings.put('!',CharKey.EXCLAMATION);
	    specialMappings.put('@',CharKey.AT);
	    specialMappings.put('#',CharKey.POUND);
	    specialMappings.put('$',CharKey.DOLLAR);
	    specialMappings.put('%',CharKey.PERCENTAGE);
	    specialMappings.put('^',CharKey.CARET);
	    specialMappings.put('&',CharKey.AMPERSAND);
	    specialMappings.put('*',CharKey.ASTERISK);
	    specialMappings.put('(',CharKey.OPENPARENTHESIS);
	    specialMappings.put(')',CharKey.CLOSEPARENTHESIS);
	    specialMappings.put('-',CharKey.MINUS);
	    specialMappings.put('+',CharKey.PLUS);
	    specialMappings.put('=',CharKey.EQUALS);
	    specialMappings.put('[',CharKey.OPENSQUAREBRACE);
	    specialMappings.put(']',CharKey.CLOSESQUAREBRACE);
	    specialMappings.put('{',CharKey.OPENCURLYBRACE);
	    specialMappings.put('}',CharKey.CLOSECURLYBRACE);
	    specialMappings.put(' ',CharKey.SPACE);
	    specialMappings.put(',',CharKey.COMMA);
	    specialMappings.put('.',CharKey.DOT);
    }
    
    public StrokeInformer() {
        bufferCode = -1;
    }

    /**
     *
     * @param toWho where to add keyListener
     */
    public void informKey(Thread toWho) {
        keyListener = toWho;
    }

    /**
     *
     * @return code shows what was input
     */
    public int getInkeyBuffer() {
        return bufferCode;
    }

    /**
     * Captures input
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        bufferCode = charCode(e);
        keyListener.interrupt();
    }

    /**
     * Takes raw input and turns it into CharKey encoding.
     * @param x pressing of a key
     * @return CharKey encoded value
     */
    private int charCode(KeyEvent x) {
        int code = x.getKeyCode();
        if (x.isControlDown()) {
            return CharKey.CTRL;
        }
   
        if (code >= KeyEvent.VK_A && code <= KeyEvent.VK_Z) {
            if (x.getKeyChar() >= 'a') {
                int diff = KeyEvent.VK_A - CharKey.a;
                return code - diff;
            } else {
                int diff = KeyEvent.VK_A - CharKey.A;
                return code - diff;
            }
        }
        
        if (x.getKeyChar() >= '0' && x.getKeyChar() <= '9')
        	return CharKey.N0 + (x.getKeyChar() - '0');
        
        Object retKey = specialMappings.get(x.getKeyChar());
        if (retKey != null)
        	return (Integer)retKey;

        switch (x.getKeyCode()) {
            case KeyEvent.VK_F1:
                return CharKey.F1;
            case KeyEvent.VK_ENTER:
                return CharKey.ENTER;
            case KeyEvent.VK_BACK_SPACE:
                return CharKey.BACKSPACE;
            case KeyEvent.VK_ESCAPE:
                return CharKey.ESC;
            case KeyEvent.VK_UP:
                return CharKey.UARROW;
            case KeyEvent.VK_DOWN:
                return CharKey.DARROW;
            case KeyEvent.VK_LEFT:
                return CharKey.LARROW;
            case KeyEvent.VK_RIGHT:
                return CharKey.RARROW;
            case KeyEvent.VK_COLON:
            	return CharKey.COLON;
            case KeyEvent.VK_BACK_SLASH:
            	return CharKey.BACKSLASH;
            case KeyEvent.VK_SLASH:
            	return CharKey.SLASH;

        }
        return CharKey.NONE;
    }

    /**
     * Currently does nothing.
     * @param e
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Currently does nothing.
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }
}
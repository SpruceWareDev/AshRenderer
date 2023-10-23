package dev.spruce.ash.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    private static KeyboardInput instance;

    private boolean[] keys;
    
    public KeyboardInput() {
        keys = new boolean[256];
    }
    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = true;
        }

        /*
        if (RenderPanel.getCurrentScreen().isPresent()) {
            RenderPanel.getCurrentScreen().get().keyTyped(e.getKeyChar(), e.getKeyCode());
            return;
        }
        RenderPanel.getStateManager().getCurrentState().keyTyped(e.getKeyChar(), e.getKeyCode());

         */
    }
    
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }
    
    public void keyTyped(KeyEvent e) {
        // Not used
    }
    
    public boolean isKeyDown(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        } else {
            return false;
        }
    }

    public static KeyboardInput get(){
        if (instance == null){
            instance = new KeyboardInput();
        }
        return instance;
    }
}
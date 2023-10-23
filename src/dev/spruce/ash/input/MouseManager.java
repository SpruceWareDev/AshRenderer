package dev.spruce.ash.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {

	private static MouseManager instance;

	private boolean leftPressed, rightPressed;
	private int mouseX, mouseY;
	
	@Override
	public void mousePressed(MouseEvent e) {
		// left click pressed
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = true;
		}
		// right click pressed
		if(e.getButton() == MouseEvent.BUTTON3) {
			rightPressed = true;
		}

		/*
		if (RenderPanel.getCurrentScreen().isPresent()) {
			RenderPanel.getCurrentScreen().get().mousePressed(e.getX(), e.getY(), e.getButton());
			return;
		}
		RenderPanel.getStateManager().getCurrentState().mousePressed(e.getX(), e.getY(), e.getButton());

		 */
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// left click pressed
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = false;
		}
		// right click pressed
		if(e.getButton() == MouseEvent.BUTTON3) {
			rightPressed = false;
		}

		/*
		if (RenderPanel.getCurrentScreen().isPresent()) {
			RenderPanel.getCurrentScreen().get().mouseReleased(e.getX(), e.getY(), e.getButton());
			return;
		}
		RenderPanel.getStateManager().getCurrentState().mouseReleased(e.getX(), e.getY(), e.getButton());

		 */
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public boolean hovered(float x, float y, float width, float height) {
		return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
	}

	public static MouseManager get(){
		if (instance == null){
			instance = new MouseManager();
		}
		return instance;
	}
}
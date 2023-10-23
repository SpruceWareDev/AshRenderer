package dev.spruce.ash.graphics;

import dev.spruce.ash.AshProgram;
import dev.spruce.ash.input.KeyboardInput;
import dev.spruce.ash.input.MouseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainRenderer extends JPanel {

    private static JFrame parentWindow;
    private final AshProgram parentProgram;
    private int lastWidth = 0, lastHeight = 0;

    private BufferedImage image;
    private Graphics graphics;

    private boolean running;

    public MainRenderer(AshProgram program, JFrame parentIn) {
        this.parentProgram = program;
        parentWindow = parentIn;
        lastWidth = parentWindow.getWidth();
        lastHeight = parentWindow.getHeight();
    }

    public void run() {
        init();

        long lastUpdateTime = System.nanoTime();
        long lastRenderTime = System.nanoTime();
        final double nsPerUpdate = 1000000000.0 / 60.0;
        final double nsPerRender = 1000000000.0 / 60.0;

        int updates = 0;
        int renders = 0;
        long timer = System.currentTimeMillis();
        double deltaUpdate = 0.0;
        double deltaRender = 0.0;

        while (running) {
            long now = System.nanoTime();
            deltaUpdate += (now - lastUpdateTime) / nsPerUpdate;
            deltaRender += (now - lastRenderTime) / nsPerRender;
            lastUpdateTime = now;
            lastRenderTime = now;

            while (deltaUpdate >= 1.0) {
                update();
                updates++;
                deltaUpdate -= 1.0;
            }

            if (deltaRender >= 1.0) {
                render();
                draw();
                renders++;
                deltaRender -= 1.0;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.println("Updates: " + updates + ", Renders: " + renders);
                updates = 0;
                renders = 0;
                timer += 1000;
            }
        }
    }

    private void init() {
        this.running = true;
        setFocusable(true);
        requestFocus();
        addKeyListener(KeyboardInput.get());
        addMouseListener(MouseManager.get());
        addMouseMotionListener(MouseManager.get());
        updateImage(parentWindow.getWidth(), parentWindow.getHeight());
        //stateManager = new StateManager(new MenuState());
    }

    private void updateImage(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.getGraphics();
    }

    private void update() {
        if (lastWidth != parentWindow.getWidth() || lastHeight != parentWindow.getHeight())
            updateImage(parentWindow.getWidth(), parentWindow.getHeight());
        //stateManager.update();
        //getCurrentScreen().ifPresent(Screen::update);
        parentProgram.update();
    }

    private void render() {
        if (graphics == null)
            return;
        graphics.setColor(new Color(50, 50, 50));
        graphics.fillRect(0, 0, parentWindow.getWidth(), parentWindow.getHeight());

        parentProgram.render(graphics);
        //stateManager.render(graphics);
        //getCurrentScreen().ifPresent(screen -> screen.render(graphics));
        //getCurrentScreen().ifPresent(screen -> screen.renderPanels(graphics));
    }

    private void draw() {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        if (g2d == null)
            return;
        g2d.drawImage(this.image, 0, 0, parentWindow.getWidth(), parentWindow.getHeight(), null);
        g2d.dispose();
    }

    /*
    public static StateManager getStateManager() {
        return stateManager;
    }

    public static Optional<Screen> getCurrentScreen() {
        return (currentScreen == null) ? Optional.empty() : Optional.of(currentScreen);
    }

    public static void setCurrentScreen(Screen currentScreen) {
        RenderPanel.currentScreen = currentScreen;
        currentScreen.init();
    }

    public static void exitScreen() {
        RenderPanel.currentScreen = null;
    }

     */

    public static JFrame getWindow() {
        return parentWindow;
    }
}

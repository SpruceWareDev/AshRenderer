package dev.spruce.ash.window;

import dev.spruce.ash.AshProgram;
import dev.spruce.ash.graphics.MainRenderer;

import javax.swing.*;
import java.awt.*;

public class Window {

    private final JFrame window;
    private final MainRenderer panel;

    public Window(AshProgram program, String title, int width, int height) {
        this.window = new JFrame();
        this.window.setTitle(title);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setSize(new Dimension(width, height));
        this.window.setPreferredSize(new Dimension(width, height));
        this.window.setMinimumSize(new Dimension(800, 600));
        this.window.setResizable(false);
        this.window.setContentPane(panel = new MainRenderer(program, window));
        this.window.pack();
        this.window.setLocationRelativeTo(null);
        this.window.setVisible(true);
    }

    public JFrame getWindow() {
        return window;
    }

    public MainRenderer getRenderer() {
        return panel;
    }
}

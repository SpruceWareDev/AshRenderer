package dev.spruce.ash;

import dev.spruce.ash.graphics.MainRenderer;
import dev.spruce.ash.input.KeyboardInput;
import dev.spruce.ash.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AshProgram {

    private final Window window;
    private final MainRenderer renderer;

    private int px, py;
    private double deltaX, deltaY, angle = 0;
    private final int mapX = 8, mapY = 8, mapS = 64;
    private final int[][] map = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 1, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };
    private final int[][] colourMap = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };

    public AshProgram() {
        this.window = new Window(this, "Ash Renderer Dev Build 1", 800, 600);
        this.renderer = this.window.getRenderer();
        init();
        renderer.run();
    }

    private void init() {
        px = 100;
        py = 100;
        deltaX = (Math.cos(angle) * 5);
        deltaY = (Math.sin(angle) * 5);
    }

    public void render(Graphics graphics) {
        //drawWorld2D(graphics);
        drawPlayer(graphics);
    }

    public void update() {
        handleInput();
    }

    private void handleInput() {
        if (KeyboardInput.get().isKeyDown(KeyEvent.VK_W)) {
            py = checkCollisions(px, (int) (py + deltaY)) ? py : (int) (py + deltaY);
            px = checkCollisions((int) (px + deltaX), py) ? px : (int) (px + deltaX);
        } else if (KeyboardInput.get().isKeyDown(KeyEvent.VK_S)) {
            //py -= deltaY;
            //px -= deltaX;
            py = checkCollisions(px, (int) (py - deltaY)) ? py : (int) (py - deltaY);
            px = checkCollisions((int) (px - deltaX), py) ? px : (int) (px - deltaX);
        }

        if (KeyboardInput.get().isKeyDown(KeyEvent.VK_A)) {
            angle -= 0.1f;
            if (angle < 0) {
                angle += (2 * Math.PI);
            }
            deltaX = (int) (Math.cos(angle) * 5);
            deltaY = (int) (Math.sin(angle) * 5);
        } else if (KeyboardInput.get().isKeyDown(KeyEvent.VK_D)) {
            angle += 0.1f;
            if (angle > (2 * Math.PI)) {
                angle -= (2 * Math.PI);
            }
            deltaX = (Math.cos(angle) * 5);
            deltaY = (Math.sin(angle) * 5);
        }
    }

    private void drawWorld2D(Graphics graphics) {
        for (int y = 0; y < mapY; y++) {
            for (int x = 0; x < mapX; x++) {
                int current = map[x][y];
                graphics.setColor(current == 1 ? Color.white : Color.BLACK);
                graphics.fillRect(x * mapS, y * mapS, mapS, mapS);
            }
        }
    }

    private void drawPlayer(Graphics graphics) {
        /*
        graphics.setColor(Color.yellow);
        graphics.fillRect(px - 5, py - 5, 10, 10);
        graphics.drawLine(px, py, (int) (px + (deltaX * 5)), (int) (py + (deltaY * 5)));

         */

        castAndDrawRay((Graphics2D) graphics, 800, 600);
    }

    public void castAndDrawRay(Graphics2D g2d, int screenWidth, int screenHeight) {
        for (int x = 0; x < screenWidth; x++) {
            // Calculate the ray direction
            double cameraX = 2 * x / (double) screenWidth - 1;
            double rayDirX = deltaX + cameraX * deltaY;
            double rayDirY = deltaY - cameraX * deltaX;

            // Initial position and step direction
            double rayX = px;
            double rayY = py;
            int stepX = (rayDirX > 0) ? 1 : -1;
            int stepY = (rayDirY > 0) ? 1 : -1;

            // Calculate side distance
            double sideDistX = Math.abs((stepX - (rayX - (int) rayX)) / rayDirX);
            double sideDistY = Math.abs((stepY - (rayY - (int) rayY)) / rayDirY);

            // Initialize wall hit and wall side
            boolean hit = false;
            int side = 0;

            // Digital Differential Analysis (DDA)
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += Math.abs(1 / rayDirX);
                    rayX += stepX;
                    side = 0;
                } else {
                    sideDistY += Math.abs(1 / rayDirY);
                    rayY += stepY;
                    side = 1;
                }

                // Check for a wall hit
                if (map[(int) rayX / mapS][(int) rayY / mapS] == 1) {
                    hit = true;
                }
            }

            // Calculate distance to the wall
            double perpWallDist;
            if (side == 0) {
                perpWallDist = (rayX - px + (1 - stepX) / 2) / rayDirX;
            } else {
                perpWallDist = (rayY - py + (1 - stepY) / 2) / rayDirY;
            }

            // Calculate line height
            int lineHeight = (int) (screenHeight / perpWallDist);

            // Calculate drawing limits
            int drawStart = -lineHeight / 2 + screenHeight / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + screenHeight / 2;
            if (drawEnd >= screenHeight) drawEnd = screenHeight - 1;

            // Choose a wall color based on the side
            //Color wallColor = (side == 1) ? Color.BLUE : Color.RED;
            Color wallColor = getColourFromMap(colourMap[(int) (rayX / mapS)][(int) (rayY / mapS)]);

            // Draw the wall slice
            g2d.setColor(wallColor);
            g2d.drawLine(x, drawStart, x, drawEnd);
        }
    }

    private boolean checkCollisions(int objX, int objY) {
        if (objX > (mapX * mapS) || objX < 0 || objY > (mapY * mapS) || objY < 0) return false;
        return map[objX / mapS][objY / mapS] == 1;
    }

    private Color getColourFromMap(int mapValue) {
        switch (mapValue) {
            case 1 -> {return Color.green;}
            default -> {return Color.red;}
        }
    }
}

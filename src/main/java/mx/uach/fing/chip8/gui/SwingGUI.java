/* 
 * Copyright (C) 2015 UACH <http://fing.uach.mx>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mx.uach.fing.chip8.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;

import mx.uach.fing.chip8.Chip8;
import mx.uach.fing.chip8.VRAM;
import mx.uach.fing.chip8.utils.MemoryUtils;

/**
 *
 * @author Luis Chávez
 */
public class SwingGUI {

    public static final int[] KEY_CODES = {
        KeyEvent.VK_X, // 0
        KeyEvent.VK_1, // 1
        KeyEvent.VK_2, // 2
        KeyEvent.VK_3, // 3
        KeyEvent.VK_Q, // 4
        KeyEvent.VK_W, // 5
        KeyEvent.VK_E, // 6
        KeyEvent.VK_A, // 7
        KeyEvent.VK_S, // 8
        KeyEvent.VK_D, // 9
        KeyEvent.VK_Z, // A
        KeyEvent.VK_C, // B
        KeyEvent.VK_4, // C
        KeyEvent.VK_R, // D
        KeyEvent.VK_F, // E
        KeyEvent.VK_V, // F
    };

    private final Chip8 chip8 = new Chip8();
    private final JFrame mainFrame = new JFrame("Chip-8 by luischavez");
    private final GameCanvas canvas = new GameCanvas(chip8.vram());

    private void initialize() {
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainFrame.setLayout(new BorderLayout());

        this.canvas.setPreferredSize(new Dimension(VRAM.SCREEN_WIDTH * 10, VRAM.SCREEN_HEIGHT * 10));

        this.mainFrame.add(this.canvas);

        this.mainFrame.pack();
        this.mainFrame.setResizable(false);
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.setVisible(true);
    }

    private void start() {
        byte[] rom = MemoryUtils.toBytes(Chip8.class.getResourceAsStream("/rom/PONG"));

        this.mainFrame.addKeyListener(new SwingKeyListener());

        this.chip8.vram().setListener(new SwingBufferListener());
        this.chip8.vram().clear();

        this.chip8.loadMemory(rom);

        new Thread(chip8, "CHIP-8 THREAD").start();
    }

    public static void main(String[] args) {
        SwingGUI main = new SwingGUI();

        main.initialize();
        main.start();
    }

    private class SwingKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            for (int i = 0; i < KEY_CODES.length; i++) {
                if (keyCode == KEY_CODES[i]) {
                    chip8.keyboard().down(i);
                    break;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();

            for (int i = 0; i < KEY_CODES.length; i++) {
                if (keyCode == KEY_CODES[i]) {
                    chip8.keyboard().up(i);
                    break;
                }
            }
        }
    }

    private class SwingBufferListener implements VRAM.BufferListener {

        @Override
        public void onDraw(int[] buffer) {
            canvas.repaint();
        }
    }

    private class GameCanvas extends JComponent {

        private final VRAM vram;
        private final BufferedImage screen = new BufferedImage(VRAM.SCREEN_WIDTH * 10, VRAM.SCREEN_HEIGHT * 10, BufferedImage.TYPE_USHORT_GRAY);

        public GameCanvas(VRAM vram) {
            this.vram = vram;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int[] buffer = this.vram.getBuffer();

            for (int i = 0; i < buffer.length; i++) {
                int x = VRAM.toPointX(i);
                int y = VRAM.toPointY(i);

                int pixel = buffer[i];

                Graphics2D graphics = this.screen.createGraphics();

                graphics.setColor(0 == pixel ? Color.BLACK : Color.WHITE);

                graphics.fillRect(x * 10, y * 10, 10, 10);
            }

            g.drawImage(this.screen, 0, 0, mainFrame);
        }
    }
}

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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import mx.uach.fing.chip8.Chip8;
import mx.uach.fing.chip8.VRAM;
import mx.uach.fing.chip8.utils.MemoryUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Chávez
 */
public class SwingGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingGUI.class);

    // Instancia del CHIP 8.
    private Chip8 chip8;

    // Listener que escuchara por los eventos del teclado.
    private final SwingKeyListener keyListener = new SwingKeyListener();

    // Componentes Swing.
    private final JFrame mainFrame = new JFrame("Chip-8 by luischavez");
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem loadMenuItem = new JMenuItem("Load");

    // Lienzo donde se dibujara el estado.
    private GameCanvas canvas;

    /**
     * Inicializa la ventana y agrega los componentes, este metodo solo se llama
     * la primera vez que se crea la ventana.
     */
    private void initialize() {
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.mainFrame.setLayout(new FlowLayout());

        this.mainFrame.addKeyListener(this.keyListener);
        this.loadMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });

        this.fileMenu.add(this.loadMenuItem);
        this.menuBar.add(this.fileMenu);
        this.mainFrame.setJMenuBar(this.menuBar);

        this.mainFrame.pack();
        this.mainFrame.setResizable(false);
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.setVisible(true);
    }

    /**
     * Crea el lienzo y lo muestra por pantalla.
     *
     * @param vram memoria de video.
     */
    private void createCanvas(final VRAM vram) {
        if (null != this.canvas) {
            this.mainFrame.remove(this.canvas);
            this.mainFrame.pack();
        }

        this.canvas = new GameCanvas(vram);
        this.canvas.setPreferredSize(
                new Dimension(
                        vram.screenWidth() * 10,
                        vram.screenHeight() * 10));

        vram.setListener(new VRAM.BufferListener() {
            @Override
            public void onDraw(int[] buffer) {
                SwingGUI.this.canvas.repaint();
            }

            @Override
            public void onModeChanged(boolean extended) {
                SwingGUI.this.createCanvas(vram);
            }
        });

        this.mainFrame.add(this.canvas);
        this.mainFrame.pack();
        this.mainFrame.setLocationRelativeTo(null);
    }

    /**
     * Crea una nueva instancia del CHIP 8, ademas de configurar la rom en
     * memoria y inicializar los listeners.
     */
    private void start(byte[] rom) {
        if (null != this.chip8) {
            this.chip8.stop();
        }

        this.chip8 = new Chip8();
        this.chip8.loadMemory(rom);

        this.keyListener.setKeyboard(this.chip8.keyboard);

        this.createCanvas(this.chip8.vram);

        new Thread(this.chip8, "CHIP-8 THREAD").start();
    }

    /**
     * Carga una rom desde un archivo y inicializa el emulador.
     */
    private void load() {
        JFileChooser fileChooser
                = new JFileChooser(System.getProperty("user.dir"));

        int option = fileChooser.showOpenDialog(this.mainFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] rom = MemoryUtils.toBytes(inputStream);

                this.start(rom);
            } catch (IOException ex) {
                LOGGER.error("", ex);
            }
        }
    }

    public static void main(String[] args) {
        new SwingGUI().initialize();
    }
}

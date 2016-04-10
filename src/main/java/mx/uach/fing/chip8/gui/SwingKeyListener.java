/*
 * Copyright (C) 2015 UACH
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import mx.uach.fing.chip8.Keyboard;

/**
 *
 * @author Luis Chávez
 */
public class SwingKeyListener extends KeyAdapter {

    /**
     * Mapeo de teclas de Swing. 
     * |---------------| 
     * | 1 | 2 | 3 | C | 
     * | 4 | 5 | 6 | D | 
     * | 7 | 8 | 9 | E | 
     * | A | 0 | B | F | 
     * |---------------|
     */
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

    /**
     * Instancia del teclado.
     */
    private Keyboard keyboard;

    public SwingKeyListener() {
    }
    
    public SwingKeyListener(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    /**
     * Estavlece la instancia del teclado actual.
     * 
     * @param keyboard teclado.
     */
    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (null == this.keyboard) {
            return;
        }
        
        int keyCode = e.getKeyCode();
        for (int i = 0; i < KEY_CODES.length; i++) {
            if (keyCode == KEY_CODES[i]) {
                this.keyboard.down(i);
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (null == this.keyboard) {
            return;
        }
        
        int keyCode = e.getKeyCode();
        for (int i = 0; i < KEY_CODES.length; i++) {
            if (keyCode == KEY_CODES[i]) {
                this.keyboard.up(i);
                break;
            }
        }
    }
}

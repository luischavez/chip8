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
package mx.uach.fing.chip8.instruction;

import mx.uach.fing.chip8.Chip8;
import mx.uach.fing.chip8.Keyboard;
import mx.uach.fing.chip8.OPCode;

/**
 *
 * Fx0A - LD Vx, K Wait for a key press, store the value of the key in Vx.
 *
 * All execution stops until a key is pressed, then the value of that key is
 * stored in Vx.
 *
 * @author Luis Chávez
 */
public class WaitKeyDownInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, final Chip8 chip8) {
        final int x = opcode.getX();

        chip8.keyboard.waitKey(new Keyboard.KeyListener() {

            @Override
            public void onKeyFound(int key) {
                chip8.register.set(x, key);
            }
        });
    }
}

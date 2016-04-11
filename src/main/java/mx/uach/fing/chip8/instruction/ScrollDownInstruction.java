/*
 * Copyright (C) 2016 UACH
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
import mx.uach.fing.chip8.OPCode;

/**
 * 00CN - Scroll display N lines down.
 *
 * @author Luis Chavez
 */
public class ScrollDownInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Chip8 chip8) {
        int n = opcode.getNibble();
        while (n-- > 0) {
            for (int row = chip8.vram.screenHeight() - 2; row >= 0; row--) {
                for (int column = 0;
                        column < chip8.vram.screenWidth(); column++) {
                    int pixel = chip8.vram.pixel(column, row);
                    chip8.vram.pixel(column, row + 1, pixel);
                }
            }
        }
    }
}

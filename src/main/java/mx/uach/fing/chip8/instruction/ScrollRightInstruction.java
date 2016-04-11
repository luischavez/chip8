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
 * 00FB - Scroll display 4 pixels right.
 *
 * @author Luis Chavez
 */
public class ScrollRightInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Chip8 chip8) {
        int n = 4;
        while (n-- > 0) {
            for (int column = chip8.vram.screenWidth() - 2;
                    column >= 0; column--) {
                for (int row = 0; row < chip8.vram.screenHeight(); row++) {
                    int pixel = chip8.vram.pixel(column, row);
                    chip8.vram.pixel(column + 1, row, pixel);
                }
            }
        }
    }
}

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
import mx.uach.fing.chip8.Memory;
import mx.uach.fing.chip8.OPCode;

/**
 * FX30 - Point I to 10-byte font sprite for digit VX (0..9).
 *
 * @author Luis Chavez
 */
public class LoadExtendedFontVxInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Chip8 chip8) {
        int x = opcode.getX();

        int vx = chip8.register.get(x);

        vx = Memory.FONT_INIT + vx * 10;

        chip8.register.setRegisterI(vx);
    }
}

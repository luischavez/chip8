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
package mx.uach.fing.chip8.instruction;

import mx.uach.fing.chip8.Keyboard;
import mx.uach.fing.chip8.Memory;
import mx.uach.fing.chip8.OPCode;
import mx.uach.fing.chip8.Register;
import mx.uach.fing.chip8.Stack;
import mx.uach.fing.chip8.VRAM;

/**
 * 4xkk - SNE Vx, byte Skip next instruction if Vx != kk.
 *
 * The interpreter compares register Vx to kk, and if they are not equal,
 * increments the program counter by 2.
 *
 * @author UACH <http://fing.uach.mx>
 */
public class SkipNotEqualVxKKInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Memory memory, VRAM vram, Stack stack, Register register, Keyboard keyboard) {
        int x = opcode.getX();
        int b = opcode.getByte();

        int vx = register.get(x);

        if (vx != b) {
            register.incrementPC();
        }
    }
}

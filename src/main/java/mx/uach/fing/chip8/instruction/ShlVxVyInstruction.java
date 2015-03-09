/*
 * Copyright (C) 2015 Your Organisation
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
 * 8xyE - SHL Vx {, Vy} Set Vx = Vx SHL 1.
 *
 * If the most-significant bit of Vx is 1, then VF is set to 1, otherwise to 0.
 * Then Vx is multiplied by 2.
 *
 * @author UACH <http://fing.uach.mx>
 */
public class ShlVxVyInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Memory memory, VRAM vram, Stack stack, Register register, Keyboard keyboard) {
        int x = opcode.getX();
        int y = opcode.getY();

        int vy = register.get(y);

        register.set(Register.REGISTER_FLAG, (vy & 0x80) != 0 ? Register.BIT_1 : Register.BIT_0);

        vy <<= 1;

        register.set(x, vy);
    }
}

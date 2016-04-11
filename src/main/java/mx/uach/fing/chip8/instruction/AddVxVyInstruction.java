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
import mx.uach.fing.chip8.OPCode;
import mx.uach.fing.chip8.Register;

/**
 *
 * 8xy4 - ADD Vx, Vy Set Vx = Vx + Vy, set VF = carry.
 *
 * The values of Vx and Vy are added together. If the result is greater than 8
 * bits (i.e., > 255,) VF is set to 1, otherwise 0. Only the lowest 8 bits of
 * the result are kept, and stored in Vx.
 *
 * @author Luis Chávez
 */
public class AddVxVyInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Chip8 chip8) {
        int x = opcode.getX();
        int y = opcode.getY();

        int vx = chip8.register.get(x);
        int vy = chip8.register.get(y);

        chip8.register.set(Register.REGISTER_FLAG,
                vx > vx + vy ? Register.CARRY : Register.NOT_CARRY);

        vx += vy;

        chip8.register.set(x, vx);
    }
}

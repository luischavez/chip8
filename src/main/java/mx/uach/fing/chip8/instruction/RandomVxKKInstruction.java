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

import java.util.Random;
import mx.uach.fing.chip8.Keyboard;
import mx.uach.fing.chip8.Memory;
import mx.uach.fing.chip8.OPCode;
import mx.uach.fing.chip8.Register;
import mx.uach.fing.chip8.Stack;
import mx.uach.fing.chip8.VRAM;

/**
 * Cxkk - RND Vx, byte Set Vx = random byte AND kk.
 *
 * The interpreter generates a random number from 0 to 255, which is then ANDed
 * with the value kk. The results are stored in Vx. See instruction 8xy2 for
 * more information on AND.
 *
 * @author UACH <http://fing.uach.mx>
 */
public class RandomVxKKInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Memory memory, VRAM vram, Stack stack, Register register, Keyboard keyboard) {
        Random random = new Random();

        int x = opcode.getX();

        int number = random.nextInt(256);
        int kk = opcode.getByte();

        register.set(x, number & kk);
    }
}

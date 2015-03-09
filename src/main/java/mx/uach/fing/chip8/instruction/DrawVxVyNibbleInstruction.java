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
 * Dxyn - DRW Vx, Vy, nibble Display n-byte sprite starting at memory location I
 * at (Vx, Vy), set VF = collision.
 *
 * The interpreter reads n bytes from memory, starting at the address stored in
 * I. These bytes are then displayed as sprites on screen at coordinates (Vx,
 * Vy). Sprites are XORed onto the existing screen. If this causes any pixels to
 * be erased, VF is set to 1, otherwise it is set to 0. If the sprite is
 * positioned so part of it is outside the coordinates of the display, it wraps
 * around to the opposite side of the screen. See instruction 8xy3 for more
 * information on XOR, and section 2.4, Display, for more information on the
 * Chip-8 screen and sprites.
 *
 * @author UACH <http://fing.uach.mx>
 */
public class DrawVxVyNibbleInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Memory memory, VRAM vram, Stack stack, Register register, Keyboard keyboard) {
        int x = opcode.getX();
        int y = opcode.getY();

        int n = opcode.getNibble();

        int i = register.getRegisterI();

        register.set(Register.REGISTER_FLAG, Register.NOT_COLLISION);
        for (int row = 0; row < n; row++) {
            int sprite = memory.read(i + row);

            for (int column = 0; column < VRAM.SPRITE_WIDTH; column++) {
                int px = register.get(x) + column;
                px &= (VRAM.SCREEN_WIDTH - 1);

                int py = register.get(y) + row;
                py &= (VRAM.SCREEN_HEIGHT - 1);

                int pixel = (sprite & (1 << (7 - column))) != 0 ? 1 : 0;
                int unset = vram.xor(px, py, pixel);

                int vf = register.get(Register.REGISTER_FLAG);
                vf |= unset;

                register.set(Register.REGISTER_FLAG, vf);
            }
        }
    }
}

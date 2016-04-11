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
import mx.uach.fing.chip8.VRAM;

/**
 * Dxyn - DRW Vx, Vy, nibble Display n-byte sprite starting at memory location I
 * at (Vx, Vy), set VF = collision.
 *
 * If N=0 and extended mode, show 16x16 sprite.
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
 * @author Luis Chávez
 */
public class DrawVxVyNibbleInstruction implements Instruction {

    @Override
    public void execute(OPCode opcode, Chip8 chip8) {
        int x = opcode.getX();
        int y = opcode.getY();

        int n = opcode.getNibble();

        int i = chip8.register.getRegisterI();

        chip8.register.set(Register.REGISTER_FLAG, Register.NOT_COLLISION);
        for (int row = 0; row < n; row++) {
            int sprite = chip8.memory.read(i + row);

            int spriteWidth = 0 == n && chip8.isExtended()
                    ? VRAM.EXTENDED_SPRITE_WIDTH : VRAM.STANDARD_SPRITE_WIDTH;

            for (int column = 0; column < spriteWidth; column++) {
                int px = chip8.register.get(x) + column;
                px &= (chip8.vram.screenWidth() - 1);

                int py = chip8.register.get(y) + row;
                py &= (chip8.vram.screenHeight() - 1);

                int pixel = (sprite & (1 << (7 - column))) != 0 ? 1 : 0;
                int unset = chip8.vram.xor(px, py, pixel);

                int vf = chip8.register.get(Register.REGISTER_FLAG);
                vf |= unset;

                chip8.register.set(Register.REGISTER_FLAG, vf);
            }
        }
    }
}

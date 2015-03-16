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
package mx.uach.fing.chip8;

/**
 *
 * @author UACH <http://fing.uach.mx>
 */
public class OPCode {

    // Codigo de operacion (Operation Code), almacena 2 bytes.
    private final int code;

    public OPCode(int code) {
        this.code = code;
    }

    /**
     * Obtiene el codigo de operacion completo.
     *
     * @return codigo de operacion.
     */
    public int get() {
        return this.code & 0xFFFF;
    }

    /**
     * Obtiene la instruccion, los 4 bits mas altos del primer byte de la
     * instruccion.
     *
     * @return instruccion.
     */
    public int getInstruction() {
        return (this.code >> 0xC) & 0xF;
    }

    /**
     * Obtiene una direccion de memoria, los 12 bits mas bajos de la
     * instruccion.
     *
     * @return direccion de memoria.
     */
    public int getAddress() {
        return this.code & 0xFFF;
    }

    /**
     * Obtiene un valor, los 4 bits mas bajos del segundo byte de la
     * instruccion.
     *
     * @return valor de 4 bits.
     */
    public int getNibble() {
        return this.code & 0xF;
    }

    /**
     * Obtiene un valor, los 4 bits mas bajos del primer byte de la instruccion.
     *
     * @return valor de 4 bits.
     */
    public int getX() {
        return (this.code >> 0x8) & 0xF;
    }

    /**
     * Obtiene un valor, los 4 bits mas altos del segundo byte de la
     * instruccion.
     *
     * @return valor de 4 bits.
     */
    public int getY() {
        return (this.code >> 0x4) & 0xF;
    }

    /**
     * Obtiene un byte, los 8 bits mas bajos de la instruccion.
     *
     * @return byte.
     */
    public int getByte() {
        return this.code & 0xFF;
    }

    @Override
    public String toString() {
        return String.format("%x%x%x%x", this.getInstruction(), this.getX(), this.getY(), this.getNibble()).toUpperCase();
    }
}

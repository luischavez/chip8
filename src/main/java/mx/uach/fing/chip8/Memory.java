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
package mx.uach.fing.chip8;

/**
 *
 * @author UACH <http://fing.uach.mx>
 */
public class Memory {

    // Tamano de la memoria en bytes.
    public static final int MEMORY_SIZE = 4096;

    // Tamano maximo de los programas.
    public static final int MAX_PROGRAM_SIZE = 3584;

    // Inicio de la memoria.
    public static final int MEMORY_INIT = 0x0;

    // Inicio de la fuente.
    public static final int FONT_INIT = 0x50;

    // Reservada para el interprete, 0x1FF (511).
    public static final int INTERPRETER_END = 0x1FF;

    // Inicio de los programas en memoria, 0x200 (512).
    public static final int PROGRAM_INIT = 0x200;

    // Inicio de los programas (ETI) en memoria, 0x600 (1536).
    public static final int PROGRAM_ETI_INIT = 0x600;

    // Fin de los programas en memoria, 0xFFF (4095).
    public static final int PROGRAM_END = 0xFFF;

    // Arreglo que representa la memoria en bytes.
    private final int[] ram;

    // Indice de la primera instruccion del programa.
    private int programIndex;

    public Memory() {
        this.ram = new int[MEMORY_SIZE];
    }

    /**
     * Obtiene el indice de la primera instruccion del programa.
     *
     * @return indice de la primera instruccion.
     */
    public int getProgramIndex() {
        return this.programIndex;
    }

    /**
     * Carga la fuente en memoria.
     */
    public void loadFont() {
        // Fuente en hexadecimal.
        char[] fonts = new char[]{
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80, // F
        };

        // Copia la fuente en memoria.
        for (int i = 0; i < fonts.length; i++) {
            this.ram[FONT_INIT + i] = fonts[i] & 0xFF;
        }
    }

    /**
     * Carga una ROM en memoria.
     *
     * @param data bytes a cargar.
     * @throws MemoryLoadException si el tamano de la ROM es demasiado grande.
     */
    public void load(byte[] data) throws MemoryLoadException {
        // Verifica que el tamano de la ROM no sea mayor al tamano maximo de los programas.
        if (MAX_PROGRAM_SIZE < data.length) {
            throw new MemoryLoadException(String.format("El tamano de la ROM es demasiado grande, el tamano maximo es: %d", MAX_PROGRAM_SIZE));
        }

        // Establece el tipo de programa dependiendo del tamano de los datos.
        this.programIndex = (PROGRAM_END - PROGRAM_ETI_INIT) == data.length
                ? PROGRAM_ETI_INIT : PROGRAM_INIT;

        // Almacena la ROM en memoria.
        int currentIndex = this.programIndex;
        for (int i = 0; i < data.length; i++) {
            this.ram[currentIndex++] = data[i];
        }
    }

    /**
     * Lee un byte de memoria almacenado en la direccion especificada.
     *
     * @param address direccion de la cual leer.
     * @return byte almacenado.
     */
    public int read(int address) throws InvalidMemoryAddressException {
        // Verifica si la direccion es valida.
        if (MEMORY_INIT > address || PROGRAM_END < address) {
            throw new InvalidMemoryAddressException(String.format("La direccion 0x%x no es valida", address));
        }

        return this.ram[address];
    }

    /**
     * Almacena un valor en memoria.
     *
     * @param address direccion donde almacenar el valor.
     * @param b valor a almacenar.
     * @throws InvalidMemoryAddressException si la direccion de memoria no es
     * valida.
     */
    public void set(int address, int b) throws InvalidMemoryAddressException {
        // Verifica si la direccion es valida.
        if (MEMORY_INIT > address || PROGRAM_END < address) {
            throw new InvalidMemoryAddressException(String.format("La direccion 0x%x no es valida", address));
        }

        this.ram[address] = b & 0xFF;
    }

    /**
     * Lee una instruccion de la memoria, cada instruccion tiene un tamano de 2
     * bytes.
     *
     * @param address direccion de la cual leer.
     * @return instruccion almacenada.
     * @throws InvalidMemoryAddressException si la direccion de memoria no es
     * valida.
     */
    public OPCode readInstruction(int address) throws InvalidMemoryAddressException {
        // Verifica si la direccion es valida.
        if (this.programIndex > address || PROGRAM_END < address) {
            throw new InvalidMemoryAddressException(String.format("La direccion 0x%x no es valida", address));
        }

        int b1 = this.read(address);
        int b2 = this.read(address + 1);

        // Se combinan los dos bytes en uno solo, formando una instruccion de 2 bytes.
        int instr = (b1 << 0x8) | (b2 & 0xFF);

        return new OPCode(instr);
    }
}

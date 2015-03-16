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

import mx.uach.fing.chip8.instruction.Instruction;
import mx.uach.fing.chip8.instruction.InstructionSet;

/**
 *
 * @author UACH <http://fing.uach.mx>
 */
public class Chip8 implements Runnable {

    // Frecuencia de actualizacion.
    public static final int HZ = 60;

    // Memoria RAM del chip con 4KB de almacenamiento.
    private final Memory memory;

    // Memoria de video.
    private final VRAM vram;

    // Pila donde se almacenaran las direcciones de las rutinas.
    private final Stack stack;

    // Registro donde se almacenaran las variables.
    private final Register register;

    // Teclado virtual.
    private final Keyboard keyboard;

    // Set de instrucciones del chip.
    private final InstructionSet instructionSet;

    // Bandera que indica que el chip esta corriendo.
    private boolean running = false;

    public Chip8() {
        this.memory = new Memory();
        this.vram = new VRAM();
        this.stack = new Stack();
        this.register = new Register();
        this.keyboard = new Keyboard();
        this.instructionSet = new InstructionSet();
    }

    /**
     * Obtiene la memoria del chip.
     *
     * @return memoria.
     */
    public Memory memory() {
        return this.memory;
    }

    /**
     * Obtiene la memoria de video del chip.
     *
     * @return memoria de video.
     */
    public VRAM vram() {
        return this.vram;
    }

    /**
     * Obtiene la pila del chip.
     *
     * @return pila.
     */
    public Stack stack() {
        return this.stack;
    }

    /**
     * Obtiene el registro del chip.
     *
     * @return registro.
     */
    public Register register() {
        return this.register;
    }

    /**
     * Obtiene el teclado.
     *
     * @return teclado.
     */
    public Keyboard keyboard() {
        return this.keyboard;
    }

    /**
     * Guarda una rom en memoria y establece el contrador del programa al inicio
     * del programa.
     *
     * @param rom arreglo de bytes con la rom a cargar.
     */
    public void loadMemory(byte[] rom) {
        this.memory.load(rom);
        this.register.setPC(this.memory.getProgramIndex());
    }

    /**
     * Avanza un paso la logica del chip.
     */
    public void step() {
        int st = this.register.getST();

        if (!this.keyboard.isWaiting()) {
            int pc = this.register.getPC();
            OPCode opcode = this.memory.readInstruction(pc);
            this.register.incrementPC();

            Instruction instruction = this.instructionSet.resolve(opcode);

            System.out.printf("Address: %x, OPCode: %s, PC: %d, Instruction: %s\n", pc, opcode.toString(), pc, instruction.getClass());
            instruction.execute(opcode, this.memory, this.vram, this.stack, this.register, this.keyboard);
        }

        if (0 < st) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Decrementa los contadores del programa.
     */
    public void decrementCounters() {
        this.register.decrementDT();
        this.register.decrementST();
    }

    /**
     * Detiene la ejecucion del chip.
     */
    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        final double FREQUENCY = 1_000.0 / HZ;

        double delta = 0f;

        long lastTime = System.currentTimeMillis();
        long currentTime;

        double cycles = 0f;

        this.running = true;
        while (this.running) {
            currentTime = System.currentTimeMillis();

            delta += (currentTime - lastTime);
            cycles += ((currentTime - lastTime) / 1_000.0);

            if (1 <= cycles) {
                cycles--;
                this.step();
            }

            while (FREQUENCY <= delta) {
                delta -= FREQUENCY;
                this.decrementCounters();
            }

            lastTime = System.currentTimeMillis();
        }
    }
}

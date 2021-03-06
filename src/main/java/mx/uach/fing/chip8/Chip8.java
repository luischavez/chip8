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
package mx.uach.fing.chip8;

import mx.uach.fing.chip8.instruction.Instruction;
import mx.uach.fing.chip8.instruction.InstructionSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Chávez
 */
public class Chip8 implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chip8.class);

    // Frecuencia de actualizacion de los contadores.
    public static final int TIMER_HZ = 60;

    // Frecuencia de actualizacion del reloj.
    public static final int UPDATE_HZ = 500;

    // Memoria RAM del chip con 4KB de almacenamiento.
    public final Memory memory;

    // Memoria de video.
    public final VRAM vram;

    // Pila donde se almacenaran las direcciones de las rutinas.
    public final Stack stack;

    // Registro donde se almacenaran las variables.
    public final Register register;

    // Teclado virtual.
    public final Keyboard keyboard;

    // Set de instrucciones del chip.
    private final InstructionSet instructionSet;

    // Bandera que indica que el chip esta corriendo.
    private boolean running = false;

    // Bandera que indica si se esta ejecutando en modo extendido.
    private boolean extended = true;

    public Chip8() {
        this.memory = new Memory();
        this.vram = new VRAM();
        this.stack = new Stack();
        this.register = new Register();
        this.keyboard = new Keyboard();
        this.instructionSet = new InstructionSet();
    }

    /**
     * Retorna el estado de la ejecucion del emulador.
     *
     * @return true si se esta ejecutando, false de otra manera.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Retorna el modo de ejecucion del emulador.
     *
     * @return true si esta en modo extendido, false de otra manera.
     */
    public boolean isExtended() {
        return extended;
    }

    /**
     * Establece el modo de ejecucion.
     *
     * @param extended true para el modo extendido, false de otra manera.
     */
    public void setExtended(boolean extended) {
        this.extended = extended;
        this.vram.setMode(extended);
    }

    /**
     * Guarda una rom en memoria y establece el contrador del programa al inicio
     * del programa.
     *
     * @param rom arreglo de bytes con la rom a cargar.
     */
    public void loadMemory(byte[] rom) {
        LOGGER.debug("Loading ROM: {}", rom);
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

            //LOGGER.debug("Address: {}, OPCode: {}, PC: {}, Instruction: {}\n",
            //        pc, opcode.toString(), pc, instruction.getClass());
            instruction.execute(opcode, this);
        }

        if (0 < st) {
            //java.awt.Toolkit.getDefaultToolkit().beep();
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
        LOGGER.debug("Stopping CHIP 8");
        this.running = false;
    }

    @Override
    public void run() {
        LOGGER.debug("Running CHIP 8");
        final double TIMER_FREQUENCY = 1_000.0 / TIMER_HZ;
        final double UPDATE_FREQUENCY = 1_000.0 / UPDATE_HZ;

        double delta = 0f;

        long lastTime = System.currentTimeMillis();
        long currentTime;

        long timer = 0;

        double cycles = 0f;

        int updates = 0;
        int renders = 0;

        this.running = true;
        while (this.running) {
            currentTime = System.currentTimeMillis();

            delta += currentTime - lastTime;
            cycles += currentTime - lastTime;

            if (UPDATE_FREQUENCY <= cycles) {
                updates++;
                cycles -= UPDATE_FREQUENCY;

                this.step();
            }

            if (TIMER_FREQUENCY <= delta) {
                renders++;
                delta -= TIMER_FREQUENCY;
                this.vram.draw();
                this.decrementCounters();
            }

            timer += currentTime - lastTime;
            if (1_000 <= timer) {
                //LOGGER.debug("updates: {}, renders: {}", updates, renders);
                timer = 0;
                updates = 0;
                renders = 0;
            }

            lastTime = currentTime;
        }
    }
}

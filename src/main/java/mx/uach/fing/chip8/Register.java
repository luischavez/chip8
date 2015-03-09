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
public class Register {

    // Tamano del registro.
    public static final int REGISTER_SIZE = 16;

    // Registro especial, utilizado como bandera (flag, true o false).
    public static final int REGISTER_FLAG = 0xF;

    // Bandera que indica apagado.
    public static final int BIT_0 = 0;

    // Bandera que indica encendido.
    public static final int BIT_1 = 1;

    // Bandera que indica que se genero acarreo.
    public static final int CARRY = 1;

    // Bandera que indica que no se genero acarreo.
    public static final int NOT_CARRY = 0;

    // Bandera que indica que se desbordo una variable.
    public static final int BORROW = 0;

    // Bandera que indica que no se desbordo una variable.
    public static final int NOT_BORROW = 1;

    // Bandera que indica que ocurrio una colision.
    public static final int COLLISION = 1;

    // Bandera que indica que no ocurrio una colision.
    public static final int NOT_COLLISION = 0;

    // Valores almacenados en el registro.
    private final int[] values;

    // Registro especial, usualmente se utiliza para almacenar direcciones de memoria.
    private int i;

    // Tiempo de espera (delay time), es utilizado para cuentas regresivas.
    private int dt;

    // Temporizador de sonido (Sound timer), es utilizado para emitir sonidos.
    private int st;

    // Puntero del programa (Program pointer), tiene un paso de 2 bytes.
    private int pc;

    public Register() {
        this.values = new int[REGISTER_SIZE];
    }

    /**
     * Obtiene el valor del registro I.
     *
     * @return valor del registro I.
     */
    public int getRegisterI() {
        return this.i;
    }

    /**
     * Establece el valor del reistro I.
     *
     * @param i valor a almacenar.
     */
    public void setRegisterI(int i) {
        this.i = i & 0xFFFF;
    }

    /**
     * Obtiene el valor del tiempo de espera dt.
     *
     * @return valor del tiempo de espera.
     */
    public int getDT() {
        return this.dt;
    }

    /**
     * Establece el valor del tiempo de espera dt.
     *
     * @param dt nuevo valor de dt.
     * @throws RegisterException si dt es 0.
     */
    public void setDT(int dt) throws RegisterException {
        if (0 > dt) {
            throw new RegisterException("El valor de dt no puede ser menor de 0");
        }

        this.dt = dt & 0xFF;
    }

    /**
     * Decrementa el valor del tiempo de espera dt.
     *
     * @return nuevo valor decrementado de dt.
     */
    public int decrementDT() {
        if (0 < this.dt) {
            this.dt--;
        }

        return this.dt;
    }

    /**
     * Obtiene el valor del temporizador de sonido st.
     *
     * @return valor del temporizador de sonido.
     */
    public int getST() {
        return this.st;
    }

    /**
     * Establece el valor del temporizador del sondio st.
     *
     * @param st nuevo valor de st.
     * @throws RegisterException si st es 0.
     */
    public void setST(int st) throws RegisterException {
        if (0 > st) {
            throw new RegisterException("El valor de st no puede ser menor de 0");
        }

        this.st = st & 0xFF;
    }

    /**
     * Decrementa el valor del temporizador del sonido st.
     *
     * @return nuevo valor decrementado de st.
     */
    public int decrementST() {
        if (0 < this.st) {
            this.st--;
        }

        return this.st;
    }

    /**
     * Obtiene el valor del contador del programa pc.
     *
     * @return valor del contador del programa.
     */
    public int getPC() {
        return this.pc;
    }

    /**
     * Establece el valor del contador del programa pc.
     *
     * @param pc nuevo valor de pc.
     */
    public void setPC(int pc) {
        this.pc = pc & 0xFFFF;
    }

    /**
     * Incrementa el valor del contador del programa pc.
     */
    public void incrementPC() {
        this.pc = (this.pc + 2) & 0xFFFF;
    }

    /**
     * Obtiene el valor almacenado en el indice especificado.
     *
     * @param index indice del cual obtener el valor.
     * @return valor almacenado.
     * @throws InvalidRegisterIndexException si el indice no es valido.
     */
    public int get(int index) throws InvalidRegisterIndexException {
        // Verifica si el indice del registro es valido.
        if (0 > index || REGISTER_SIZE - 1 < index) {
            throw new InvalidRegisterIndexException(String.format("El indice %d no es valido", index));
        }

        return this.values[index];
    }

    /**
     * Establece un valor en el indice especificado.
     *
     * @param index indice en el cual almacenar el valor.
     * @param value valor a almacenar.
     * @throws InvalidRegisterIndexException si el indice no es valido.
     */
    public void set(int index, int value) throws InvalidRegisterIndexException {
        // Verifica si el indice del registro es valido.
        if (0 > index || REGISTER_SIZE - 1 < index) {
            throw new InvalidRegisterIndexException(String.format("El indice %d no es valido", index));
        }

        this.values[index] = value & 0xFF;
    }

    /**
     * Copia un valor de un indice a otro.
     *
     * @param from direccion del valor a copiar.
     * @param to direccion donde almacenar la copia.
     */
    public void copy(int from, int to) {
        this.set(to, this.get(from));
    }
}

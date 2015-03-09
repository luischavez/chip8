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
public class Stack {

    // Tamano de la pila.
    public static final int STACK_SIZE = 16;

    // Arreglo con las direcciones contenidas dentro de la pila.
    private final int[] addresses;

    // Puntero de la pila (Stack pointer), indica la posicion actual. 
    private byte sp;

    public Stack() {
        this.addresses = new int[STACK_SIZE];
    }

    /**
     * Obtiene la direccion almacenada en el tope de la pila, luego decrementa
     * el valor del puntero.
     *
     * @return direccion almacenada en el tope de la pila.
     * @throws StackOverflowException si la pila esta vacia.
     */
    public int pop() throws StackOverflowException {
        // Verifica si la pila esta vacia, si esta vacia lanza un error.
        if (0 == this.sp) {
            throw new StackOverflowException("La pila esta vacia, no se puede obtener el valor solicitado");
        }

        // Obtiene la direccion y posteriormente decrementa el puntero.
        return this.addresses[--this.sp];
    }

    /**
     * Almacena una direccion en el tope de la pila, previamente aumenta el
     * valor del puntero.
     *
     * @param address direccion a almacenar.
     * @throws StackOverflowException si la pila esta llena.
     */
    public void push(int address) throws StackOverflowException {
        // Verifica si la pila esta llena, si esta llena lanza un error.
        if (STACK_SIZE == this.sp) {
            throw new StackOverflowException(String.format("La pila esta llena, no se puede almacenar el valor %x", address));
        }

        // Almancena la direccion pero previamente incrementa el puntero.
        this.addresses[this.sp++] = address & 0xFFFF;
    }
}

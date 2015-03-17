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

import java.util.Arrays;

/**
 *
 * @author Luis Chávez
 */
public class VRAM {

    // Ancho de la pantalla.
    public static final int SCREEN_WIDTH = 64;

    // Alto de la pantalla.
    public static final int SCREEN_HEIGHT = 32;

    // Tamano del buffer.
    public static final int BUFFER_SIZE = SCREEN_WIDTH * SCREEN_HEIGHT;

    // Ancho de los sprites.
    public static final int SPRITE_WIDTH = 8;

    // Alto de los sprites.
    public static final int SPRITE_HEIGHT = 15;

    // Arreglo que representa la pantalla.
    private final int[] buffer;

    // Funcion que se aplicara cuando se cambie un pixel.
    private BufferListener listener;

    public VRAM() {
        this.buffer = new int[BUFFER_SIZE];
    }

    /**
     * Establece el listener para los eventos de cambio de pixel.
     *
     * @param listener listener a agregar.
     */
    public void setListener(BufferListener listener) {
        this.listener = listener;
    }

    /**
     * Obtiene un arreglo unidimensional con los bytes de video.
     *
     * @return arreglo unidimensional con los bytes de video.
     */
    public int[] getBuffer() {
        return this.buffer;
    }

    /**
     * Guarda un pixel, en las coordenadas (x, y) y retorna un byte indicando si
     * se apago un pixel.
     *
     * @param x coordenada x.
     * @param y coordenada y.
     * @param pixel pixel a guardar.
     * @return 1 si se apago un pixel, 0 de otra manera.
     * @throws ScreenPointOutOfBoundsException si el punto (x, y) no es valido.
     */
    public int xor(int x, int y, int pixel) throws ScreenPointOutOfBoundsException {
        // Verifica que el punto (x, y) sea valido.
        if ((0 > x || SCREEN_WIDTH < x) || (0 > y || SCREEN_HEIGHT < y)) {
            throw new ScreenPointOutOfBoundsException(String.format("El punto (%d, %d) no es valido en la pantalla", x, y));
        }

        // Transforma el punto (x, y) en un indice de una dimension.
        int index = VRAM.toIndex(x, y);

        // Verifica si se eliminara un pixel en pantalla.
        int unset = this.buffer[index] & pixel;

        this.buffer[index] ^= pixel;

        return unset;
    }

    /**
     * Limpia la pantalla.
     */
    public void clear() {
        Arrays.fill(this.buffer, (byte) 0);
    }

    /**
     * Solicita dibujar el buffer en pantalla.
     */
    public void draw() {
        if (null != this.listener) {
            this.listener.onDraw(this.buffer);
        }
    }

    /**
     * Transforma un punto (x, y) en un indice.
     *
     * @param x punto x.
     * @param y punto y.
     * @return indice.
     */
    public static int toIndex(int x, int y) {
        return SCREEN_WIDTH * y + x;
    }

    /**
     * Transforma un indice es un punto x.
     *
     * @param index indice.
     * @return punto x.
     */
    public static int toPointX(int index) {
        return index % VRAM.SCREEN_WIDTH;
    }

    /**
     * Transforma un indice es un punto y.
     *
     * @param index indice.
     * @return punto y.
     */
    public static int toPointY(int index) {
        return index / VRAM.SCREEN_WIDTH;
    }

    /**
     * Listener para la escucha de eventos de video.
     */
    public interface BufferListener {

        /**
         * Se activa cuando se solicita dibujar el contenido del buffer.
         *
         * @param buffer buffer con el contenido a dibujar.
         */
        public void onDraw(int[] buffer);
    }
}

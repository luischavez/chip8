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

    // Ancho de la pantalla en modo estandar.
    public static final int STANDARD_SCREEN_WIDTH = 64;

    // Alto de la pantalla en modo estandar.
    public static final int STANDARD_SCREEN_HEIGHT = 32;

    // Ancho de los sprites en modo estandar.
    public static final int STANDARD_SPRITE_WIDTH = 8;

    // Alto de los sprites en modo estandar.
    public static final int STANDARD_SPRITE_HEIGHT = 16;

    // Ancho de la pantalla en modo estandar.
    public static final int EXTENDED_SCREEN_WIDTH = 128;

    // Alto de la pantalla en modo estandar.
    public static final int EXTENDED_SCREEN_HEIGHT = 64;

    // Ancho de los sprites en modo estandar.
    public static final int EXTENDED_SPRITE_WIDTH = 16;

    // Alto de los sprites en modo estandar.
    public static final int EXTENDED_SPRITE_HEIGHT = 16;

    // Ancho de la pantalla.
    private int screenWidth;

    // Alto de la pantalla.
    private int screenHeight;

    // Ancho de los sprites.
    private int spriteWidth;

    // Alto de los sprites.
    private int spriteHeight;

    // Tamano del buffer.
    private int bufferSize;

    // Arreglo que representa la pantalla.
    private int[] buffer;

    // Funcion que se aplicara cuando se cambie un pixel.
    private BufferListener listener;

    /**
     * Inicializa la memoria de video en modo estandar.
     */
    public VRAM() {
        setMode(false);
    }

    /**
     * Obtiene el ancho de la pantalla.
     *
     * @return ancho de la pantalla.
     */
    public int screenWidth() {
        return this.screenWidth;
    }

    /**
     * Obtiene el alto de la pantalla.
     *
     * @return alto de la pantalla.
     */
    public int screenHeight() {
        return this.screenHeight;
    }

    /**
     * Obtiene el ancho maximo de los sprites.
     *
     * @return ancho maximo de los sprites.
     */
    public int spriteWidth() {
        return this.spriteWidth;
    }

    /**
     * Obtiene el alto maximo de los sprites.
     *
     * @return alto maximo de los sprites.
     */
    public int spriteHeight() {
        return this.spriteHeight;
    }

    /**
     * Obtiene el tamano de la memoria de video.
     *
     * @return tamano de la memoria de video.
     */
    public int bufferSize() {
        return this.bufferSize;
    }

    /**
     * Obtiene un arreglo unidimensional con los bytes de video.
     *
     * @return arreglo unidimensional con los bytes de video.
     */
    public int[] buffer() {
        return this.buffer;
    }

    /**
     * Establece el modo de video.
     *
     * @param extended true para el modo extendido, false para el estandar.
     */
    public final void setMode(boolean extended) {
        this.screenHeight = extended
                ? EXTENDED_SCREEN_HEIGHT : STANDARD_SCREEN_HEIGHT;
        this.screenWidth = extended
                ? EXTENDED_SCREEN_WIDTH : STANDARD_SCREEN_WIDTH;
        this.spriteHeight = extended
                ? EXTENDED_SPRITE_HEIGHT : STANDARD_SPRITE_HEIGHT;
        this.spriteWidth = extended
                ? EXTENDED_SPRITE_WIDTH : STANDARD_SPRITE_WIDTH;
        this.bufferSize = this.screenHeight * this.screenWidth;
        this.buffer = new int[this.bufferSize];
        if (null != this.listener) {
            this.listener.onModeChanged(extended);
        }
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
     * Obtiene el pixel en la posicion especificada.
     *
     * @param x coordenada x.
     * @param y coordenada y.
     * @return pixel.
     */
    public int pixel(int x, int y) {
        int index = toIndex(x, y);
        return this.buffer[index];
    }

    /**
     * Guarda un pixel en la posicion especificada.
     *
     * @param x coordenada x.
     * @param y coordenada y.
     * @param pixel pixel a guardar.
     */
    public void pixel(int x, int y, int pixel) {
        int index = toIndex(x, y);
        this.buffer[index] = pixel;
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
    public int xor(int x, int y, int pixel)
            throws ScreenPointOutOfBoundsException {
        // Verifica que el punto (x, y) sea valido.
        if ((0 > x || screenWidth < x) || (0 > y || screenHeight < y)) {
            throw new ScreenPointOutOfBoundsException(
                    String.format("El punto (%d, %d) "
                            + "no es valido en la pantalla", x, y));
        }

        // Transforma el punto (x, y) en un indice de una dimension.
        int index = toIndex(x, y);

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
    public int toIndex(int x, int y) {
        return screenWidth * y + x;
    }

    /**
     * Transforma un indice es un punto x.
     *
     * @param index indice.
     * @return punto x.
     */
    public int toPointX(int index) {
        return index % screenWidth;
    }

    /**
     * Transforma un indice es un punto y.
     *
     * @param index indice.
     * @return punto y.
     */
    public int toPointY(int index) {
        return index / screenWidth;
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

        /**
         * Se activa cuando se cambia de modo.
         *
         * @param extended true si es modo extendido, false para modo estandar.
         */
        public void onModeChanged(boolean extended);
    }
}

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

/**
 *
 * @author Luis Chávez
 */
public class Keyboard {

    // Estado de las teclas, de 0x0 a 0xF.
    private final boolean[] keys;

    // Indica si se espera por la pulsacion de una tecla.
    private boolean wait;

    // Funcion que se aplicara cuando se encuentre una tecla.
    private KeyListener listener;

    public Keyboard() {
        this.keys = new boolean[16];
        this.wait = false;
        this.listener = null;
    }

    /**
     * Verifica si el teclado esta esperando por una tecla.
     *
     * @return true si esta esperando una tecla, false de otra manera.
     */
    public boolean isWaiting() {
        return this.wait;
    }

    /**
     * Indica que se espera por la pulsacion de una tecla.
     *
     * @param listener funcion que se aplica cuando se pula una tecla.
     */
    public void waitKey(KeyListener listener) {
        this.wait = true;
        this.listener = listener;
    }

    /**
     * Establece el estado de la tecla, si esta pulsada o no.
     *
     * @param key tecla a modificar.
     * @param down estado de la tecla, true si esta pulsada, false de otra
     * manera.
     * @throws InvalidKeyException si la tecla no es valida.
     */
    public void setKeyStatus(int key, boolean down) throws InvalidKeyException {
        // Verifica si la tecla es valida.
        if (0 > key || 0xF < key) {
            throw new InvalidKeyException(String.format("La tecla %x no es valida", key));
        }

        this.keys[key] = down;

        // Verifica si se esta esperando por la tecla, si esta pulada la guarda.
        if (down && this.wait && null != listener) {
            this.wait = false;
            this.listener.onKeyFound(key);
            this.listener = null;
        }
    }

    /**
     * Establece el estado de la tecla como no pulsada.
     *
     * @param key tecla a modificar.
     */
    public void up(int key) {
        this.setKeyStatus(key, false);
    }

    /**
     * Establece el estado de la tecla a pulsada.
     *
     * @param key tecla a modificar.
     */
    public void down(int key) {
        this.setKeyStatus(key, true);
    }

    /**
     * Verifica si la tecla esta pulsada.
     *
     * @param key tecla a verificar.
     * @return true si la tecla esta pulsada, false de otra manera.
     * @throws InvalidKeyException si la tecla no es valida.
     */
    public boolean isKeyDown(int key) throws InvalidKeyException {
        // Verifica si la tecla es valida.
        if (0 > key || 0xF < key) {
            throw new InvalidKeyException(String.format("La tecla %x no es valida", key));
        }

        return this.keys[key];
    }

    /**
     * Listener para la escucha de eventos de teclado.
     */
    public interface KeyListener {

        /**
         * Se activa cuando se encuentra la pulsacion de una tecla.
         *
         * @param key tecla encontrada.
         */
        public void onKeyFound(int key);
    }
}

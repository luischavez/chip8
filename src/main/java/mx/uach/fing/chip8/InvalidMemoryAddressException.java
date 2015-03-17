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
public class InvalidMemoryAddressException extends RuntimeException {

    public InvalidMemoryAddressException() {
    }

    public InvalidMemoryAddressException(String message) {
        super(message);
    }

    public InvalidMemoryAddressException(Throwable cause) {
        super(cause);
    }

    public InvalidMemoryAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}

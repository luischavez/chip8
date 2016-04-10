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
package mx.uach.fing.chip8.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Luis Chávez
 */
public class MemoryUtils {

    /**
     * Lee un stream y obtiene un arreglo con los bytes.
     *
     * @param inputStream stream a leer.
     * @return bytes contenidos.
     * @throws java.io.IOException si ocurre un error al manejar el stream.
     */
    public static byte[] toBytes(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        outputStream.close();

        byte[] bytes = outputStream.toByteArray();

        return bytes;
    }
}

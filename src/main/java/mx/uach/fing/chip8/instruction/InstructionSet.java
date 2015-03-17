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
package mx.uach.fing.chip8.instruction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import mx.uach.fing.chip8.OPCode;

/**
 *
 * @author Luis Chávez
 */
public class InstructionSet {

    // Mapa con el set de instrucciones a utilizar.
    private final Map<Pattern, Instruction> instructionMap;

    public InstructionSet() {
        this.instructionMap = new HashMap<>();

        this.sets();
    }

    /**
     * Configura el set de instrucciones, utiliza expresiones regulares para
     * identificar el tipo de instruccion.
     */
    private void sets() {
        // 0nnn - SYS addr
        // this.instructionMap.put(Pattern.compile("^0[0-9a-fA-F]{3}$"), new SystemAddressInstruction());
        // 00E0 - CLS
        this.instructionMap.put(Pattern.compile("^00E0$"), new ClearScreenInstruction());
        // 00EE - RET
        this.instructionMap.put(Pattern.compile("^00EE$"), new ReturnSubroutineInstruction());
        // 1nnn - JP addr
        this.instructionMap.put(Pattern.compile("^1[0-9a-fA-F]{3}$"), new JumpNNNInstruction());
        // 2nnn - CALL addr
        this.instructionMap.put(Pattern.compile("^2[0-9a-fA-F]{3}$"), new CallSubroutineInstruction());
        // 3xkk - SE Vx, byte
        this.instructionMap.put(Pattern.compile("^3[0-9a-fA-F]{3}$"), new SkipEqualVxKKInstruction());
        // 4xkk - SNE Vx, byte
        this.instructionMap.put(Pattern.compile("^4[0-9a-fA-F]{3}$"), new SkipNotEqualVxKKInstruction());
        // 5xy0 - SE Vx, Vy
        this.instructionMap.put(Pattern.compile("^5[0-9a-fA-F]{2}0$"), new SkipEqualVxVyInstruction());
        // 6xkk - LD Vx, byte
        this.instructionMap.put(Pattern.compile("^6[0-9a-fA-F]{3}$"), new LoadVxKKInstruction());
        // 7xkk - ADD Vx, byte
        this.instructionMap.put(Pattern.compile("^7[0-9a-fA-F]{3}$"), new AddVxKKInstruction());
        // 8xy0 - LD Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}0$"), new LoadVxVyInstruction());
        // 8xy1 - OR Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}1$"), new OrVxVyInstruction());
        // 8xy2 - AND Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}2$"), new AndVxVyInstruction());
        // 8xy3 - XOR Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}3$"), new XorVxVyInstruction());
        // 8xy4 - ADD Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}4$"), new AddVxVyInstruction());
        // 8xy5 - SUB Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}5$"), new SubVxVyInstruction());
        // 8xy6 - SHR Vx {, Vy}
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}6$"), new ShrVxVyInstruction());
        // 8xy7 - SUBN Vx, Vy
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}7$"), new SubnVxVyInstruction());
        // 8xyE - SHL Vx {, Vy}
        this.instructionMap.put(Pattern.compile("^8[0-9a-fA-F]{2}E$"), new ShlVxVyInstruction());
        // 9xy0 - SNE Vx, Vy
        this.instructionMap.put(Pattern.compile("^9[0-9a-fA-F]{2}0$"), new SkipNotEqualVxVyInstruction());
        // Annn - LD I, addr
        this.instructionMap.put(Pattern.compile("^A[0-9a-fA-F]{3}$"), new LoadINNNInstruction());
        // Bnnn - JP V0, addr
        this.instructionMap.put(Pattern.compile("^B[0-9a-fA-F]{3}$"), new JumpV0NNNInstruction());
        // Cxkk - RND Vx, byte
        this.instructionMap.put(Pattern.compile("^C[0-9a-fA-F]{3}$"), new RandomVxKKInstruction());
        // Dxyn - DRW Vx, Vy, nibble
        this.instructionMap.put(Pattern.compile("^D[0-9a-fA-F]{3}$"), new DrawVxVyNibbleInstruction());
        // Ex9E - SKP Vx
        this.instructionMap.put(Pattern.compile("^E[0-9a-fA-F]{1}9E$"), new SkipIfKeyDownInstruction());
        // ExA1 - SKNP Vx
        this.instructionMap.put(Pattern.compile("^E[0-9a-fA-F]{1}A1$"), new SkipIfKeyUpInstruction());
        // Fx07 - LD Vx, DT
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}07$"), new LoadVxDTInstruction());
        // Fx0A - LD Vx, K
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}0A$"), new WaitKeyDownInstruction());
        // Fx15 - LD DT, Vx
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}15$"), new LoadDTVxInstruction());
        // Fx18 - LD ST, Vx
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}18$"), new LoadSTVxInstruction());
        // Fx1E - ADD I, Vx
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}1E$"), new AddIVxInstruction());
        // Fx29 - LD F, Vx
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}29$"), new LoadFontVxInstruction());
        // Fx33 - LD B, Vx
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}33$"), new LoadBCDInstruction());
        // Fx55 - LD [I], Vx
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}55$"), new LoadIVxInstruction());
        // Fx65 - LD Vx, [I]
        this.instructionMap.put(Pattern.compile("^F[0-9a-fA-F]{1}65$"), new LoadVxIInstruction());
    }

    /**
     * Resuelve la instruccion para el opcode solicitado.
     *
     * @param opcode opcode a resolver.
     * @return instruccion del opcode.
     * @throws UnknownInstructionException si no existe una instruccion para el
     * opcode.
     */
    public Instruction resolve(OPCode opcode) throws UnknownInstructionException {
        Iterator<Map.Entry<Pattern, Instruction>> iterator = this.instructionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Pattern, Instruction> entry = iterator.next();

            Pattern pattern = entry.getKey();
            if (pattern.matcher(opcode.toString()).matches()) {
                return entry.getValue();
            }
        }

        throw new UnknownInstructionException(String.format("No existe una instruccion para el opcode %s", opcode.toString()));
    }
}

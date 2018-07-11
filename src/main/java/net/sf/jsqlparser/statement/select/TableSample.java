/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2018 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package net.sf.jsqlparser.statement.select;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

import java.util.List;

public class TableSample extends ASTNodeAccessImpl {

    private final String samplingMethod;
    private final List<String> samplingMethodParams;
    private final String seed;

    public TableSample(String samplingMethod, List<String> samplingMethodParams, String seed) {
        this.samplingMethod = samplingMethod;
        this.samplingMethodParams = samplingMethodParams;
        this.seed = seed;
    }

    @Override
    public String toString() {
        if (samplingMethod != null) {
            StringBuffer buffer = new StringBuffer();
            String param;
            for (int i = 0; i < samplingMethodParams.size(); i++) {
                if (i > 0) {
                    buffer.append(",");
                }
                param = samplingMethodParams.get(i);
                if (param != null) {
                    buffer.append(param);
                }
            }

            if (buffer.length() <= 0) {
                return "";
            }

            return  "TABLESAMPLE " + samplingMethod.toUpperCase() + "(" + buffer.toString() + ") " + (seed != null ? "REPEATABLE(" + seed + ")" : "");
        }
        return "";
    }
}

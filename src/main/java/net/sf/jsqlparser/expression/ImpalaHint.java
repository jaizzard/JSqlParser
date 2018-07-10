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
package net.sf.jsqlparser.expression;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImpalaHint extends ASTNodeAccessImpl implements Expression {
    private String value = "";
    private Type type = null;

    private List<String> hints = new ArrayList<>();

    public enum Type {
        SINGLE_LINE(Pattern.compile("--\\s*\\+\\s*([^\\W\\n\\r]+(\\,[^\\W\\n\\r]+)*)"), "-- +%s\n"),
        MULTI_LINE(Pattern.compile("\\/\\*\\s*\\+\\s*(\\w+(\\,\\w+)*)\\s*\\*\\/"), "/* +%s */");

        private final Pattern pattern;
        private final String format;

        Type(Pattern pattern, String format) {
            this.pattern = pattern;
            this.format = format;
        }

        public Matcher match(String text) {
            return pattern.matcher(text);
        }

        public static Type matchedType(String text) {
            for (Type type : Type.values()) {
                if (type.match(text).find()) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return format;
        }
    }

    public static boolean isHintMatch(String comment) {
        return Type.matchedType(comment) != null;
    }

    public final void setComment(String comment) {
        Matcher m;
        for (Type type : Type.values()) {
            if ((m = type.match(comment)).find()) {
                this.type = type;
                this.value = m.group(1);
                break;
            }
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    public List<String> getHints() {
        return hints;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return (type != null) ? String.format(type.toString(), value.replaceAll("[^\\S\\r\\n]", "").toUpperCase()) : "";
    }
}
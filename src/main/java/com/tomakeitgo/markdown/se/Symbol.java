package com.tomakeitgo.markdown.se;

import java.util.Objects;

class Symbol implements SEPart {
    private final String value;

    public Symbol(String value) {this.value = value;}

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(value, symbol.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "value='" + value + '\'' +
                '}';
    }
}

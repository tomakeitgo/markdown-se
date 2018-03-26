package com.tomakeitgo.markdown.se;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Expression implements SEPart {

    private final List<SEPart> items = new ArrayList<>();

    public void add(SEPart item) {
        items.add(item);
    }

    public List<SEPart> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return "Expression{" +
                "items=" + items +
                '}';
    }
}

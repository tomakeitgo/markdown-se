package com.tomakeitgo.markdown.se;

import java.util.*;

import static java.util.stream.Collectors.joining;

class Evaluator {

    private static final Symbol DEFAULT_FUNCTION = new Symbol("___default___");

    private final Environment environment = new Environment();

    public Symbol eval(Expression expression) {
        Function outer = environment.lookup(DEFAULT_FUNCTION);
        LinkedList<Symbol> args = new LinkedList<>();

        for (SEPart sePart : expression.getItems()) {
            if (sePart instanceof Symbol) {
                args.add((Symbol) sePart);
            } else {
                args.add(evalInner((Expression) sePart));
            }
        }

        return outer.eval(args);
    }

    private Symbol evalInner(Expression expression) {
        Iterator<SEPart> items = expression.getItems().iterator();
        if (!items.hasNext()) {
            return new Symbol("");
        }

        SEPart toLookup = items.next();
        LinkedList<Symbol> args = new LinkedList<>();

        Function function;
        if (toLookup instanceof Symbol && environment.exists((Symbol) toLookup)) {
            function = environment.lookup((Symbol) toLookup);
        } else if (toLookup instanceof Expression) {
            function = environment.lookup(DEFAULT_FUNCTION);
            args.add(evalInner((Expression) toLookup));
        } else {
            function = environment.lookup(DEFAULT_FUNCTION);
            args.add((Symbol) toLookup);
        }

        while (items.hasNext()) {
            SEPart next = items.next();
            if (next instanceof Symbol)
                args.add((Symbol) next);
            else
                args.add(evalInner((Expression) next));
        }

        return function.eval(args);
    }

    public interface Function {
        Symbol eval(List<Symbol> args);
    }

    private static class Environment {
        private final Map<Symbol, Function> functions = new HashMap<>();

        public Environment() {
            functions.put(DEFAULT_FUNCTION,
                    expression -> new Symbol(expression.stream()
                            .map(Symbol::getValue)
                            .collect(joining(" ")))
            );
            functions.put(new Symbol("___define___"), new Function() {
                @Override
                public Symbol eval(List<Symbol> args) {
                    if (args.size() != 3) {
                        throw new IllegalArgumentException("Bad Definition");
                    }
                    functions.put(args.get(0), new DefinedFunction(args.get(1).getValue(), args.get(2).getValue()));
                    return new Symbol("");
                }
            });
        }

        public boolean exists(Symbol symbol) {
            return functions.containsKey(symbol);
        }

        public Function lookup(Symbol symbol) {
            return functions.get(symbol);
        }
    }

    static class DefinedFunction implements Function {

        private final String prefix;
        private final String suffix;

        DefinedFunction(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public Symbol eval(List<Symbol> args) {
            return new Symbol(args.stream()
                    .map(Symbol::getValue)
                    .collect(joining(" ", prefix, suffix)));
        }
    }
}

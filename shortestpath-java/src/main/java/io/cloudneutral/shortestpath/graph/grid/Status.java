package io.cloudneutral.shortestpath.graph.grid;

import java.awt.Color;

public enum Status {
    empty {
        @Override
        public Color color() {
            return Color.white;
        }
    }, closed {
        @Override
        public Color color() {
            return Color.gray;
        }
    }, visited {
        @Override
        public Color color() {
            return Color.blue;
        }
    }, path {
        @Override
        public Color color() {
            return Color.green;
        }
    }, start {
        @Override
        public Color color() {
            return Color.yellow;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }
    }, goal {
        @Override
        public Color color() {
            return Color.red;
        }

        @Override
        public boolean isTerminal() {
            return true;
        }
    }, obstacle {
        @Override
        public Color color() {
            return Color.black;
        }
    };

    public abstract Color color();

    public boolean isTerminal() {
        return false;
    }

    ;
}

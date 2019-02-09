package GameEngine;

import java.io.Serializable;

public enum eDiscType implements Serializable {
    black{
        @Override
        public String toString() {
            return "black";
        }
    },
    white {
        @Override
        public String toString() {
            return "white";
        }
    },
    blue {
        @Override
        public String toString() {
            return "blue";
        }
    }, green {
        @Override
        public String toString() {
            return "green";
        }
    }, red {
        @Override
        public String toString() {
            return "red";
        }
    }
}

package GameEngine;

import java.io.Serializable;

public enum eDiscType implements Serializable {
    BLACK {
        @Override
        public String toString() {
            return "Black";
        }
    },
    WHITE {
        @Override
        public String toString() {
            return "White";
        }
    },
    BLUE {
        @Override
        public String toString() {
            return "Blue";
        }
    }, GREEN {
        @Override
        public String toString() {
            return "Green";
        }
    }, RED {
        @Override
        public String toString() {
            return "Red";
        }
    }
}

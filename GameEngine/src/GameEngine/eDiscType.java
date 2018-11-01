package GameEngine;

public enum eDiscType {
    BLACK {
        @Override
        public String toString() {
            return "X";
        }
    },
    WHITE {
        @Override
        public String toString() {
            return "O";
        }
    },
    BLUE {
        @Override
        public String toString() {
            return "@";
        }
    }, GREEN {
        @Override
        public String toString() {
            return "$";
        }
    }, RED {
        @Override
        public String toString() {
            return "%";
        }
    }
}

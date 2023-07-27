package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Field {
    private final int gridSize = 10;
    private final char[][] field;
    private final String playerName;
    private Field opponentField;

    public Field(String playerName) {
        this.playerName = playerName;
        field = new char[gridSize][gridSize];
        for (char[] row : field) {
            Arrays.fill(row, '~');
        }
    }

    public void setOpponentField(Field opponentField) {
        this.opponentField = opponentField;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        char letter = 'A';
        out.append("  1 2 3 4 5 6 7 8 9 10\n");
        for (int i = 0; i < gridSize; i++) {
            out.append(letter);
            for (int j = 0; j < gridSize; j++) {
                out.append(" ");
                out.append(field[i][j]);
            }
            letter++;
            out.append("\n");
        }
        return out.toString();
    }

    public void print() {
        System.out.println(this);
    }

    public void printCompleteView() {
        opponentField.printFogged();
        System.out.println("---------------------");
        print();
    }

    public void placeShips() {
        System.out.println(playerName + ", place your ships on the game field");
        print();
        for (Ship ship : Ship.values()) {
            setShip(ship);
            print();
        }
    }

    public void setShip(Ship ship) {
        Scanner scanner = new Scanner(System.in);
        boolean pointsValid = false;
        Point startPoint = null;
        Point endPoint = null;
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getLength());
        while (!pointsValid) {
            startPoint = toPoint(scanner.next());
            endPoint = toPoint(scanner.next());
            try {
                pointsValid = validatePoints(startPoint, endPoint, ship);
            } catch (IllegalAccessException e) {
                System.out.println("Error! " + e.getMessage() + " Try again:");
            }
        }
        for (int i = Math.min(startPoint.y, endPoint.y); i <= Math.max(startPoint.y, endPoint.y); i++) {
            for (int j = Math.min(startPoint.x, endPoint.x); j <= Math.max(startPoint.x, endPoint.x); j++) {
                field[i][j] = 'O';
            }
        }
    }

    private Point toPoint(String str) {
        int y = str.charAt(0) - 'A';
        int x = Integer.parseInt(str.substring(1)) - 1;
        return new Point(x, y);
    }

    private boolean validatePoints(Point startPoint, Point endPoint, Ship ship) throws IllegalAccessException {
//        XNOR gate
        boolean wrongDirection = (startPoint.x == endPoint.x) == (startPoint.y == endPoint.y);
        if (wrongDirection) {
            throw new IllegalAccessException("Wrong ship location!");
        }

        int shipLength = Math.abs(startPoint.x - endPoint.x) + Math.abs(startPoint.y - endPoint.y) + 1;
        boolean wrongLength = shipLength != ship.getLength();
        if (wrongLength) {
            throw new IllegalAccessException("Wrong length of the ship " + ship.getName());
        }

        for (int i = Math.min(startPoint.y, endPoint.y) - 1; i <= Math.max(startPoint.y, endPoint.y) + 1; i++) {
            for (int j = Math.min(startPoint.x, endPoint.x) - 1; j <= Math.max(startPoint.x, endPoint.x) + 1; j++) {
                if (i >= 0 && i < gridSize && j >= 0 && j < gridSize && field[i][j] != '~') {
                    throw new IllegalAccessException("You placed it too close to another one.");
                }
            }
        }
        return true;
    }
    public void shoot() {
        printCompleteView();
        System.out.println(playerName + ", it's your turn:");
        opponentField.takeDamage();
    }
    public void takeDamage() {
        Scanner scanner = new Scanner(System.in);
        boolean pointValid = false;
        Point targetPoint = null;
        while (!pointValid) {
            targetPoint = toPoint(scanner.next());
            try {
                pointValid = validateTarget(targetPoint);
            } catch (IllegalAccessException e) {
                System.out.println("Error! " + e.getMessage() + " Try again:");
            }
        }
        if (field[targetPoint.y][targetPoint.x] == 'O' || field[targetPoint.y][targetPoint.x] == 'X') {
            field[targetPoint.y][targetPoint.x] = 'X';
//            opponentField.printCompleteView();
            if (isAnyNeighbourOccupied(targetPoint)) {
                System.out.println("You hit a ship!");
            } else if (shipExists()) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You sank the last ship. You won. Congratulations!");
            }
        } else {
            field[targetPoint.y][targetPoint.x] = 'M';
//            opponentField.printCompleteView();
            System.out.println("You missed!");
        }

    }

    private boolean validateTarget(Point point) throws IllegalAccessException {
        if (point.x < 0 || point.x >= gridSize || point.y < 0 || point.y >= gridSize) {
            throw new IllegalAccessException("You entered the wrong coordinates!");
        }
        return true;
    }

    private boolean isAnyNeighbourOccupied(Point point) {
        if (point.x - 1 >= 0 && field[point.y][point.x - 1] == 'O') {
            return true;
        } else if (point.x + 1 < gridSize && field[point.y][point.x + 1] == 'O') {
            return true;
        } else if (point.y - 1 >= 0 && field[point.y - 1][point.x] == 'O') {
            return true;
        } else if (point.y + 1 < gridSize && field[point.y + 1][point.x] == 'O') {
            return true;
        } else {
            return false;
        }
    }

    public void printFogged() {
        System.out.print(toString().replace('O', '~'));
    }

    public boolean shipExists() {
        return toString().contains("O");
    }
}

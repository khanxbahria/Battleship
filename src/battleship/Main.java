package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Field field1 = new Field("Player 1");
        Field field2 = new Field("Player 2");
        field1.setOpponentField(field2);
        field2.setOpponentField(field1);

        field1.placeShips();
        printSwitchPlayerPrompt();
        field2.placeShips();
        do {
            printSwitchPlayerPrompt();
            field1.shoot();
            printSwitchPlayerPrompt();
            field2.shoot();
        } while(field1.shipExists() || field2.shipExists());

    }
    public static void printSwitchPlayerPrompt() {
        System.out.println("Press Enter and pass the move to another player ");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}


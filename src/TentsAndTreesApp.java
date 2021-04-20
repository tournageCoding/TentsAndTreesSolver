import java.util.Scanner;

/*
 * Application class with a main method that creates a Tent object, reads
 * the input, solves the game and then prints the game to output.
 */

public class TentsAndTreesApp {
    public static void main(String[] args) {
        Tent t = new Tent();
        t.readInput();
        t.solve();
        t.printGame();

    }
}

import java.util.Scanner;

/*
 * Application class with a main method that creates a Tent object, reads
 * the input, solves the game and then prints the game to output.
 */

public class TentsAndTreesApp {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (scan.hasNextLine()) {
            String input = scan.nextLine();
            if (input.isEmpty()) {
                Tent t = new Tent();
                t.readInput(sb.toString());
                t.solve();
                t.printGame();
                System.out.println();
                sb = new StringBuilder();
            } else {
                sb.append(input + "\n");
            }
            i++;
        }

        Tent t = new Tent();
        t.readInput(sb.toString());
        t.solve();
        t.printGame();
                                 
    }
}

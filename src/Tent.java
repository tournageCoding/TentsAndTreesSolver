import java.util.*;

/*
 * A class to represent one game of Tents And Trees.
 */

public class Tent {
    private char[][] game;
    private int[] tentRow, tentColumn;
    private int numTrees;
    private Point[] trees;

    /*
     * Read the game input into a 2D array and the tent counts for rows and columns
     * into separate arrays.
     */
    public void readInput() {
        Scanner scan = new Scanner(System.in);
        String[] rowTemp, columnTemp;
        Stack<String> stack = new Stack<String>();

        while (scan.hasNextLine()) {
            stack.push(scan.nextLine());
        }
        scan.close();

        // send input to tentColumn
        columnTemp = stack.pop().split(" ");
        this.tentColumn = new int[columnTemp.length];
        for (int i = 0; i < columnTemp.length; i++) {
            if (this.isNumeric(columnTemp[i])) {
                this.tentColumn[i] = Integer.parseInt(columnTemp[i]);
            } else {
                System.out.println("Bad input format");
                return;
            }
        }

        // send input to tentRow
        rowTemp = stack.pop().split(" ");
        this.tentRow = new int[rowTemp.length];
        for (int i = 0; i < rowTemp.length; i++) {
            if (this.isNumeric(rowTemp[i])) {
                this.tentRow[i] = Integer.parseInt(rowTemp[rowTemp.length - 1 - i]);
            } else {
                System.out.println("Bad input format");
                return;
            }
            this.tentRow[i] = Integer.parseInt(rowTemp[rowTemp.length - 1 - i]);
            // flipped since input is given from bottom to top
        }

        // send input to game
        this.game = new char[tentColumn.length][tentRow.length];
        for (int y = tentRow.length - 1; y >= 0; y--) {
            String currentRow = stack.pop();
            for (int x = 0; x < tentColumn.length; x++) {
                this.game[x][y] = currentRow.charAt(x);
            }
        }
    }

    /*
     * Check if the puzzle is sovled by comparing the number of tents in each row
     * and column to the required number.
     */
    private boolean isSolved() {
        // check rows
        for (int y = 0; y < this.tentRow.length; y++) {
            int numTents = 0;
            for (int x = 0; x < this.tentColumn.length; x++) {
                if (this.game[x][y] == 'C') {
                    numTents++;
                }
            }
            if (numTents != this.tentRow[y]) {
                return false;
            }
        }
        // check columns
        for (int x = 0; x < this.tentColumn.length; x++) {
            int numTents = 0;
            for (int y = 0; y < this.tentRow.length; y++) {
                if (this.game[x][y] == 'C') {
                    numTents++;
                }
            }
            if (numTents != this.tentColumn[x]) {
                return false;
            }
        }
        return true;
    }

    /*
     * Print out a visual representation of the game.
     */
    public void printGame() {
        if (!this.isSolved()) {
            System.out.println("No solution");
            return;
        }
        for (int i = 0; i < this.tentRow.length; i++) {
            for (int j = 0; j < this.tentColumn.length; j++) {
                System.out.print(this.game[j][i]);
            }
            System.out.println();
        }
    }

    /*
     * Solves recursively by assigning a campsite to each tree and checks multiple
     * possibilities without violating rules.
     */
    private int recursiveSolver(int tCounter) {
        if (tCounter == this.numTrees) {
            return tCounter;
        }

        int x = this.trees[tCounter].getX();
        int y = this.trees[tCounter].getY();

        if (y > 0 && this.game[x][y - 1] == '.') {
            this.game[x][y - 1] = 'C';
            if (this.tentDoesNotExceedCount(x, y - 1) && this.tentNotAdjacentToOtherTent(x, y - 1)) {
                int result = this.recursiveSolver(tCounter + 1);
                if (result == this.numTrees) {
                    return result;
                }
            }
            this.game[x][y - 1] = '.';
        }

        if (x < this.tentColumn.length - 1 && this.game[x + 1][y] == '.') {
            this.game[x + 1][y] = 'C';
            if (this.tentDoesNotExceedCount(x + 1, y) && this.tentNotAdjacentToOtherTent(x + 1, y)) {
                int result = this.recursiveSolver(tCounter + 1);
                if (result == this.numTrees) {
                    return result;
                }
            }
            this.game[x + 1][y] = '.';
        }

        if (y < this.tentRow.length - 1 && this.game[x][y + 1] == '.') {
            this.game[x][y + 1] = 'C';
            if (this.tentDoesNotExceedCount(x, y + 1) && this.tentNotAdjacentToOtherTent(x, y + 1)) {
                int result = this.recursiveSolver(tCounter + 1);
                if (result == this.numTrees) {
                    return result;
                }
            }
            this.game[x][y + 1] = '.';
        }

        if (x > 0 && this.game[x - 1][y] == '.') {
            this.game[x - 1][y] = 'C';
            if (this.tentDoesNotExceedCount(x - 1, y) && this.tentNotAdjacentToOtherTent(x - 1, y)) {
                int result = this.recursiveSolver(tCounter + 1);
                if (result == this.numTrees) {
                    return result;
                }
            }
            this.game[x - 1][y] = '.';
        }

        return -1;
    }

    /*
     * Ensure placing the current campsite does not exceed the required row and
     * column count.
     */
    private boolean tentDoesNotExceedCount(int x, int y) {
        int tentCountColumn = 0;
        int tentCountRow = 0;
        for (int i = 0; i < this.tentRow.length; i++) {
            if (this.game[x][i] == 'C') {
                tentCountColumn++;
            }
        }
        if (tentCountColumn > this.tentColumn[x]) {
            return false;
        }

        for (int i = 0; i < this.tentColumn.length; i++) {
            if (this.game[i][y] == 'C') {
                tentCountRow++;
            }
        }
        if (tentCountRow > this.tentRow[y]) {
            return false;
        }

        return true;
    }

    /*
     * Ensure there are no adjacent tents to the current tent.
     */
    private boolean tentNotAdjacentToOtherTent(int x, int y) {
        boolean up = y > 0;
        boolean right = x < this.tentColumn.length - 1;
        boolean down = y < this.tentRow.length - 1;
        boolean left = x > 0;

        if (up && this.game[x][y - 1] == 'C') {
            return false;
        }
        if (right && this.game[x + 1][y] == 'C') {
            return false;
        }
        if (down && this.game[x][y + 1] == 'C') {
            return false;
        }
        if (left && this.game[x - 1][y] == 'C') {
            return false;
        }
        if (up && right && this.game[x + 1][y - 1] == 'C') {
            return false;
        }
        if (down && right && this.game[x + 1][y + 1] == 'C') {
            return false;
        }
        if (down && left && this.game[x - 1][y + 1] == 'C') {
            return false;
        }
        if (up && left && this.game[x - 1][y - 1] == 'C') {
            return false;
        }

        return true;
    }

    /*
     * Count the total number of trees in the puzzle.
     */
    private int countNumTrees() {
        int trees = 0;
        for (int x = 0; x < this.tentColumn.length; x++) {
            trees += this.tentColumn[x];
        }
        return trees;
    }

    /*
     * Create an array of the coordinates of all the trees in the puzzle.
     */
    private void initializeTreeArray() {
        int treeNum = 0;
        this.trees = new Point[this.numTrees];
        for (int y = 0; y < this.tentRow.length; y++) {
            for (int x = 0; x < this.tentColumn.length; x++) {
                if (this.game[x][y] == 'T') {
                    this.trees[treeNum++] = new Point(x, y);
                }
            }
        }
    }

    /*
     * Call the required methods to solve a tents and trees puzzle.
     */
    public void solve() {
        this.numTrees = this.countNumTrees();
        this.initializeTreeArray();
        this.recursiveSolver(0);
    }

    /*
     * Check that every character in a given String is a digit.
     */
    private boolean isNumeric(String input) {
        try {
            int x = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

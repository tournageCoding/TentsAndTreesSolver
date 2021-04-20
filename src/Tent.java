import java.util.*;

/*
 * A class to represent one game of Tents And Trees.
 * Key: 'C'-Campsite 'T'-Tree '.'-Empty space
 */

public class Tent {
    private char[][] game;
    private int[] rowTentsRequired, columnTentsRequired;
    private int numberOfRows, numberOfColumns, numberOfRequiredTrees;
    private Point[] trees;

    /*
     * Read the game input into a 2D array and the required tent counts for rows and
     * columns into separate arrays.
     */
    public boolean readInput() {
        Scanner scan = new Scanner(System.in);
        String[] rowTemp, columnTemp;
        Stack<String> inputStack = new Stack<String>();

        while (scan.hasNextLine()) {
            inputStack.push(scan.nextLine());
        }
        scan.close();

        // send input to columnTentsRequired
        columnTemp = inputStack.pop().split(" ");
        this.columnTentsRequired = new int[columnTemp.length];
        this.numberOfColumns = this.columnTentsRequired.length;
        for (int i = 0; i < numberOfColumns; i++) {
            if (this.isNumeric(columnTemp[i])) {
                this.columnTentsRequired[i] = Integer.parseInt(columnTemp[i]);
            } else {
                System.out.println("Error: Number of tents required in a column must be a digit.");
                return false;
            }
        }

        // send input to rowTentsRequired
        rowTemp = inputStack.pop().split(" ");
        this.rowTentsRequired = new int[rowTemp.length];
        this.numberOfRows = this.rowTentsRequired.length;
        for (int i = 0; i < numberOfRows; i++) {
            if (this.isNumeric(rowTemp[i])) {
                this.rowTentsRequired[i] = Integer.parseInt(rowTemp[rowTemp.length - 1 - i]);
            } else {
                System.out.println("Error: Number of tents required in a row must be a digit.");
                return false;
            }
            // flipped since number of tents required in a row is given from bottom to top
            this.rowTentsRequired[i] = Integer.parseInt(rowTemp[rowTemp.length - 1 - i]);
        }

        // send input to game
        this.game = new char[numberOfColumns][numberOfRows];
        for (int y = numberOfRows - 1; y >= 0; y--) {
            String currentRow = inputStack.pop();
            if (currentRow.length() != numberOfColumns) {
                System.out.println("Error: Must give a required number of tents for each column of the game.");
                return false;
            }
            for (int x = 0; x < numberOfColumns; x++) {
                this.game[x][y] = currentRow.charAt(x);
            }
        }
        if (!inputStack.isEmpty()) {
            System.out.println("Error: Must give a required number of tents for each row of the game.");
            return false;
        }
        return true;
    }

    /*
     * Check if the puzzle is sovled by comparing the number of tents in each row
     * and column to the required number.
     */
    private boolean isSolved() {
        // check rows
        for (int y = 0; y < this.numberOfRows; y++) {
            int numTents = 0;
            for (int x = 0; x < this.numberOfColumns; x++) {
                if (this.game[x][y] == 'C') {
                    numTents++;
                }
            }
            if (numTents != this.rowTentsRequired[y]) {
                return false;
            }
        }

        // check columns
        for (int x = 0; x < this.numberOfColumns; x++) {
            int numTents = 0;
            for (int y = 0; y < this.numberOfRows; y++) {
                if (this.game[x][y] == 'C') {
                    numTents++;
                }
            }
            if (numTents != this.columnTentsRequired[x]) {
                return false;
            }
        }
        return true;
    }

    /*
     * Print out a visual representation of the game if it is solved.
     */
    public void printGame() {
        if (!this.isSolved()) {
            System.out.println("No solution");
            return;
        }
        for (int i = 0; i < this.numberOfRows; i++) {
            for (int j = 0; j < this.numberOfColumns; j++) {
                System.out.print(this.game[j][i]);
            }
            System.out.println();
        }
    }

    /*
     * Solves recursively by assigning a campsite to each tree and checks multiple
     * possibilities without violating rules.
     */
    private int recursiveSolver(int treeCounter) {
        if (treeCounter == this.numberOfRequiredTrees) {
            return treeCounter;
        }

        int x = this.trees[treeCounter].getX();
        int y = this.trees[treeCounter].getY();

        if (y > 0 && this.game[x][y - 1] == '.') {
            this.game[x][y - 1] = 'C';
            if (this.tentDoesNotExceedCount(x, y - 1) && this.tentNotAdjacentToOtherTent(x, y - 1)) {
                int result = this.recursiveSolver(treeCounter + 1);
                if (result == this.numberOfRequiredTrees) {
                    return result;
                }
            }
            this.game[x][y - 1] = '.';
        }

        if (x < this.numberOfColumns - 1 && this.game[x + 1][y] == '.') {
            this.game[x + 1][y] = 'C';
            if (this.tentDoesNotExceedCount(x + 1, y) && this.tentNotAdjacentToOtherTent(x + 1, y)) {
                int result = this.recursiveSolver(treeCounter + 1);
                if (result == this.numberOfRequiredTrees) {
                    return result;
                }
            }
            this.game[x + 1][y] = '.';
        }

        if (y < this.numberOfRows - 1 && this.game[x][y + 1] == '.') {
            this.game[x][y + 1] = 'C';
            if (this.tentDoesNotExceedCount(x, y + 1) && this.tentNotAdjacentToOtherTent(x, y + 1)) {
                int result = this.recursiveSolver(treeCounter + 1);
                if (result == this.numberOfRequiredTrees) {
                    return result;
                }
            }
            this.game[x][y + 1] = '.';
        }

        if (x > 0 && this.game[x - 1][y] == '.') {
            this.game[x - 1][y] = 'C';
            if (this.tentDoesNotExceedCount(x - 1, y) && this.tentNotAdjacentToOtherTent(x - 1, y)) {
                int result = this.recursiveSolver(treeCounter + 1);
                if (result == this.numberOfRequiredTrees) {
                    return result;
                }
            }
            this.game[x - 1][y] = '.';
        }

        return -1;
    }

    /*
     * Ensure placing a tent at (x, y) does not exceed the required number of tents
     * in a row or column.
     */
    private boolean tentDoesNotExceedCount(int x, int y) {
        int tentCountColumn = 0;
        int tentCountRow = 0;
        for (int i = 0; i < this.numberOfRows; i++) {
            if (this.game[x][i] == 'C') {
                tentCountColumn++;
            }
        }
        if (tentCountColumn > this.columnTentsRequired[x]) {
            return false;
        }

        for (int i = 0; i < this.numberOfColumns; i++) {
            if (this.game[i][y] == 'C') {
                tentCountRow++;
            }
        }
        if (tentCountRow > this.rowTentsRequired[y]) {
            return false;
        }

        return true;
    }

    /*
     * Ensure there are no adjacent tents to the tent at (x, y).
     */
    private boolean tentNotAdjacentToOtherTent(int x, int y) {
        boolean up = y > 0;
        boolean right = x < this.numberOfColumns - 1;
        boolean down = y < this.numberOfRows - 1;
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
    private int countNumberOfRequiredTrees() {
        int trees = 0;
        for (int x = 0; x < this.numberOfColumns; x++) {
            trees += this.columnTentsRequired[x];
        }
        return trees;
    }

    /*
     * Create an array of the coordinates of all the trees in the puzzle.
     */
    private void initializeTreeArray() {
        int treeNum = 0;
        this.trees = new Point[this.numberOfRequiredTrees];
        for (int y = 0; y < this.numberOfRows; y++) {
            for (int x = 0; x < this.numberOfColumns; x++) {
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
        this.numberOfRequiredTrees = this.countNumberOfRequiredTrees();
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

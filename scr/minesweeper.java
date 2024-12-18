import java.util.Random;
public class minesweeper{
    private Random random;
    private gamestack gamestack;
    
    private static final char MINE = '*';
    private static final char UNREVEALED = '-';
    private static final char[] DIGITS = {'0','1','2','3','4','5','6','7','8'};
    //Setting up the number of mines near by in a 3x3 space around the place revealed

    public int numMines;
    public int numRevealed = 0;
    //default none revealed
    public char[][] board;
    public boolean[][] mines;
    public boolean[][] revealed;

    public minesweeper(){
        board = new char[10][10];
        revealed = new boolean[10][10];
        mines = new boolean[10][10];
        random = new Random();
    //board size is 10x10 set-in-stone for now
    }

    public void initializeBoard(){
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col] = UNREVEALED;
                revealed[row][col] = false;
                mines[row][col] = false;
            }
        }

        int numAdded = 0;
        while (numAdded < numMines) {
            int row = random.nextInt(10);
            int col = random.nextInt(10);

            if (!mines[row][col]) {
                mines[row][col] = true;
                numAdded++;
            }
        }
        gamestack = new gamestack(board, revealed, mines);
    }

    public void revealCell(int row, int col) {
        revealed[row][col] = true;
        numRevealed++;

        if (mines[row][col]) {
            board[row][col] = MINE;
            return;
        }

        int numAdjacentMines = countAdjacentMines(row, col);
        if (numAdjacentMines == 0) {
            board[row][col] = ' ';
            revealAdjacentCells(row, col);
        } else {
            board[row][col] = DIGITS[numAdjacentMines];
        }
    }

    private void revealAdjacentCells(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i < 0 || i >= 10 || j < 0 || j >= 10) {
                    continue;
                }

                if (revealed[i][j] || mines[i][j]) {
                    continue;
                }

                revealCell(i, j);
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i < 0 || i >= 10 || j < 0 || j >= 10) {
                    continue;
                }

                if (mines[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    public void Undo() {
        if (gamestack.canUndo()) {
            gamestack.undo();
            board = gamestack.getBoard();
            revealed = gamestack.getRevealed();
            mines = gamestack.getMines();
            numRevealed = gamestack.getnumRevealed();
        }
    }

    public void Save() {
        gamestack.saveSnapshot();
    }

}
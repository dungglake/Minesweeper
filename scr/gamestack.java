import java.util.Stack;

public class gamestack {
    private char[][] board;
    private boolean[][] revealed;
    private boolean[][] mines;
    private Integer numRevealed;

    private Stack<char[][]> boardStack;
    private Stack<boolean[][]> revealedStack;
    private Stack<boolean[][]> minesStack;
    private Stack<Integer> numRevealedStack;

    public gamestack(char[][] board, boolean[][] revealed, boolean[][] mines) {
        this.board = board;
        this.revealed = revealed;
        this.mines = mines;
        this.numRevealed = 0;
        this.boardStack = new Stack<>();
        this.revealedStack = new Stack<>();
        this.minesStack = new Stack<>();
        this.numRevealedStack = new Stack<>();
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean[][] getRevealed() {
        return revealed;
    }

    public boolean[][] getMines() {
        return mines;
    }

    public int getnumRevealed() {
        return numRevealed;
    }

    public boolean canUndo() {
        return !boardStack.isEmpty();
    }

    public void saveSnapshot() {
        char[][] snapshotBoard = new char[board.length][board[0].length];
        boolean[][] snapshotRevealed = new boolean[revealed.length][revealed[0].length];
        boolean[][] snapshotMines = new boolean[mines.length][mines[0].length];

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                snapshotBoard[row][col] = board[row][col];
                snapshotRevealed[row][col] = revealed[row][col];
                snapshotMines[row][col] = mines[row][col];
            }
        }

        boardStack.push(snapshotBoard);
        revealedStack.push(snapshotRevealed);
        minesStack.push(snapshotMines);
        numRevealedStack.push(numRevealed);
    }

    public void undo() {
        if (canUndo()) {
            board = boardStack.pop();
            revealed = revealedStack.pop();
            mines = minesStack.pop();
            numRevealed = numRevealedStack.pop();
        }
    }
}
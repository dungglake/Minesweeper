import javax.swing.*;

class GamePanel {
    public void run() {
        JFrame frame = new JFrame("Minesweeper");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        gameMainMenu main = new gameMainMenu();

        frame.add(main);
        frame.pack();
        frame.setVisible(true);
    }
}

public class gameplay {
    public static void main(String[] args) {
        GamePanel game = new GamePanel();
        game.run();
    }
}
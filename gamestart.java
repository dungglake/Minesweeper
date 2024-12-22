import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class gamestart extends JPanel {
    public minesweeper Game = new minesweeper();

    private static final int Symbolsize = 90;

    private Image GameImage;
    private Image QuitImage;
    private Image MusicOnImage;
    private Image MusicOffImage;
    private Image FlagButtonImage;
    private Image UndoImage;

    enum SymbolImport {
        ONE("images/1.png"),
        TWO("images/2.png"),
        THREE("images/3.png"),
        FOUR("images/4.png"),
        FIVE("images/5.png"),
        SIX("images/6.png"),
        SEVEN("images/7.png"),
        EIGHT("images/8.png"),
        FLAG("images/Flag.png"),
        MINE("images/Mine.png"),
        UNREVEALED("images/Unrevealed.png");

        private String imagePath;

        SymbolImport(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
    
    enum ButtonImport {
        QUIT("images/QuitButton.png", 120, 124),
        FLAG("images/FlagButton.png", 140, 140),
        UNDO("images/UndoButton.png", 443, 124),
        MUSIC_ON("images/MusicOnButton.png", 140, 140),
        MUSIC_OFF("images/MusicOffButton.png", 140, 140);

        private final String path;
        private final int width;
        private final int height;

        ButtonImport(String path, int width, int height) {
            this.path = path;
            this.width = width;
            this.height = height;
        }

        public String getPath() {
            return this.path;
        }

        public int getheight() {
            return height;
        }

        public int getwidth() {
            return width;
        }
    }

    private Image[] symbolImages = new Image[SymbolImport.values().length];

    private Image TickImage;
    private Image CrossImage;

    private Clip musicClip;
    private boolean BGMOn;

    private boolean revealAll = false;
    private boolean gameWon = false;
    private boolean gameLost = false;

    private Point flagLocation = null;
    
    public gamestart(boolean music, int numMines) {
        Game.numMines = numMines;

        this.BGMOn = music;

        setPreferredSize(new Dimension(gameSettings.WIDTH, gameSettings.HEIGHT));

        try {
            File GamePlay = new File("images/GamePlay.png");
            BufferedImage GamePlayOriginal = ImageIO.read(GamePlay);
            Image scaledMenu = GamePlayOriginal.getScaledInstance(gameSettings.WIDTH, gameSettings.HEIGHT,
                    Image.SCALE_SMOOTH);
            GameImage = scaledMenu;

            for (ButtonImport button : ButtonImport.values()) {
                File buttonFile = new File(button.getPath());
                BufferedImage buttonOriginal = ImageIO.read(buttonFile);
                Image scaledButton;

                if (button == ButtonImport.MUSIC_ON || button == ButtonImport.MUSIC_OFF
                        || button == ButtonImport.QUIT || button == ButtonImport.UNDO || button == ButtonImport.FLAG) {
                    scaledButton = buttonOriginal.getScaledInstance(
                            gameSettings.AdjustWidth(128),
                            gameSettings.AdjustHeight(128),
                            Image.SCALE_SMOOTH);
                } else {
                    scaledButton = buttonOriginal.getScaledInstance(
                            gameSettings.AdjustWidth(443),
                            gameSettings.AdjustHeight(124),
                            Image.SCALE_SMOOTH);
                }

                switch (button) {
                    case QUIT:
                        QuitImage = scaledButton;
                        break;
                    case FLAG:
                        FlagButtonImage = scaledButton;
                        break;
                    case UNDO:
                        UndoImage = scaledButton;
                        break;
                    case MUSIC_ON:
                        MusicOnImage = scaledButton;
                        break;
                    case MUSIC_OFF:
                        MusicOffImage = scaledButton;
                        break;
                }
            }
            
    File bgm = new File("sounds/Music.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bgm);

            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);

            if (BGMOn) {
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                musicClip.start();
            }

            File TickSign = new File("images/Tick.png");
            BufferedImage TickSignOriginal = ImageIO.read(TickSign);
            Image scaledTick = TickSignOriginal.getScaledInstance(gameSettings.AdjustWidth(392),
                    gameSettings.AdjustHeight(368),
                    Image.SCALE_SMOOTH);
            TickImage = scaledTick;

            File CrossSign = new File("images/Cross.png");
            BufferedImage CrossSignOriginal = ImageIO.read(CrossSign);
            Image scaledCross = CrossSignOriginal.getScaledInstance(gameSettings.AdjustWidth(700),
                    gameSettings.AdjustHeight(700),
                    Image.SCALE_SMOOTH);
            CrossImage = scaledCross;

            for (SymbolImport symbolType : SymbolImport.values()) {
                File symbolFile = new File(symbolType.getImagePath());
                BufferedImage symbolImageOriginal = ImageIO.read(symbolFile);
                Image scaledSymbol = symbolImageOriginal.getScaledInstance(
                        gameSettings.AdjustWidth(Symbolsize),
                        gameSettings.AdjustHeight(Symbolsize),
                        Image.SCALE_SMOOTH);
                symbolImages[symbolType.ordinal()] = scaledSymbol;
            }

        } catch (Exception e) {
            System.out.println("Exception found: " + e);
        }
        
        Game.initializeBoard();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if (!gameWon && !gameLost) {
                    if (mouseX >= gameSettings.AdjustWidth(1742)
                            && mouseX <= gameSettings.AdjustWidth(1742) + gameSettings.AdjustWidth(140)
                            && mouseY >= gameSettings.AdjustHeight(38)
                            && mouseY <= gameSettings.AdjustHeight(38) + gameSettings.AdjustHeight(140)) {
                        BGMOn = !BGMOn;
                        if (BGMOn) {
                            musicClip.start();
                        } else {
                            musicClip.stop();
                        }
                        repaint();
                    }

                    if (mouseX >= gameSettings.AdjustWidth(1080)
                            && mouseX <= gameSettings.AdjustWidth(1080 + UndoImage.getWidth(null))
                            && mouseY >= gameSettings.AdjustHeight(698)
                            && mouseY <= gameSettings.AdjustHeight(698 + UndoImage.getHeight(null))) {
                        Game.Undo();
                        repaint();
                    }

                    if (mouseX >= gameSettings.AdjustWidth(Symbolsize)
                            && mouseX <= gameSettings.AdjustWidth(Symbolsize * 11)
                            && mouseY >= gameSettings.AdjustHeight(Symbolsize)
                            && mouseY <= gameSettings.AdjustHeight(Symbolsize * 11)) {
                        int row = (int) (mouseY / gameSettings.AdjustHeight(Symbolsize)) - 1;
                        int col = (int) (mouseX / gameSettings.AdjustWidth(Symbolsize)) - 1;

                        Game.Save();

                        if (Game.board[row][col] == '-') {
                            Game.revealCell(row, col);
                            if (Game.mines[row][col]) {
                                gameLost = true;
                                try {
                                    playlose();
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                                    e1.printStackTrace();
                                }
                                repaint();

                            } else if (Game.numRevealed == 100 - Game.numMines) {
                                gameWon = true;
                                try {
                                    playwin();
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                                    e1.printStackTrace();
                                }
                                repaint();
                            } else {
                                repaint();
                            }
                        }
                    }
                }
                if (mouseX >= gameSettings.AdjustWidth(1080)
                        && mouseX <= gameSettings.AdjustWidth(1080 + QuitImage.getWidth(null))
                        && mouseY >= gameSettings.AdjustHeight(858)
                        && mouseY <= gameSettings.AdjustHeight(858 + QuitImage.getHeight(null))) {
                    int confirm = JOptionPane.showConfirmDialog(
                        gamestart.this, 
                        "Do you want to exit game?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (musicClip != null && musicClip.isRunning()) {
                            musicClip.stop();
                            musicClip.close();
                        }

                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gamestart.this);
                        frame.setContentPane(new gameMainMenu()); 
                        frame.pack(); 
                    }
                }
            }

        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameWon && !gameLost) {
                    if (e.getX() >= gameSettings.AdjustWidth(1578)
                            && e.getX() <= gameSettings.AdjustWidth(1578) + gameSettings.AdjustWidth(140)
                            && e.getY() >= gameSettings.AdjustHeight(38)
                            && e.getY() <= gameSettings.AdjustHeight(38) + gameSettings.AdjustHeight(140)) {
                        flagLocation = e.getPoint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (flagLocation != null) {
                    int x = e.getX();
                    int y = e.getY();

                    if (x >= gameSettings.AdjustWidth(Symbolsize)
                            && x <= gameSettings.AdjustWidth(Symbolsize * 11)
                            && y >= gameSettings.AdjustHeight(Symbolsize)
                            && y <= gameSettings.AdjustHeight(Symbolsize * 11)) {
                        int row = (int) (y / gameSettings.AdjustHeight(Symbolsize)) - 1;
                        int col = (int) (x / gameSettings.AdjustWidth(Symbolsize)) - 1;
                        Game.Save();
                        if (Game.board[row][col] == '-') {
                            Game.board[row][col] = 'F';
                        } else if (Game.board[row][col] == 'F') {
                            Game.board[row][col] = '-';
                        }
                    }
                    flagLocation = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (flagLocation != null) {
                    flagLocation.setLocation(e.getPoint());
                    repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(GameImage, 0, 0, this);

        if (BGMOn) {
            g.drawImage(MusicOnImage, gameSettings.AdjustWidth(1742), gameSettings.AdjustHeight(38), this);
        } else {
            g.drawImage(MusicOffImage, gameSettings.AdjustWidth(1742), gameSettings.AdjustHeight(38), this);
        }

        g.drawImage(FlagButtonImage, gameSettings.AdjustWidth(1578), gameSettings.AdjustHeight(38), this);
        g.drawImage(UndoImage, gameSettings.AdjustWidth(1080), gameSettings.AdjustHeight(698), this);
        g.drawImage(QuitImage, gameSettings.AdjustWidth(1080), gameSettings.AdjustHeight(858), this);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (Game.mines[row][col] && (revealAll || Game.revealed[row][col])) {
                    g.drawImage(symbolImages[SymbolImport.MINE.ordinal()],
                            gameSettings.AdjustWidth(Symbolsize * (col + 1)),
                            gameSettings.AdjustHeight(Symbolsize * (row + 1)), this);
                }

                else if (Game.board[row][col] == 'F') {
                    g.drawImage(symbolImages[SymbolImport.UNREVEALED.ordinal()],
                            gameSettings.AdjustWidth(Symbolsize * (col + 1)),
                            gameSettings.AdjustHeight(Symbolsize * (row + 1)), this);
                    g.drawImage(symbolImages[SymbolImport.FLAG.ordinal()],
                            gameSettings.AdjustWidth(Symbolsize * (col + 1)),
                            gameSettings.AdjustHeight(Symbolsize * (row + 1)), this);
                }

                else if (Game.revealed[row][col]) {
                    SymbolImport symbolType = null;
                    switch (Game.board[row][col]) {
                        case '1':
                            symbolType = SymbolImport.ONE;
                            break;
                        case '2':
                            symbolType = SymbolImport.TWO;
                            break;
                        case '3':
                            symbolType = SymbolImport.THREE;
                            break;
                        case '4':
                            symbolType = SymbolImport.FOUR;
                            break;
                        case '5':
                            symbolType = SymbolImport.FIVE;
                            break;
                        case '6':
                            symbolType = SymbolImport.SIX;
                            break;
                        case '7':
                            symbolType = SymbolImport.SEVEN;
                            break;
                        case '8':
                            symbolType = SymbolImport.EIGHT;
                            break;
                        default:
                            break;
                    }

                    if (symbolType != null) {
                        g.drawImage(symbolImages[symbolType.ordinal()],
                                gameSettings.AdjustWidth(Symbolsize * (col + 1)),
                                gameSettings.AdjustHeight(Symbolsize * (row + 1)), this);
                    }
                }

                else {
                    g.drawImage(symbolImages[SymbolImport.UNREVEALED.ordinal()],
                            gameSettings.AdjustWidth(Symbolsize * (col + 1)),
                            gameSettings.AdjustHeight(Symbolsize * (row + 1)), this);
                }
            }
        }

        if (flagLocation != null) {
            g.drawImage(symbolImages[SymbolImport.FLAG.ordinal()],
                    (int) flagLocation.getX() - (int) (gameSettings.AdjustWidth(Symbolsize) / 2),
                    (int) flagLocation.getY() - (int) (gameSettings.AdjustHeight(Symbolsize) / 2), this);
        }

        if (this.gameWon == true) {
            g.drawImage(TickImage, gameSettings.AdjustWidth(384), gameSettings.AdjustHeight(384), this);
        }

        if (this.gameLost == true) {
            g.drawImage(CrossImage, gameSettings.AdjustWidth(220), gameSettings.AdjustHeight(220), this);
        }

    }

    public void playwin() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Clip clip;
        BGMOn = false;
        musicClip.stop();
        revealAll = true;

        File win = new File("sounds/clear.wav");
        AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(win);
        clip = AudioSystem.getClip();
        clip.open(audioIn2);
        clip.start();

    }

    public void playlose() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Clip clip;
        BGMOn = false;
        musicClip.stop();
        revealAll = true;

        File lose = new File("sounds/gameover.wav");
        AudioInputStream audioIn3 = AudioSystem.getAudioInputStream(lose);
        clip = AudioSystem.getClip();
        clip.open(audioIn3);
        clip.start();
    }
}
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

enum ButtonImport {
    EASY("images/EasyButton.png"),
    MEDIUM("images/MediumButton.png"),
    HARD("images/HardButton.png"),
    EXTREME("images/ExtremeButton.png"),
    QUIT("images/QuitButton.png"),
    MUSIC_ON("images/MusicOnButton.png"),
    MUSIC_OFF("images/MusicOffButton.png");

    private final String path;

    private ButtonImport(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}

enum DifficultyLevelImport {
    EASY(gameSettings.EASY_MINES, 470),
    MEDIUM(gameSettings.MEDIUM_MINES, 618),
    HARD(gameSettings.HARD_MINES, 767),
    EXTREME(gameSettings.EXTREME_MINES, 915);

    private final int mines;
    private final int mouseY;

    DifficultyLevelImport(int mines, int mouseY) {
        this.mines = mines;
        this.mouseY = mouseY;
    }

    public int getMines() {
        return mines;
    }

    public int getMouseY() {
        return mouseY;
    }
}

public class gameMainMenu extends JPanel {
    private Image MenuImage;
    private Image WelcomeImage;
    private Image ChooseImage;
    private Image EasyImage;
    private Image MediumImage;
    private Image HardImage;
    private Image ExtremeImage;
    private Image MusicOnImage;
    private Image MusicOffImage;
    private Image QuitImage;

    private Clip musicClip;
    private boolean MusicOn = true;
    private boolean savedMusicOn;

    public gameMainMenu() {
        setPreferredSize(new Dimension(gameSettings.WIDTH, gameSettings.HEIGHT));

        try {
            File mainMenu = new File("images/MainMenu.png");
            BufferedImage mainMenuOriginal = ImageIO.read(mainMenu);
            Image scaledMenu = mainMenuOriginal.getScaledInstance(gameSettings.WIDTH, gameSettings.HEIGHT,
                    Image.SCALE_SMOOTH);
            MenuImage = scaledMenu;

            File welcomeSign = new File("images/WelcomeSign.png");
            BufferedImage welcomeSignOriginal = ImageIO.read(welcomeSign);
            Image scaledWelcome = welcomeSignOriginal.getScaledInstance(gameSettings.AdjustWidth(861),
                    gameSettings.AdjustHeight(243),
                    Image.SCALE_SMOOTH);
            WelcomeImage = scaledWelcome;

            File chooseSign = new File("images/ChoosePrompt.png");
            BufferedImage chooseSignOriginal = ImageIO.read(chooseSign);
            Image scaledChoose = chooseSignOriginal.getScaledInstance(gameSettings.AdjustWidth(861),
                    gameSettings.AdjustHeight(96),
                    Image.SCALE_SMOOTH);
            ChooseImage = scaledChoose;

            for (ButtonImport button : ButtonImport.values()) {
                File buttonFile = new File(button.getPath());
                BufferedImage buttonOriginal = ImageIO.read(buttonFile);
                Image scaledButton;

                if (button == ButtonImport.MUSIC_ON || button == ButtonImport.MUSIC_OFF
                        || button == ButtonImport.QUIT) {
                    scaledButton = buttonOriginal.getScaledInstance(
                            gameSettings.AdjustWidth(64),
                            gameSettings.AdjustHeight(64),
                            Image.SCALE_SMOOTH);
                } else {
                    scaledButton = buttonOriginal.getScaledInstance(
                            gameSettings.AdjustWidth(443),
                            gameSettings.AdjustHeight(124),
                            Image.SCALE_SMOOTH);
                }

                switch (button) {
                    case EASY:
                        EasyImage = scaledButton;
                        break;
                    case MEDIUM:
                        MediumImage = scaledButton;
                        break;
                    case HARD:
                        HardImage = scaledButton;
                        break;
                    case EXTREME:
                        ExtremeImage = scaledButton;
                        break;
                    case MUSIC_ON:
                        MusicOnImage = scaledButton;
                        break;
                    case MUSIC_OFF:
                        MusicOffImage = scaledButton;
                        break;
                    case QUIT:
                        QuitImage = scaledButton;
                        break;
                }
            }

            File bgm = new File("sounds/Music.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bgm);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) {
            System.out.println("Exception found: " + e);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                if (mouseX >= gameSettings.AdjustWidth(1742)
                        && mouseX <= gameSettings.AdjustWidth(1742) + gameSettings.AdjustWidth(140)
                        && mouseY >= gameSettings.AdjustHeight(38)
                        && mouseY <= gameSettings.AdjustHeight(38) + gameSettings.AdjustHeight(140)) {

                    MusicOn = !MusicOn;
                    if (MusicOn) {
                        musicClip.start();
                    } else {
                        musicClip.stop();
                    }
                    repaint();
                }

                if (mouseX >= gameSettings.AdjustWidth(1440)
                        && mouseX <= gameSettings.AdjustWidth(1440) + gameSettings.AdjustWidth(443)
                        && mouseY >= gameSettings.AdjustHeight(915)
                        && mouseY <= gameSettings.AdjustHeight(915) + gameSettings.AdjustHeight(124)) {
                    System.exit(0);
                }

                for (DifficultyLevelImport level : DifficultyLevelImport.values()) {
                    if (mouseX >= gameSettings.AdjustWidth(739)
                            && mouseX <= gameSettings.AdjustWidth(739) + gameSettings.AdjustWidth(443)
                            && mouseY >= gameSettings.AdjustHeight(level.getMouseY())
                            && mouseY <= gameSettings.AdjustHeight(level.getMouseY())
                                    + gameSettings.AdjustHeight(124)) {
                        savedMusicOn = MusicOn;
                        musicClip.stop();
                        JPanel gameStart = new gamestart(savedMusicOn, level.getMines());
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gameMainMenu.this);
                        frame.setContentPane(gameStart);
                        frame.pack();
                        break;
                    }
                }
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(MenuImage, 0, 0, this);
        g.drawImage(WelcomeImage, gameSettings.AdjustWidth(527), gameSettings.AdjustHeight(34), this);
        g.drawImage(ChooseImage, gameSettings.AdjustWidth(527), gameSettings.AdjustHeight(350), this);
        g.drawImage(EasyImage, gameSettings.AdjustWidth(739), gameSettings.AdjustHeight(470), this);
        g.drawImage(MediumImage, gameSettings.AdjustWidth(739), gameSettings.AdjustHeight(618), this);
        g.drawImage(HardImage, gameSettings.AdjustWidth(739), gameSettings.AdjustHeight(767), this);
        g.drawImage(ExtremeImage, gameSettings.AdjustWidth(739), gameSettings.AdjustHeight(915), this);

        if (MusicOn) {
            g.drawImage(MusicOnImage, gameSettings.AdjustWidth(1840), gameSettings.AdjustHeight(38), this);
        } else {
            g.drawImage(MusicOffImage, gameSettings.AdjustWidth(1840), gameSettings.AdjustHeight(38), this);
        }

        g.drawImage(QuitImage, gameSettings.AdjustWidth(1840), gameSettings.AdjustHeight(1000), this);
    }
}
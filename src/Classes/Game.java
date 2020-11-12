package Classes;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private int playerCount;
    private int minimumPlayerCount = 2;
    private int maximumPlayerCount = 4;
    private Player[] players;
    private Dice dice = new Dice();
    private Board board = new Board();
    private Scanner scanner = new Scanner(System.in);

    public Game() {
        initializeGame();
    }

    private void startGame() {

        Player currentPlayer = getYoungestPlayer();
        boolean running = true;
        while (running) {

            String name = currentPlayer.getName();
            System.out.println("### " + name + " ###");
            System.out.println("Kast terning - tryk på Enter");
            String waitForEnter = scanner.next();
            int diceValue = dice.roll();
            System.out.println("Kast terning: " + diceValue);
            int currentIndex = (currentPlayer.getCurrentSquareIndex() + diceValue) % board.getSquareCount();
            currentPlayer.setCurrentSquareIndex(currentIndex);
            board.takePlayerTurn(currentPlayer);
        }
    }

    private Player getYoungestPlayer() {
        Player youngestPlayer = players[0];
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.getAge() < youngestPlayer.getAge()) {
                youngestPlayer = player;
            }
        }
        return youngestPlayer;
    }

    private void initializeGame() {

        System.out.println("Velkommen til Matador");

        System.out.println("Indtas antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        playerCount = scanner.nextInt();
        if (playerCount < minimumPlayerCount || playerCount > maximumPlayerCount) {
            System.out.println("Antal spillere skal være mellem " + minimumPlayerCount + " og " + maximumPlayerCount);
            System.exit(1);
            return;
        }

        players = new Player[playerCount];
        int initialPlayerBalance = getPlayerInitialBalance();
        PlayerFigureType[] figureTypes = getPlayerFigureTypes();
        for (int i = 0; i < playerCount; i++) {
            int playerNumber = i + 1;
            System.out.println("Spiller " + playerNumber + " navn: ");
            String name = scanner.next();
            System.out.println("Spiller " + playerNumber + " alder: ");
            int age = scanner.nextInt();
            players[i] = new Player(name, age, figureTypes[i], initialPlayerBalance);
        }
    }

    private PlayerFigureType[] getPlayerFigureTypes() {
        PlayerFigureType[] availableFigureTypes = new PlayerFigureType[4];
        availableFigureTypes[0] = PlayerFigureType.Bilen;
        availableFigureTypes[1] = PlayerFigureType.Katten;
        availableFigureTypes[2] = PlayerFigureType.Hunden;
        availableFigureTypes[3] = PlayerFigureType.Skibet;

        PlayerFigureType[] figureTypes = new PlayerFigureType[playerCount];
        for (int i = 0; i < playerCount; i++) {
            figureTypes[i] = availableFigureTypes[i];
        }
        return figureTypes;
    }

    private int getPlayerInitialBalance() {
        if (playerCount == 2) {
            return 20;
        }
        if (playerCount == 3) {
            return 18;
        }
        return 16;
    }

}

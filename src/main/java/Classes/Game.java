package Classes;

import java.util.Scanner;

import gui_fields.GUI_Player;
import gui_main.GUI;

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

    public void start() {

        int playerIndex = getYoungestPlayerIndex();
        Player currentPlayer = players[playerIndex];
        boolean running = true;
        while (running) {
            String name = currentPlayer.getName();
            System.out.println();
            System.out.println("### " + name + " ###");
            System.out.println("Kast terning - tryk på Enter");
            String waitForEnter = scanner.nextLine();
            int diceValue = dice.roll();
            System.out.println("Terning: " + diceValue);

            try {
                board.takePlayerTurn(currentPlayer, diceValue);
                BoardSquare boardSquare = board.getBoardSquareByIndex(currentPlayer.getCurrentSquareIndex());
                Square square = boardSquare.getSquare();
                System.out.println("Pris: " + square.getPrice());
                System.out.println("Pengebeholdning: " + currentPlayer.getBalance());
                System.out.println("Square: " + square.getName());
            } catch (NotEnoughBalanceException e) {
                handleGameOver(currentPlayer);
                return;
            }

            playerIndex++;
            playerIndex = playerIndex % playerCount;
            currentPlayer = players[playerIndex];
        }
    }

    private void handleGameOver(Player currentPlayer) {
        Player winingPlayer = players[0];
        for (int i = 0; i < playerCount; i++) {
            Player player = players[i];
            if(player == currentPlayer){
                continue;
            }
            if(player.getBalance() > winingPlayer.getBalance()){
                winingPlayer = player;
            }
        }

        System.out.println(" ### SPIL SLUT ###");
        System.out.println("Vinder");
        System.out.println("Navn: " + winingPlayer.getName());
        System.out.println("Pengebeholding: " + winingPlayer.getBalance());
    }

    private int getYoungestPlayerIndex() {
        int index = 0;
        Player youngestPlayer = players[index];
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.getAge() < youngestPlayer.getAge()) {
                index = i;
                youngestPlayer = player;
            }
        }
        return index;
    }

    private void initializeGame() {
        GUI gui = new GUI();
        gui.showMessage("                                                                        Velkommen til Matador!");

        playerCount = gui.getUserInteger("Indtas antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        //playerCount = nextIntFromScanner();
        while(playerCount < minimumPlayerCount || playerCount > maximumPlayerCount) {
            gui.showMessage("Antal spillere skal være mellem " + minimumPlayerCount + " og " + maximumPlayerCount);
            playerCount = gui.getUserInteger("Indtast antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        }

        //int initialPlayerBalance = getPlayerInitialBalance();
        //PlayerFigureType[] figureTypes = getPlayerFigureTypes();
        GUI_Player[] players = new GUI_Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            String name = gui.getUserString("Indtast spiller " + i+1 + "'s navn: ");
            //int age = gui.getUserInteger("Indtast spiller " + i + "'s alder: ");
            players[i] = new GUI_Player(name, 2000);
            gui.addPlayer(players[i]);
            gui.getFields()[0].setCar(players[i], true);
            //players[i] = new Player(name, age, figureTypes[i], initialPlayerBalance);
        }
    }

    private int nextIntFromScanner(){
        int value = scanner.nextInt();
        String readLineButItsNotUsed = scanner.nextLine();
        return value;
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

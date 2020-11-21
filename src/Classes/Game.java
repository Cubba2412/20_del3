package Classes;

import java.util.Scanner;

import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

public class Game {

    private int playerCount;
    private int minimumPlayerCount = 2;
    private int maximumPlayerCount = 4;
    private Player[] players;
    private Dice dice = new Dice();
    private GUI gui;
    private Board board = new Board(gui);
    private Scanner scanner = new Scanner(System.in);

    public Game(GUI gui) {
        this.gui = gui;
        start();
    }

    //public GUI_Player[] Game(GUI gui) {
      //  initializeGame(gui);
   // }

    public void start() {
        // Initialize the game
        players = initializeGame(gui);
        //Ensure the youngest player starts
        int playerIndex  = getYoungestPlayerIndex();
        Player currentPlayer = players[playerIndex];
        while (true) {
            String name = currentPlayer.getName();
            String choice = gui.getUserButtonPressed("Spiller" + name + "'s tur. Kast terningen - tryk på Enter" );
            int diceValue = -1;
            if (choice.equals("Spiller" + name + "'s tur. Kast terningen - tryk på Enter")) {
                diceValue = dice.roll();
                gui.setDie(diceValue);
            }
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

    private Player[] initializeGame(GUI gui) {
        gui.showMessage("                                                                        Velkommen til Matador!");

        playerCount = gui.getUserInteger("Indtas antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        //playerCount = nextIntFromScanner();
        while(playerCount < minimumPlayerCount || playerCount > maximumPlayerCount) {
            gui.showMessage("Antal spillere skal være mellem " + minimumPlayerCount + " og " + maximumPlayerCount);
            playerCount = gui.getUserInteger("Indtast antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        }

        Player[] players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            String name = gui.getUserString("Indtast spiller " + i+1 + "'s navn: ");
            int age = gui.getUserInteger("Indtast spiller " + i+1 + "'s alder: ");
            GUI_Player gui_player = new GUI_Player(name, 2000);
            players[i] = new Player(gui_player, age, 0);
            gui.addPlayer(gui_player);
            gui.getFields()[0].setCar(players[i].getGuiPlayer(), true);
        }
        return players;
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

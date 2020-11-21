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
    private Board board;
    private Scanner scanner = new Scanner(System.in);

    public Game(GUI gui) {
        this.gui = gui;
        this.board = new Board(gui);
        start();
    }

    //public GUI_Player[] Game(GUI gui) {
      //  initializeGame(gui);
   // }

    public void start() {
        // Initialize the game
        players = initializeGame();
        //Ensure the youngest player starts
        int playerIndex  = getYoungestPlayerIndex();
        while (true) {
            Player currentPlayer = players[playerIndex];
           try {
                board.takePlayerTurn(currentPlayer, dice);
            } catch (NotEnoughBalanceException e) {
                handleGameOver(currentPlayer);
                break;
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

        gui.showMessage(" ### SPIL SLUT ###" + "\n" + "Vinder" + "\n" + "Navn: " + winingPlayer.getName());

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

    private Player[] initializeGame() {
        this.gui.showMessage("                                                                        Velkommen til Matador!");

        playerCount = this.gui.getUserInteger("Indtas antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        //playerCount = nextIntFromScanner();
        while(playerCount < minimumPlayerCount || playerCount > maximumPlayerCount) {
            this.gui.showMessage("Antal spillere skal være mellem " + minimumPlayerCount + " og " + maximumPlayerCount);
            playerCount = this.gui.getUserInteger("Indtast antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        }

        Player[] players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            String name = this.gui.getUserString("Indtast spiller " + String.valueOf(i+1) + "'s navn: ");
            int age = this.gui.getUserInteger("Indtast spiller " + String.valueOf(i+1) + "'s alder: ");
            GUI_Player gui_player = new GUI_Player(name, 2000);
            players[i] = new Player(gui_player, age, 0);
            this.gui.addPlayer(gui_player);
            this.gui.getFields()[0].setCar(players[i].getGuiPlayer(), true);
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

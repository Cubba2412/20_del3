package Classes;
import gui_fields.GUI_Player;
import gui_main.GUI;

public class Game {

    private int playerCount;
    private int minimumPlayerCount = 2;
    private int maximumPlayerCount = 4;
    private Player[] players;
    private Dice dice = new Dice();
    private GUI gui;
    private GameBoard gameBoard;
    
    public Game() {
        this.gameBoard = new GameBoard(players);
        GUI gui = new GUI(gameBoard.getFields());
        this.gui = gui;
        gameBoard.setGUI(gui);
        players = initializeGame();
        gameBoard.setPlayers(players);
    }

    public void start() {
        // Initialize the game
        //Ensure the youngest player starts
        int playerIndex  = getYoungestPlayerIndex();
        Player currentPlayer = players[playerIndex];
        while (true) {
           try {
                gameBoard.takePlayerTurn(currentPlayer, dice);
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

        this.playerCount = gui.getUserInteger("Indtas antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        //playerCount = nextIntFromScanner();
        while(playerCount < minimumPlayerCount || playerCount > maximumPlayerCount) {
            this.gui.showMessage("Antal spillere skal være mellem " + minimumPlayerCount + " og " + maximumPlayerCount);
            playerCount = gui.getUserInteger("Indtast antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        }
        int initialBalance = getPlayerInitialBalance();
        Player[] players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            String name = gui.getUserString("Indtast spiller " + String.valueOf(i+1) + "'s navn: ");
            int age = gui.getUserInteger("Indtast spiller " + String.valueOf(i+1) + "'s alder: ");
            while(age == -1) {
                age = gui.getUserInteger("Indtast venligst spiller " + String.valueOf(i+1) + "'s alder: ");
            }

            GUI_Player gui_player = new GUI_Player(name, initialBalance);
            players[i] = new Player(gui_player, age, 0);
            this.gui.addPlayer(gui_player);
            this.gui.getFields()[0].setCar(players[i].getGuiPlayer(), true);
        }
        return players;
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

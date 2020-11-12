package Classes;

import java.util.Scanner;

public class Game {

    private int playerCount;
    private int minimumPlayerCount = 2;
    private int maximumPlayerCount = 4;
    private Player[] players;

    public Game(){
        initializeGame();
    }

    private void startGame(){

        Player current = getYoungestPlayer();
        boolean running = true;
        while(running){



        }
    }

    private Player getYoungestPlayer(){
        Player youngestPlayer = players[0];
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if(player.getAge() < youngestPlayer.getAge()){
                youngestPlayer = player;
            }
        }
        return youngestPlayer;
    }

    private void initializeGame(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Velkommen til Matador");

        System.out.println("Indtas antal spiller: " + minimumPlayerCount + " - " + maximumPlayerCount);
        playerCount = scanner.nextInt();
        if(playerCount < minimumPlayerCount  || playerCount > maximumPlayerCount){
            System.out.println("Antal spillere skal være mellem " + minimumPlayerCount + " og " + maximumPlayerCount);
            System.exit(1);
            return;
        }

        players = new Player[playerCount];
        int initialPlayerBalance  = getPlayerInitialBalance();
        for (int i = 0; i < playerCount; i++) {
            int playerNumber = i+1;
            System.out.println("Spiller " + playerNumber + " navn: ");
            String name = scanner.next();
            System.out.println("Spiller " + playerNumber + " alder: ");
            int age = scanner.nextInt();
            players[i] = new Player(name, age, initialPlayerBalance);
        }
    }

    private int getPlayerInitialBalance(){
        if(playerCount == 2){
            return 20;
        }
        if(playerCount == 3){
            return 18;
        }
        return 16;
    }

}

package Classes;

import java.util.ArrayList;

public class BoardSquare {

    private Square square;
    private Player soldToPlayer;
    private ArrayList<Player> rentedToPlayers = new ArrayList<Player>();

    public BoardSquare(Square square){
        this.square = square;
    }

    public Square getSquare() {
        return square;
    }

    public Player getSoldToPlayer() {
        return soldToPlayer;
    }

    public void setSoldToPlayer(Player soldToPlayer) {
        this.soldToPlayer = soldToPlayer;
    }

    public ArrayList<Player> getRentedToPlayers() {
        return rentedToPlayers;
    }

    public void addRentedToPlayer(Player rentedToPlayer){
        for (int i = 0; i < rentedToPlayers.size(); i++) {
            Player player = rentedToPlayers.get(i);
            if(player == rentedToPlayer){
                return;
            }
        }
        this.rentedToPlayers.add(rentedToPlayer);
    }
}

package Classes;

import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

public class Board {

    private int squareCount = 24;
    private GUI gui;
    private BoardSquare[] boardSquares = new BoardSquare[squareCount];

    public Board(GUI gui) {
        this.gui = gui;
        initializeBoard();
    }

    public BoardSquare getBoardSquareByIndex(int index){
        return boardSquares[index];
    }

   public void takePlayerTurn(Player currentPlayer, int diceValue) throws NotEnoughBalanceException {
        //Remove player from current field

        int nextIndex = currentPlayer.getCurrentSquareIndex() + diceValue;
        int currentIndex = nextIndex % squareCount;
        currentPlayer.setCurrentSquareIndex(gui,currentIndex);

        GUI_Field currentField = gui.getFields()[currentIndex];
        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
        Square square = boardSquare.getSquare();
        handleAnySquareBefore(currentPlayer);

        switch (square.getSquareType()) {
            case DoNothing:
                handleNothingSquare(currentPlayer);
                break;
            case Payment:
                handlePaymentSquare(currentPlayer);
                break;
            case GotoJail:
                handleGotoJailSquare(currentPlayer);
                break;
            case TakeBreak:
                handleTakeABreakSquare(currentPlayer);
                break;
            case TakeChanceCard:
                handleTakeChanceCardSquare(currentPlayer);
                break;
            case FreeParking:
                handleFreeParkingSquare(currentPlayer);
                break;
        }

        handleAnySquareAfter(currentPlayer, nextIndex);
    }

    private void handleAnySquareBefore(Player currentPlayer) throws NotEnoughBalanceException {
        if (currentPlayer.isInPrison()) {
            currentPlayer.decreaseBalanceBy(1);
            currentPlayer.setInPrison(false);
        }
    }

    private void handleAnySquareAfter(Player currentPlayer, int nextIndex) {
        if (nextIndex >= squareCount && !currentPlayer.isInPrison()) {
            currentPlayer.increaseBalanceBy(2);
        }
    }

    private void handleNothingSquare(Player currentPlayer) {

    }

    private void handlePaymentSquare(Player currentPlayer) throws NotEnoughBalanceException {

        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
        Square square = boardSquare.getSquare();
        int price = square.getPrice();
        Player soldToPlayer = boardSquare.getSoldToPlayer();

// if the soldToPlayer is the same as the currentPlayer then do not do anything because
// the currentPlayer already owns this square
        if (soldToPlayer == currentPlayer){
            return;


        }

        if (soldToPlayer == null) {
            // the first player on this square becomes the owner and pays the price
            currentPlayer.decreaseBalanceBy(price);
            boardSquare.setSoldToPlayer(currentPlayer);
        } else {
            // other players coming to this square pays to the owner and become one of the renters
            soldToPlayer.increaseBalanceBy(price);
            currentPlayer.decreaseBalanceBy(price);
            boardSquare.addRentedToPlayer(currentPlayer);
        }
    }

    private void handleGotoJailSquare(Player currentPlayer) {
        currentPlayer.setInPrison(true);
        int takeABreakBoardSquareIndex = getSquareIndexByType(SquareType.TakeBreak);
        currentPlayer.setCurrentSquareIndex(this.gui,takeABreakBoardSquareIndex);
    }

    private void handleTakeABreakSquare(Player currentPlayer) {

    }

    private void handleTakeChanceCardSquare(Player currentPlayer) {

    }

    private void handleFreeParkingSquare(Player currentPlayer) {

    }

    private int getSquareIndexByType(SquareType squareType) {
        for (int i = 0; i < squareCount; i++) {
            BoardSquare boardSquare = boardSquares[i];
            if (boardSquare.getSquare().getSquareType() == squareType) {
                return i;
            }
        }
        return 0;
    }


    private void initializeBoard() {

        Square start = new Square("Start", 0, SquareColor.None, SquareType.DoNothing);
        Square burgerBaren = new Square("Burgerbaren", 1, SquareColor.Brown, SquareType.Payment);
        Square pizzaria = new Square("Pizzaria", 1, SquareColor.Brown, SquareType.Payment);
        Square chance = new Square("Chance", 0, SquareColor.None, SquareType.TakeChanceCard);
        Square slikButikken = new Square("Slikbutikken", 1, SquareColor.LightBlue, SquareType.Payment);
        Square isKiosken = new Square("Iskiosken", 1, SquareColor.LightBlue, SquareType.Payment);
        Square påBesoeg = new Square("På besøg", 0, SquareColor.None, SquareType.DoNothing);
        Square museet = new Square("Musset", 2, SquareColor.Pink, SquareType.Payment);
        Square biblioteket = new Square("Biblioteket", 2, SquareColor.Pink, SquareType.Payment);
        Square skaterParken = new Square("Skatenparken", 2, SquareColor.Orange, SquareType.Payment);
        Square svømminPoolen = new Square("Svømmingpoolen", 2, SquareColor.Orange, SquareType.Payment);
        Square gratisParkering = new Square("Gratis parkering", 0, SquareColor.None, SquareType.DoNothing);
        Square spilleHalen = new Square("Spillehalen", 3, SquareColor.Red, SquareType.Payment);
        Square biografen = new Square("Biografen", 3, SquareColor.Red, SquareType.Payment);
        Square legetøjsButikken = new Square("Legetøjsbutikken", 3, SquareColor.Yellow, SquareType.Payment);
        Square dyreHandlen = new Square("Dyrehandlen", 3, SquareColor.Yellow, SquareType.Payment);
        Square gåIFængsel = new Square("Gå i fængsel", 0, SquareColor.None, SquareType.GotoJail);
        Square bowlingHalen = new Square("Bowlinghalen", 4, SquareColor.Green, SquareType.Payment);
        Square zoo = new Square("Zoo", 4, SquareColor.Green, SquareType.Payment);
        Square vandLandet = new Square("Vandlandet", 5, SquareColor.DarkBlue, SquareType.Payment);
        Square strandPromenaden = new Square("Strandpromenaden", 5, SquareColor.DarkBlue, SquareType.Payment);

        int index = 0;
        boardSquares[index++] = new BoardSquare(start);
        boardSquares[index++] = new BoardSquare(burgerBaren);
        boardSquares[index++] = new BoardSquare(pizzaria);
        boardSquares[index++] = new BoardSquare(chance);
        boardSquares[index++] = new BoardSquare(slikButikken);
        boardSquares[index++] = new BoardSquare(isKiosken);
        boardSquares[index++] = new BoardSquare(påBesoeg);
        boardSquares[index++] = new BoardSquare(museet);
        boardSquares[index++] = new BoardSquare(biblioteket);
        boardSquares[index++] = new BoardSquare(chance);
        boardSquares[index++] = new BoardSquare(skaterParken);
        boardSquares[index++] = new BoardSquare(svømminPoolen);
        boardSquares[index++] = new BoardSquare(gratisParkering);
        boardSquares[index++] = new BoardSquare(spilleHalen);
        boardSquares[index++] = new BoardSquare(biografen);
        boardSquares[index++] = new BoardSquare(chance);
        boardSquares[index++] = new BoardSquare(legetøjsButikken);
        boardSquares[index++] = new BoardSquare(dyreHandlen);
        boardSquares[index++] = new BoardSquare(gåIFængsel);
        boardSquares[index++] = new BoardSquare(bowlingHalen);
        boardSquares[index++] = new BoardSquare(zoo);
        boardSquares[index++] = new BoardSquare(chance);
        boardSquares[index++] = new BoardSquare(vandLandet);
        boardSquares[index++] = new BoardSquare(strandPromenaden);
    }
}

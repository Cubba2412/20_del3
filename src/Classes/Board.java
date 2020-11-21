package Classes;

import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_main.GUI;

public class Board {

    private int squareCount = 40;
    private GUI gui;
    private BoardSquare[] boardSquares;

    public Board(GUI gui) {
        this.gui = gui;
        this.boardSquares = initializeBoard();
    }

    public BoardSquare getBoardSquareByIndex(int index){
        return boardSquares[index];
    }

   public void takePlayerTurn(Player currentPlayer, int diceValue) throws NotEnoughBalanceException {
        //Remove player from current field

        int nextIndex = currentPlayer.getCurrentSquareIndex() + diceValue;
        int currentIndex = nextIndex % squareCount;
        currentPlayer.setCurrentSquareIndex(this.gui,currentIndex);

       BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()-1];
       Square square = boardSquare.getSquare();

       handleAnySquareBefore(currentPlayer);
        switch (square.getSquareType()) {
            case DoNothing:
                handleNothingSquare(currentPlayer);
                break;
            case Tax:
                handleTaxSquare(currentPlayer);
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

    private void handleTaxSquare(Player currentPlayer) throws NotEnoughBalanceException {
        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()-1];
        Square square = boardSquare.getSquare();
        if (square.getName().equals("<html><table><tr><td>Betal indkomstskat<BR>10% eller kr. 200,-")) {
            boolean choice = this.gui.getUserLeftButtonPressed("Vil du betale 200 kr eller 10% af dine penge?","200kr","10%");
                    if(choice) {
                        currentPlayer.decreaseBalanceBy(200);
                    }
                    else {
                        int balance = currentPlayer.getBalance();
                        int decreaseAmount = (int) Math.round(balance*0.1);
                        currentPlayer.decreaseBalanceBy(decreaseAmount);
                    }
        }
        else {
            this.gui.showMessage("Du skal betale en ekstraordinær statsskat på 100kr!");
            currentPlayer.decreaseBalanceBy(100);
        }
    }
    private void handleAnySquareBefore(Player currentPlayer) throws NotEnoughBalanceException {
        if (currentPlayer.isInPrison()) {
            currentPlayer.decreaseBalanceBy(1);
            currentPlayer.setInPrison(false);
        }
    }

    private void handleAnySquareAfter(Player currentPlayer, int nextIndex) {
        if (nextIndex >= squareCount && !currentPlayer.isInPrison()) {
            currentPlayer.increaseBalanceBy(200);
        }
    }

    private void handleNothingSquare(Player currentPlayer) {

    }

    private void handlePaymentSquare(Player currentPlayer) throws NotEnoughBalanceException {

        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()-1];
        Square square = boardSquare.getSquare();
        int price = square.getPrice();
        Player soldToPlayer = boardSquare.getSoldToPlayer();

        // if the soldToPlayer is the same as the currentPlayer then do not do anything because
        // the currentPlayer already owns this square
        if (soldToPlayer == currentPlayer){
            return;
        }

        else if (soldToPlayer == null) {
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
        int prisonIndex = getSquareIndexByType(SquareType.Prison);
        currentPlayer.setCurrentSquareIndex(this.gui,prisonIndex);
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


    private BoardSquare[] initializeBoard() {
        BoardSquare[] boardSquares = new BoardSquare[squareCount];
        Square start = new Square(this.gui.getFields()[0], 0, SquareColor.None, SquareType.Start);
        Square Roedovrevej = new Square(this.gui.getFields()[1], 60, SquareColor.Blue, SquareType.Payment);
        Square chance1 = new Square(this.gui.getFields()[2], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Hvidovrevej = new Square(this.gui.getFields()[3], 60, SquareColor.Blue, SquareType.Payment);
        Square IndkomstSkat = new Square(this.gui.getFields()[4], 200, SquareColor.Gray, SquareType.Tax);
        Square Oeresund = new Square(this.gui.getFields()[5], 200, SquareColor.Ship, SquareType.Payment);
        Square Roskildevej = new Square(this.gui.getFields()[6], 100, SquareColor.Pink, SquareType.Payment);
        Square chance2 = new Square(this.gui.getFields()[7], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Valby_Langgade = new Square(this.gui.getFields()[8], 100, SquareColor.Pink, SquareType.Payment);
        Square Allegade = new Square(this.gui.getFields()[9], 120, SquareColor.Pink, SquareType.Payment);
        Square Faengsel = new Square(this.gui.getFields()[10], 0, SquareColor.None, SquareType.Prison);
        Square Frederiksberg_Alle = new Square(this.gui.getFields()[11], 140, SquareColor.Green, SquareType.Payment);
        Square Tuborg = new Square(this.gui.getFields()[12], 150, SquareColor.Beer, SquareType.Payment);
        Square Bulowsvej = new Square(this.gui.getFields()[13], 140, SquareColor.Green, SquareType.Payment);
        Square Gammel_Kongevej = new Square(this.gui.getFields()[14], 140, SquareColor.Green, SquareType.Payment);
        Square DFDS = new Square(this.gui.getFields()[15], 200, SquareColor.Ship, SquareType.Payment);
        Square Bernstoffsvej = new Square(this.gui.getFields()[16], 180, SquareColor.DarkGray, SquareType.Payment);
        Square chance3 = new Square(this.gui.getFields()[17], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Hellerupvej = new Square(this.gui.getFields()[18], 180, SquareColor.DarkGray, SquareType.Payment);
        Square Strandvejen = new Square(this.gui.getFields()[19], 180, SquareColor.DarkGray, SquareType.Payment);
        Square Helle = new Square(this.gui.getFields()[20], 0, SquareColor.None, SquareType.FreeParking);
        Square Trianglen = new Square(this.gui.getFields()[21], 220, SquareColor.Red, SquareType.Payment);
        Square chance4 = new Square(this.gui.getFields()[22], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Oesterbrogade = new Square(this.gui.getFields()[23], 220, SquareColor.Red, SquareType.Payment);
        Square Groenningen = new Square(this.gui.getFields()[24], 220, SquareColor.Red, SquareType.Payment);
        Square Oes = new Square(this.gui.getFields()[25], 200, SquareColor.Ship, SquareType.Payment);
        Square Bredgade = new Square(this.gui.getFields()[26], 260, SquareColor.White, SquareType.Payment);
        Square Kgs_nytorv = new Square(this.gui.getFields()[27], 260, SquareColor.White, SquareType.Payment);
        Square Carlsberg = new Square(this.gui.getFields()[28], 150, SquareColor.Beer, SquareType.Payment);
        Square Oestergade = new Square(this.gui.getFields()[29], 280, SquareColor.White, SquareType.Payment);
        Square Gaa_i_faensgel = new Square(this.gui.getFields()[30], 0, SquareColor.None, SquareType.GotoJail);
        Square Amagertorv = new Square(this.gui.getFields()[31], 300, SquareColor.Yellow, SquareType.Payment);
        Square Vimmelskaftet = new Square(this.gui.getFields()[32], 300, SquareColor.Yellow, SquareType.Payment);
        Square chance5 = new Square(this.gui.getFields()[33], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Nygade = new Square(this.gui.getFields()[34], 320, SquareColor.Yellow, SquareType.Payment);
        Square Bornholm = new Square(this.gui.getFields()[35], 200, SquareColor.Ship, SquareType.Payment);
        Square chance6 = new Square(this.gui.getFields()[36], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Frederiksberggade = new Square(this.gui.getFields()[37], 350, SquareColor.Purple, SquareType.Payment);
        Square Skat = new Square(this.gui.getFields()[38], 100, SquareColor.Gray, SquareType.Tax);
        Square Raadhuspladsen = new Square(this.gui.getFields()[39], 400, SquareColor.Purple, SquareType.Payment);

        int index = 0;
        boardSquares[index] = new BoardSquare(start);
        boardSquares[index++] = new BoardSquare(Roedovrevej);
        boardSquares[index++] = new BoardSquare(chance1);
        boardSquares[index++] = new BoardSquare(Hvidovrevej);
        boardSquares[index++] = new BoardSquare(IndkomstSkat);
        boardSquares[index++] = new BoardSquare(Oeresund);
        boardSquares[index++] = new BoardSquare(Roskildevej);
        boardSquares[index++] = new BoardSquare(chance2);
        boardSquares[index++] = new BoardSquare(Valby_Langgade);
        boardSquares[index++] = new BoardSquare(Allegade);
        boardSquares[index++] = new BoardSquare(Faengsel);
        boardSquares[index++] = new BoardSquare(Frederiksberg_Alle);
        boardSquares[index++] = new BoardSquare(Tuborg);
        boardSquares[index++] = new BoardSquare(Bulowsvej);
        boardSquares[index++] = new BoardSquare(Gammel_Kongevej);
        boardSquares[index++] = new BoardSquare(DFDS);
        boardSquares[index++] = new BoardSquare(Bernstoffsvej);
        boardSquares[index++] = new BoardSquare(chance3);
        boardSquares[index++] = new BoardSquare(Hellerupvej);
        boardSquares[index++] = new BoardSquare(Strandvejen);
        boardSquares[index++] = new BoardSquare(Helle);
        boardSquares[index++] = new BoardSquare(Trianglen);
        boardSquares[index++] = new BoardSquare(chance4);
        boardSquares[index++] = new BoardSquare(Oesterbrogade);
        boardSquares[index++] = new BoardSquare(Groenningen);
        boardSquares[index++] = new BoardSquare(Oes);
        boardSquares[index++] = new BoardSquare(Bredgade);
        boardSquares[index++] = new BoardSquare(Kgs_nytorv);
        boardSquares[index++] = new BoardSquare(Carlsberg);
        boardSquares[index++] = new BoardSquare(Oestergade);
        boardSquares[index++] = new BoardSquare(Gaa_i_faensgel);
        boardSquares[index++] = new BoardSquare(Amagertorv);
        boardSquares[index++] = new BoardSquare(Vimmelskaftet);
        boardSquares[index++] = new BoardSquare(chance5);
        boardSquares[index++] = new BoardSquare(Nygade);
        boardSquares[index++] = new BoardSquare(Bornholm);
        boardSquares[index++] = new BoardSquare(chance6);
        boardSquares[index++] = new BoardSquare(Frederiksberggade);
        boardSquares[index++] = new BoardSquare(Skat);
        boardSquares[index++] = new BoardSquare(Raadhuspladsen);
        return boardSquares;
    }
}

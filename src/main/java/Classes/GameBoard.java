package Classes;

import gui_fields.*;
import gui_main.GUI;

import java.awt.*;

public class GameBoard {

    private int squareCount = 24;
    private int chanceCount = 7;
    private GUI gui;
    private Square[] boardSquares;
    private GUI_Field[] gui_fields;
    private ChanceCard[] chanceCards;
    private Player[] players;

    public GameBoard(Player[] players) {
        initializeBoard();
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    public GUI_Field[] getFields() {
        return gui_fields;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public GUI getGUI() {
        return gui;
    }

   /* public GUI_Field getBoardSquareByIndex(int index){
        return boardSquares[index];
    }*/

   public void takePlayerTurn(Player currentPlayer, Dice dice) throws NotEnoughBalanceException {
       boolean prison = handleAnySquareBefore(currentPlayer,dice);
       if (!prison) {
           String name = currentPlayer.getName();
           String choice = this.gui.getUserButtonPressed("Spiller " + name + "'s tur. Kast terningen - tryk på Kast", "Kast");
           // Initialize dice value
           int diceValue = -1;
           if (choice.equals("Kast")) {
               diceValue = dice.roll();
               this.gui.setDie(diceValue);
           }
           //Remove player from current field
           int nextIndex = movePlayer(currentPlayer, diceValue);
           Square boardSquare;
           if (currentPlayer.getCurrentSquareIndex() == 0 || nextIndex > 40) {
               boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
           }
           else {
               boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex() - 1];
           }
           printBoardSquare(boardSquare);
           evaluateSquare(boardSquare,currentPlayer);


           handleAnySquareAfter(currentPlayer, nextIndex);
       }
    }

    private void evaluateSquare(Square square, Player currentPlayer) throws NotEnoughBalanceException {
        switch (square.getSquareType()) {
            case DoNothing:
                handleNothingSquare(currentPlayer);
                break;
            case Start:
                handleStartSquare(currentPlayer);
                break;
            case Payment:
                handlePaymentSquare(currentPlayer);
                break;
            case GotoJail:
                handleGotoJailSquare(currentPlayer);
                break;
            case TakeChanceCard:
                handleTakeChanceCardSquare(currentPlayer);
                break;
        }
    }

    private void handleStartSquare(Player currentPlayer) {
        gui.showMessage("Du har passeret start! Modtag 200kr");
        currentPlayer.increaseBalanceBy(200);
    }

    private int movePlayer(Player currentPlayer, int diceValue) {

        //Calculate next index
        int nextIndex = currentPlayer.getCurrentSquareIndex() + diceValue;
        //Set players current index to this
        int currentIndex = nextIndex % squareCount;
        //Check if the player has once again reached start
        boolean passedStart = checkIndex(nextIndex);

        if(passedStart) {
            currentIndex = nextIndex - 40;
            currentPlayer.setCurrentSquareIndex(this.gui, currentIndex);
        }
        else {
            currentPlayer.setCurrentSquareIndex(this.gui, currentIndex);
        }
        return currentIndex;
    }

    private boolean checkIndex(int nextIndex) {
        if (nextIndex >= 40) {
            int temp = 0;
            return true;
        }
        else {
            return false;
        }
    }
    private void  printBoardSquare(Square square) {
        //Special cases, where the field should not be printed
        if (square.getSubText().equals("10% el. 200")) {
            return;
        }
        else if (square.getSubText().equals("Gå i fængsel")) {
            return;
        }
        else if (square.getSubText().equals("Betal 100")) {
            return;
        }
        else if (square.getSquareType() == SquareType.TakeChanceCard) {
            return;
        }
        this.gui.showMessage("Square: " + square.getTitle() + "\n"+ "Pris: " + square.getFieldPrice());
    }
   /* private void handleTaxSquare(Player currentPlayer) throws NotEnoughBalanceException {
        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()-1];
        Square square = boardSquare.getSquare();
        if (square.getSubText().equals("10% el. 200")) {
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
    }*/

    private boolean handleAnySquareBefore(Player currentPlayer,Dice dice) throws NotEnoughBalanceException {
        if (currentPlayer.isInPrison()) {
            if (currentPlayer.hasJailFreeCard()) {
                boolean choice1 = gui.getUserLeftButtonPressed("Du har et kom ud af fængslet kort. Vil du bruge det?", "Ja", "Nej");
                if (choice1) {
                    currentPlayer.setInPrison(false);
                    return false;
                } else {
                    boolean choice = gui.getUserLeftButtonPressed(currentPlayer.getName() + " Er i fængsel. Slå en 6'er for at komme fri eller betal 200", "Kast", "Betal 200");
                    if (choice) {
                        int diceValue = dice.roll();
                        this.gui.setDie(diceValue);
                        if (diceValue == 6) {
                            gui.showMessage("Du slog en 6'er og undslap fængslet!");
                            movePlayer(currentPlayer, diceValue);
                            currentPlayer.setInPrison(false);
                            return false;
                        } else {
                            gui.showMessage("Du slog ikke en 6'er!");
                            return true;
                        }
                    } else {
                        currentPlayer.decreaseBalanceBy(200);
                        String button = this.gui.getUserButtonPressed("Du kom ud af fængslet. Kast terningen - tryk på Kast", "Kast");
                        if (button.equals("Kast")) {
                            int diceValue = dice.roll();
                            this.gui.setDie(diceValue);
                            currentPlayer.setInPrison(false);
                            movePlayer(currentPlayer, diceValue);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void handleAnySquareAfter(Player currentPlayer, int nextIndex) {
        if (nextIndex >= squareCount) {
            currentPlayer.increaseBalanceBy(200);
            gui.showMessage("Du har passeret start og modtager 200kr!");
        }
    }

    private void handleNothingSquare(Player currentPlayer) {

    }

    private void handlePaymentSquare(Player currentPlayer) throws NotEnoughBalanceException {

        Square boardSquare;
        if(currentPlayer.getCurrentSquareIndex() == 0) {
            boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
        }
        else {
            boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex() - 1];
        }
        int fieldPrice = boardSquare.getFieldPrice();
        Player soldToPlayer = boardSquare.getSoldToPlayer();

        // if the soldToPlayer is the same as the currentPlayer then do not do anything because
        // the currentPlayer already owns this square
        if (soldToPlayer == currentPlayer){
                    gui.showMessage("Du ejer det her felt. Der var du heldig!");
        }

        else if (soldToPlayer == null) {
            boolean choice  = gui.getUserLeftButtonPressed(
                    "Vil du købe dette felt? " + "    " + "Square: " + boardSquare.getTitle() + "    "+ "Pris: " + fieldPrice , "Ja","Nej");
            // the first player on this square becomes the owner and pays the price
            if (choice) {
                currentPlayer.decreaseBalanceBy(fieldPrice);
                boardSquare.setSoldToPlayer(currentPlayer);
            }

        } else {
            int price = boardSquare.getFieldPrice();
            // other players coming to this square pays to the owner and become one of the renters
            gui.showMessage("Du er landet på et felt for en anden spiller og skal betale dem " + String.valueOf(price));
            soldToPlayer.increaseBalanceBy(price);
            currentPlayer.decreaseBalanceBy(price);
           // boardSquare.addRentedToPlayer(currentPlayer);
        }
    }


    private void handleGotoJailSquare(Player currentPlayer) {
        currentPlayer.setInPrison(true);
        gui.showMessage("Du skal i Fængsel!");
        int prisonIndex = getSquareIndexByType(SquareType.Prison);
        currentPlayer.setCurrentSquareIndex(this.gui,prisonIndex+1);
    }



    private void handleTakeChanceCardSquare(Player currentPlayer) throws NotEnoughBalanceException {
        gui.showMessage("Du er landet på prøv lykken! Tag et chance kort");
        ChanceCard chanceCard = chanceCards[1].getRandomChanceCard(chanceCards);
        String text = chanceCard.getText();
        gui.displayChanceCard(text);
        String action = chanceCard.getActionType();
        switch (action) {
            case "Start":
                currentPlayer.increaseBalanceBy(200);
                currentPlayer.setCurrentSquareIndex(gui,0);
                break;
            case "Move":
                int currentIndex = currentPlayer.getCurrentSquareIndex();
                if (text.equals("Ryk 5 felter frem")) {
                        movePlayer(currentPlayer,5);
                        Square boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
                        evaluateSquare(boardSquare,currentPlayer);
                }
                else {
                    boolean choice = gui.getUserLeftButtonPressed("Vil du rykke et felt frem eller tage et nyt chancekort?", "Ryk 1 Felt Frem", "Tag nyt chancekort");
                    if (choice) {
                        movePlayer(currentPlayer,1);
                        Square boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
                        evaluateSquare(boardSquare,currentPlayer);
                    }
                    else {
                        handleTakeChanceCardSquare(currentPlayer);
                    }
                }
                break;
            case "Pay":
                currentPlayer.decreaseBalanceBy(200);
                break;
            case "Prison":
                if (currentPlayer.hasJailFreeCard()) {
                    return;
                }
                else {
                    currentPlayer.setGetOutOfJailCard();
                }
            break;
            case "PayByOthers":
                for(int i=0;i<players.length;i++) {
                    if (!players[i].getName().equals(currentPlayer.getName())) {
                        players[i].decreaseBalanceBy(100);
                    }
                    currentPlayer.increaseBalanceBy(100);
                }
                break;
            case "PayByBank":
                currentPlayer.increaseBalanceBy(200);
                break;
        }
    }

    private int getSquareIndexByType(SquareType squareType) {
        for (int i = 0; i < squareCount; i++) {
            Square boardSquare = boardSquares[i];
            if (boardSquare.getSquareType() == squareType) {
                return i;
            }
        }
        return 0;
    }


    private void initializeBoard() {
        // Initialize chance cards
        this.chanceCards = initializeCards();
        // Squares
        Square start = new Square("Start", "","Du har passeret Start. Modtag 2M",2, Color.white, Color.red, SquareType.Start);
        Square Burgerbaren = new Square("Burgerbaren", "", "Burgerbaren",1, Color.cyan, Color.BLACK, SquareType.Payment);
        Square Pizzariaet = new Square("Pizzariaet", "", "Pizzariaet",1, Color.cyan, Color.BLACK, SquareType.Payment);
        Square chance1 = new Square("Chance", "Ta' Chancen!", "Chance",0, Color.white, Color.BLACK, SquareType.TakeChanceCard);
        Square Slikbutikken = new Square("Slikbutikken", "", "Slikbutikken",1, Color.lightGray, Color.BLACK, SquareType.Payment);
        Square Iskiosken = new Square("Iskiosken", "", "Iskiosken",1, Color.lightGray, Color.BLACK, SquareType.Payment);
        Square Faengsel = new Square("Fængsel", "Bare på besøg", "Fængsel",0, Color.white, Color.BLACK, SquareType.Prison);
        Square Museet = new Square("Museet", "", "Museet",2, Color.pink, Color.BLACK, SquareType.Payment);
        Square Biblioteket = new Square("Biblioteket", "", "Biblioteket",2, Color.pink, Color.BLACK, SquareType.Payment);
        Square chance2 = new Square("Chance", "Ta' Chancen!", "Chance",0, Color.white, Color.BLACK, SquareType.TakeChanceCard);
        Square Skateparken = new Square("Skateparken", "", "Skateparken",2, Color.orange, Color.BLACK, SquareType.Payment);
        Square Svoemmingpolen = new Square("Svømmingpoolen", "", "Svømmingpoolen",2, Color.orange, Color.BLACK, SquareType.Payment);
        Square Gratisparkering = new Square("Gratis Parkering", "", "Gratis Parkering",0, Color.white, Color.BLACK, SquareType.DoNothing);
        Square Spillehallen = new Square("Spillehallen", "", "Spillehallen",3, Color.red, Color.BLACK, SquareType.Payment);
        Square Biografen = new Square("Biografen", "", "Biografen",3, Color.red, Color.BLACK, SquareType.Payment);
        Square chance3 = new Square("Chance", "Ta' Chancen!", "Chance",0, Color.white, Color.BLACK, SquareType.TakeChanceCard);
        Square Legetoejsbutikken = new Square("Legetøjsbutikken", "", "Legetøjsbutikken",3, Color.yellow, Color.BLACK, SquareType.Payment);
        Square Dyrehandlen = new Square("Dyrehandlen", "", "Dyrehandlen",3, Color.yellow, Color.BLACK, SquareType.Payment);
        Square GaaIFaengsel = new Square("Gå I Fængsel", "Du skal i Fængsel!", "Gå I Fængsel",0, Color.white, Color.BLACK, SquareType.GotoJail);
        Square Bowlinghallen = new Square("Bowlinghallen", "", "Bowlinghallen",4, Color.green, Color.BLACK, SquareType.Payment);
        Square Zoo = new Square("Zoo", "", "Zoo",4, Color.green, Color.BLACK, SquareType.Payment);
        Square chance4 = new Square("Chance", "Ta' Chancen!", "Chance",0, Color.white, Color.BLACK, SquareType.TakeChanceCard);
        Square Vandlandet = new Square("Vandlandet", "", "Vandlandet",5, Color.blue, Color.BLACK, SquareType.Payment);
        Square Strandpromenaden = new Square("Strandpromenaden", "", "Strandpromenaden",5, Color.blue, Color.BLACK, SquareType.Payment);
        Square[] boardSquares = new Square[squareCount];
        int index = 0;
        boardSquares[index] = start;
        boardSquares[index++] = Burgerbaren;
        boardSquares[index++] = Pizzariaet;
        boardSquares[index++] = chance1;
        boardSquares[index++] = Slikbutikken;
        boardSquares[index++] = Iskiosken;
        boardSquares[index++] = Faengsel;
        boardSquares[index++] = Museet;
        boardSquares[index++] = Biblioteket;
        boardSquares[index++] = chance2;
        boardSquares[index++] = Skateparken;
        boardSquares[index++] = Svoemmingpolen;
        boardSquares[index++] = Gratisparkering;
        boardSquares[index++] = Spillehallen;
        boardSquares[index++] = Biografen;
        boardSquares[index++] = chance3;
        boardSquares[index++] = Legetoejsbutikken;
        boardSquares[index++] = Dyrehandlen;
        boardSquares[index++] = GaaIFaengsel;
        boardSquares[index++] = Bowlinghallen;
        boardSquares[index++] = Zoo;
        boardSquares[index++] = chance4;
        boardSquares[index++] = Vandlandet;
        boardSquares[index++] = Strandpromenaden;
        this.boardSquares = boardSquares;

        GUI_Field[] gui_fields = {
                MapToGui(start),
        MapToGui(Burgerbaren),
        MapToGui(Pizzariaet),
        MapToGui(chance1),
        MapToGui(Slikbutikken),
        MapToGui(Iskiosken),
        MapToGui(Faengsel),
        MapToGui(Museet),
        MapToGui(Biblioteket),
        MapToGui(chance2),
        MapToGui(Skateparken),
        MapToGui(Svoemmingpolen),
        MapToGui(Gratisparkering),
        MapToGui(Spillehallen),
        MapToGui(Biografen),
        MapToGui(chance3),
        MapToGui(Legetoejsbutikken),
        MapToGui(Dyrehandlen),
        MapToGui(GaaIFaengsel),
        MapToGui(Bowlinghallen),
        MapToGui(Zoo),
        MapToGui(chance4),
        MapToGui(Vandlandet),
        MapToGui(Strandpromenaden)
        };

        this.gui_fields = gui_fields;
    }

    private GUI_Field MapToGui(Square square) {
        switch (square.getSquareType()) {

            case Start:
                return new GUI_Start(square.getTitle(), square.getSubText(), square.getDescription(),square.getBGColor(),square.getFGColor());
            case DoNothing:
                return new GUI_Refuge();
            case Payment:
                return new GUI_Street(square.getTitle(), square.getSubText(), square.getDescription(),square.getStringPrice(),square.getBGColor(),square.getFGColor());
            case GotoJail:
                return new GUI_Jail();
            case Prison:
                return new GUI_Jail();
            case TakeChanceCard:
                return new GUI_Chance();
        }
        return new GUI_Empty();
    }

    private ChanceCard[] initializeCards() {
        ChanceCard[] chanceCards = new ChanceCard[chanceCount];
        ChanceCard chance1 = new ChanceCard("Ryk frem til START. Modtag 200","Start",200,0);
        ChanceCard chance2 = new ChanceCard("Ryk 5 felter frem","Move",0,5);
        ChanceCard chance3 = new ChanceCard("Ryk 1 felt frem eller tag et chancekort mere","Move",0,1);
        ChanceCard chance4 = new ChanceCard("Du har spist for meget slik. Betal 200 til banken","Pay",200,0);
        ChanceCard chance5 = new ChanceCard("Du løslades uden omkostninger. Behold dette kort indtil du får brugt det","Prison",0,0);
        ChanceCard chance6 = new ChanceCard("Det er din fødselsdag! Alle giver dig 100. TILLYKKE MED FØDSELSDAGEN!","PayByOthers",100,0);
        ChanceCard chance7 = new ChanceCard("Du har lavet alle dine lektier! Modtag 200 fra banken.","PayByBank",200,0);

        int index = 0;
        chanceCards[index] = chance1;
        chanceCards[index++] = chance2;
        chanceCards[index++] = chance3;
        chanceCards[index++] = chance4;
        chanceCards[index++] = chance5;
        chanceCards[index++] = chance6;
        chanceCards[index++] = chance7;
        return chanceCards;
    }
}

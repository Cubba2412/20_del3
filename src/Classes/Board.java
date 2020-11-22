package Classes;

import gui_fields.GUI_Street;
import gui_main.GUI;

public class Board {

    private int squareCount = 40;
    private int chanceCount = 7;
    private GUI gui;
    private BoardSquare[] boardSquares;
    private ChanceCard[] chanceCards;
    private Player[] players;

    public Board(GUI gui,Player[] players) {
        this.gui = gui;
        this.players = players;
        this.boardSquares = initializeBoard();
    }

    public BoardSquare getBoardSquareByIndex(int index){
        return boardSquares[index];
    }

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
           BoardSquare boardSquare;
           if (currentPlayer.getCurrentSquareIndex() == 0 || nextIndex > 40) {
               boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
           }
           else {
               boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex() - 1];
           }
           Square square = boardSquare.getSquare();
           printBoardSquare(square);
           evaluateSquare(square,currentPlayer);


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
            case Tax:
                handleTaxSquare(currentPlayer);
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
            case Ship:
                handlePaymentSquare(currentPlayer);
                break;
            case Beer:
                handlePaymentSquare(currentPlayer);
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
        this.gui.showMessage("Square: " + square.getName() + "\n"+ "Pris: " + square.getFieldPrice());
    }
    private void handleTaxSquare(Player currentPlayer) throws NotEnoughBalanceException {
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
    }
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

        BoardSquare boardSquare;
        if(currentPlayer.getCurrentSquareIndex() == 0) {
            boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
        }
        else {
            boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex() - 1];
        }
        Square square = boardSquare.getSquare();
        int fieldPrice = square.getFieldPrice();
        Player soldToPlayer = boardSquare.getSoldToPlayer();

        // if the soldToPlayer is the same as the currentPlayer then do not do anything because
        // the currentPlayer already owns this square
        if (soldToPlayer == currentPlayer){
            switch ((square.getSquareType())) {
                case Payment:
                    String choice = gui.getUserButtonPressed("Vil du bygge et hus (Pris: 100 per hus) eller hotel (Pris:400 og 4 huse) her?","Hus","Hotel","Nej");
                    if (choice.equals("Hus"))
                    {
                        int priceOfHouse = buildHouse(square);
                        currentPlayer.decreaseBalanceBy(priceOfHouse);
                    }
                    else if (choice.equals("Hotel")) {
                        buildHotel(square);
                    }
                    else {
                        //Do nothing
                    }
                    break;

                default:
                    gui.showMessage("Du ejer det her felt. Der var du heldig!");
            }
            return;
        }

        else if (soldToPlayer == null) {
            boolean choice  = gui.getUserLeftButtonPressed(
                    "Vil du købe dette felt? " + "    " + "Square: " + square.getName() + "    "+ "Pris: " + fieldPrice , "Ja","Nej");
            // the first player on this square becomes the owner and pays the price
            if (choice) {
                currentPlayer.decreaseBalanceBy(fieldPrice);
                boardSquare.setSoldToPlayer(currentPlayer);
            }

        } else {
            int price = square.getPrice();
            // other players coming to this square pays to the owner and become one of the renters
            gui.showMessage("Du er landet på et felt for en anden spiller og skal betale dem " + String.valueOf(price));
            soldToPlayer.increaseBalanceBy(price);
            currentPlayer.decreaseBalanceBy(price);
            boardSquare.addRentedToPlayer(currentPlayer);
        }
    }

    private int buildHouse(Square square) {
        int oldHouses = square.getNumHouses();
        int houses = gui.getUserInteger("Hvor mange huse vil du bygge? (Pris per hus = 100)");
        while(houses < 1 || houses > 4-oldHouses) {
            gui.showMessage("Du kan kun bygge mellem 1-4 huse. Du har indtil videre " + String.valueOf(oldHouses) + " huse her");
            houses = gui.getUserInteger("Hvor mange vil du bygge? (Pris per hus = 100)");
        }
        GUI_Street street = (GUI_Street) square.getField();
        street.setHouses(houses);
        square.setNumHouses(houses);
        int price = houses*100;
        return price;
    }

    private void buildHotel(Square square) {
        if (square.getNumHouses() == 4) {
            boolean hasHotel = square.hasHotel();
            if (hasHotel) {
                gui.showMessage("Du har allerede et hotel på denne grund. Du kan kun have ét");
            } else {
                GUI_Street street = (GUI_Street) square.getField();
                street.setHotel(true);
                square.setHotel();
            }
        }
        else{
            boolean choice = gui.getUserLeftButtonPressed("Du har ikke nok huse til at bygge et hotel her. Vil du købe nogle?","Ja","Nej");
            if(choice) {
                buildHouse(square);
                buildHotel(square);
            }
            else {
                return;
            }
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
                        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
                        Square square = boardSquare.getSquare();
                        evaluateSquare(square,currentPlayer);
                }
                else {
                    boolean choice = gui.getUserLeftButtonPressed("Vil du rykke et felt frem eller tage et nyt chancekort?", "Ryk 1 Felt Frem", "Tag nyt chancekort");
                    if (choice) {
                        movePlayer(currentPlayer,1);
                        BoardSquare boardSquare = boardSquares[currentPlayer.getCurrentSquareIndex()];
                        Square square = boardSquare.getSquare();
                        evaluateSquare(square,currentPlayer);
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
            BoardSquare boardSquare = boardSquares[i];
            if (boardSquare.getSquare().getSquareType() == squareType) {
                return i;
            }
        }
        return 0;
    }


    private BoardSquare[] initializeBoard() {
        // Initialize chance cards
        this.chanceCards = initializeCards();
        // Squares
        BoardSquare[] boardSquares = new BoardSquare[squareCount];
        Square start = new Square(this.gui.getFields()[0], 0, SquareColor.None, SquareType.Start);
        Square Roedovrevej = new Square(this.gui.getFields()[1], 60, SquareColor.Blue, SquareType.Payment);

        Square chance1 = new Square(this.gui.getFields()[2], 0, SquareColor.LightGray, SquareType.TakeChanceCard);

        Square Hvidovrevej = new Square(this.gui.getFields()[3], 60, SquareColor.Blue, SquareType.Payment);
        Square IndkomstSkat = new Square(this.gui.getFields()[4], 200, SquareColor.Gray, SquareType.Tax);
        Square Oeresund = new Square(this.gui.getFields()[5], 200, SquareColor.Ship, SquareType.Ship);
        Square Roskildevej = new Square(this.gui.getFields()[6], 100, SquareColor.Pink, SquareType.Payment);

        Square chance2 = new Square(this.gui.getFields()[7], 0, SquareColor.LightGray, SquareType.TakeChanceCard);

        Square Valby_Langgade = new Square(this.gui.getFields()[8], 100, SquareColor.Pink, SquareType.Payment);
        Square Allegade = new Square(this.gui.getFields()[9], 120, SquareColor.Pink, SquareType.Payment);
        Square Faengsel = new Square(this.gui.getFields()[10], 0, SquareColor.None, SquareType.Prison);
        Square Frederiksberg_Alle = new Square(this.gui.getFields()[11], 140, SquareColor.Green, SquareType.Payment);
        Square Tuborg = new Square(this.gui.getFields()[12], 150, SquareColor.Beer, SquareType.Beer);
        Square Bulowsvej = new Square(this.gui.getFields()[13], 140, SquareColor.Green, SquareType.Payment);
        Square Gammel_Kongevej = new Square(this.gui.getFields()[14], 140, SquareColor.Green, SquareType.Payment);
        Square DFDS = new Square(this.gui.getFields()[15], 200, SquareColor.Ship, SquareType.Ship);
        Square Bernstoffsvej = new Square(this.gui.getFields()[16], 180, SquareColor.DarkGray, SquareType.Payment);
        Square chance3 = new Square(this.gui.getFields()[17], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Hellerupvej = new Square(this.gui.getFields()[18], 180, SquareColor.DarkGray, SquareType.Payment);
        Square Strandvejen = new Square(this.gui.getFields()[19], 180, SquareColor.DarkGray, SquareType.Payment);
        Square Helle = new Square(this.gui.getFields()[20], 0, SquareColor.None, SquareType.DoNothing);
        Square Trianglen = new Square(this.gui.getFields()[21], 220, SquareColor.Red, SquareType.Payment);
        Square chance4 = new Square(this.gui.getFields()[22], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Oesterbrogade = new Square(this.gui.getFields()[23], 220, SquareColor.Red, SquareType.Payment);
        Square Groenningen = new Square(this.gui.getFields()[24], 220, SquareColor.Red, SquareType.Payment);
        Square Oes = new Square(this.gui.getFields()[25], 200, SquareColor.Ship, SquareType.Ship);
        Square Bredgade = new Square(this.gui.getFields()[26], 260, SquareColor.White, SquareType.Payment);
        Square Kgs_nytorv = new Square(this.gui.getFields()[27], 260, SquareColor.White, SquareType.Payment);
        Square Carlsberg = new Square(this.gui.getFields()[28], 150, SquareColor.Beer, SquareType.Beer);
        Square Oestergade = new Square(this.gui.getFields()[29], 280, SquareColor.White, SquareType.Payment);
        Square Gaa_i_faensgel = new Square(this.gui.getFields()[30], 0, SquareColor.None, SquareType.GotoJail);
        Square Amagertorv = new Square(this.gui.getFields()[31], 300, SquareColor.Yellow, SquareType.Payment);
        Square Vimmelskaftet = new Square(this.gui.getFields()[32], 300, SquareColor.Yellow, SquareType.Payment);
        Square chance5 = new Square(this.gui.getFields()[33], 0, SquareColor.LightGray, SquareType.TakeChanceCard);
        Square Nygade = new Square(this.gui.getFields()[34], 320, SquareColor.Yellow, SquareType.Payment);
        Square Bornholm = new Square(this.gui.getFields()[35], 200, SquareColor.Ship, SquareType.Ship);
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

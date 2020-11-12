package Classes;

public class Board {

    private int squareCount = 24;
    private Square[] squares = new Square[squareCount];

    public Board(){
        initializeBoard();
    }

    private void initializeBoard(){

        Square  start = new Square("Start", 0, SquareColor.None);
        Square  burgerBaren = new Square("Burgerbaren", 2, SquareColor.Brown);
        Square  pizzaria = new Square("Pizzaria", 1, SquareColor.Brown);
        Square  chance = new Square("Chace", 0, SquareColor.None);
        Square  slikButikken = new Square("Slikbutikken", 1, SquareColor.LightBlue);
        Square  isKiosken = new Square("Iskiosken", 1, SquareColor.LightBlue);
        Square  påBesoeg = new Square("På besøg", 0, SquareColor.None);
        Square  museet = new Square("Musset", 2, SquareColor.Pink);
        Square  biblioteket= new Square("Biblioteket", 2, SquareColor.Pink);
        Square  skaterParken = new Square("Skatenparken", 2, SquareColor.LightYellow);
        Square  svømminPoolen = new Square("Svømmingpoolen", 2, SquareColor.LightYellow);
        Square  gratisParkering = new Square("Gratis parkering", 0, SquareColor.None);
        Square  spilleHalen = new Square("Spillehalen", 3, SquareColor.Red);
        Square  biografen = new Square("Biografen", 3, SquareColor.Red);
        Square  legetøjsButikken = new Square("Legetøjsbutikken", 3, SquareColor.Yellow);
        Square  dyreHandlen = new Square("Dyrehandlen", 3, SquareColor.Yellow);
        Square  gåIFængsel = new Square("Gå i fængsel", 0, SquareColor.None);
        Square  bowlingHalen = new Square("Bowlinghalen", 4, SquareColor.Green);
        Square  zoo = new Square("Zoo", 4, SquareColor.Green);
        Square  vandLandet = new Square("Vandlandet", 5, SquareColor.DarkBlue);
        Square  strandPromenaden = new Square("Strandpromenaden", 5, SquareColor.DarkBlue);

    }

}

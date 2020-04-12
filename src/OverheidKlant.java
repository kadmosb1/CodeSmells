public class OverheidKlant extends Klant{

    public OverheidKlant (String naam, FactuurAdres adres, String btwNummer) {
        super (naam, adres, btwNummer);
    }

    public double getKortingspercentage () {
        return 2.0;
    }
}

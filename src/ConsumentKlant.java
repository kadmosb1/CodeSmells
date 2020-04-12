public class ConsumentKlant extends Klant {

    public ConsumentKlant (String naam, FactuurAdres adres, String btwNummer) {
        super (naam, adres, btwNummer);
    }

    public double getKortingspercentage () {
        return 0.0;
    }
}

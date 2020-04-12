public class HorecaKlant extends Klant {

    public HorecaKlant (String naam, FactuurAdres adres, String btwNummer) {
        super (naam, adres, btwNummer);
    }

    public double getKortingspercentage () {
        return 4.0;
    }
}

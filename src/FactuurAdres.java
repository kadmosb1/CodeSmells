public class FactuurAdres {
    private Adres adres;
    private Postcode postcode;
    private String woonplaats;

    public FactuurAdres (Adres adres, Postcode postcode, String woonplaats) {
        this.adres = adres;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
    }

    public String toString () {
        return String.format ("%s%n%s  %s%n", adres, postcode, woonplaats);
    }
}

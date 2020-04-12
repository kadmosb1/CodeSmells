public abstract class Klant {

    /*
     * Deze constanten worden gebruikt voor het type klant.
     */
    public static final int CONSUMENT = 1;
    public static final int OVERHEID = 2;
    public static final int HORECA = 3;

    /*
     * Voor een klant worden de volgende gegevens vastgelegd. Er wordt niet
     * gecontroleerd of aan de minimale voorwaarden is voldaan (bijv. dat een post-
     * code uit precies vier cijfers en 2 letters bestaat).
     */
    private String naam;
    private FactuurAdres adres;
    private String btwNummer;

    public Klant (String naam, FactuurAdres adres, String btwNummer) {
        this.naam = naam;
        this.adres = adres;
        this.btwNummer = btwNummer;
    }

    public String getNaam () {
        return naam;
    }

    public FactuurAdres getAdres () {
        return adres;
    }

    public String getBTWNummer () {
        return btwNummer;
    }

    public abstract double getKortingspercentage ();

    /*
     * Als Een klant buiten Nederland 'woont', wordt geen BTW gerekend (die BTW wordt
     * verlegd naar de klant in het buitenland die dit zelf moet melden bij zijn/haar
     * eigen belastingdienst.
     */
    public boolean btwMoetWordenVerlegd () {
        return !btwNummer.substring(0, 2).equals("NL");
    }

    public String getKlantgegevensOpFactuur () {
        return String.format ("%s%n%s", naam, adres);
    }
}
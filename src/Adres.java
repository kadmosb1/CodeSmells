public class Adres {
    private String straat;
    private int huisnummer;
    private String huisnummerToevoeging;

    public Adres (String straat, int huisnummer, String huisnummerToevoeging) {
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.huisnummerToevoeging = huisnummerToevoeging;
    }

    public String toString () {
        return String.format ("%s %d %s", straat, huisnummer, huisnummerToevoeging);
    }
}

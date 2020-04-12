import org.junit.Test;
import static org.junit.Assert.*;

public class KlantTest {

    @Test
    public void testOfBTWWordtVerlegd () {
        Adres adres = new Adres ("Johanna Westerdijkplein", 75, "");
        Postcode pc = new Postcode (2521, "EN");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DEN HAAG");
        Klant klant = new ConsumentKlant("De Haagse Hogeschool", factuurAdres, "BE 0826882419");
        assertTrue (klant.btwMoetWordenVerlegd());
    }

    @Test
    public void testOfBTWNietWordtVerlegd () {
        Adres adres = new Adres ("Johanna Westerdijkplein", 75, "");
        Postcode pc = new Postcode (2521, "EN");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DEN HAAG");
        Klant klant = new OverheidKlant("De Haagse Hogeschool", factuurAdres, "NL 0826882419");
        assertFalse (klant.btwMoetWordenVerlegd());
    }

    @Test
    public void testKortingVoorConsument () {
        Adres adres = new Adres ("Van der Lelijstraat", 20, "");
        Postcode pc = new Postcode (2624, "EM");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DELFT");
        Klant klant = new ConsumentKlant("Karel J. van der Lelij", factuurAdres, "NL 0826882419");
        double expected = 0.0;
        double actual = klant.getKortingspercentage();
        assertEquals (expected, actual, 0.1);
    }

    @Test
    public void testKortingVoorOverheid () {
        Adres adres = new Adres ("Johanna Westerdijkplein", 75, "");
        Postcode pc = new Postcode (2521, "EN");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DEN HAAG");
        Klant klant = new OverheidKlant("De Haagse Hogeschool", factuurAdres, "NL 0826882419");
        double expected = 2.0;
        double actual = klant.getKortingspercentage();
        assertEquals (expected, actual, 0.1);
    }

    @Test
    public void testKortingVoorHoreca () {
        Adres adres = new Adres ("Beestenmarkt", 22, "");
        Postcode pc = new Postcode (2621, "KJ");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DELFT");
        Klant klant = new HorecaKlant("Kobus Kuch", factuurAdres, "NL 0826882419");
        double expected = 4.0;
        double actual = klant.getKortingspercentage();
        assertEquals (expected, actual, 0.1);
    }
}
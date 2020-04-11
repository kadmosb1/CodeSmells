import org.junit.Test;
import static org.junit.Assert.*;

public class KlantTest {

    @Test
    public void testVerleggingVanBTW () {

        Adres adres = new Adres ("Johanna Westerdijkplein", 75, "");
        Postcode pc = new Postcode (2521, "EN");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DEN HAAG");
        Klant klant = new Klant("De Haagse Hogeschool", factuurAdres, "BE 0826882419", Klant.CONSUMENT);
        assertTrue (klant.btwMoetWordenVerlegd());
        klant = new Klant("De Haagse Hogeschool", factuurAdres, "NL 0826882419", Klant.OVERHEID);
        assertFalse (klant.btwMoetWordenVerlegd());
    }
}
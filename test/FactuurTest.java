import org.junit.Test;

import static org.junit.Assert.*;

public class FactuurTest {

    @Test
    public void testTotaalPrijsExclBTWMet5Producten () {
        Klant klant = Main.getKlant(true, Klant.OVERHEID);
        Factuur factuur = new Factuur (klant);
        Main.voegFactuurRegelsToe (factuur);
        double expectedPrijs = 168.41;
        double actualPrijs = factuur.getTotaalPrijsVanFactuur();
        assertEquals(expectedPrijs, actualPrijs, 0.01);
    }
}
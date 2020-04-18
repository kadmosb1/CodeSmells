import org.junit.Test;

import static org.junit.Assert.*;

public class FactuurTest {

    /*
     * Er wordt een nieuwe klant aangemaakt, die aan Factuur meegegeven kan
     * worden.
     */
    private Klant getKlant (boolean buitenNederland) {

        /*
         * De factuur moet gemakkelijk voor zowel een Nederlandse als een Belgische klant opgesteld kunnen
         * worden. Dat gebeurt op basis van de eerste twee letters van het BTW-nummer.
         */
        String btwNummer = "BE 0826882419";

        if (! buitenNederland) {
            btwNummer = "NL 0826882419";
        }

        Adres adres = new Adres ("Johanna Westerdijkplein", 75, "");
        Postcode pc = new Postcode (2521, "EN");
        FactuurAdres factuurAdres = new FactuurAdres (adres, pc, "DEN HAAG");

        return new ConsumentKlant("De Haagse Hogeschool", factuurAdres, btwNummer);
        // return new OverheidKlant("De Haagse Hogeschool", factuurAdres, btwNummer);
        // return new HorecaKlant("De Haagse Hogeschool", factuurAdres, btwNummer);
    }

    private Factuur seedTestSet () {

        Factuur factuur = new Factuur (getKlant(true));

        /*
         * Om de houdbaarheidsdata ook volgend jaar nog te laten werken, worden hieronder
         * relatieve houdbaarheidsdata geïnitialiseerd.
         */
        String vers = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(-6);
        String oud = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(6);
        String bedorven = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(20);

        /*
         * Voor elke regel worden nu producten aangemaakt, waarvoor de gegevens voor
         * aantal producten, producten per verpakking (bijv. 6 flessen in een doos),
         * gewicht en eenheid voor dat gewicht (bijv. kg) apart worden ingesteld.
         */
        Product product = new ProductPerStuk ("Product 1", 2.50, vers);
        FactuurRegel factuurRegel = new FactuurRegel(20, product);
        factuur.addFactuurRegel(factuurRegel);

        product = new ProductPerColli ("Product 2", 10.0, vers, 250);
        factuurRegel = new FactuurRegel(1, product);
        factuur.addFactuurRegel(factuurRegel);

        product = new ProductPerStuk ("Product 3", 0.22, oud);
        factuurRegel = new FactuurRegel(1000, product);
        factuur.addFactuurRegel(factuurRegel);

        ProductPerGewicht productPerGewicht = new ProductPerGewicht ("Product 4", 1.50, oud, 2.55, "kg");
        factuurRegel = new FactuurRegel(productPerGewicht);
        factuur.addFactuurRegel(factuurRegel);

        product = new ProductPerStuk ("Product 5", 0.88, bedorven);
        factuurRegel = new FactuurRegel(1, product);
        factuur.addFactuurRegel(factuurRegel);

        return factuur;
    }

    @Test
    public void testTotaalPrijsExclBTWMet5Producten () {
        Factuur factuur = seedTestSet();
        double expectedPrijs = 168.41;
        double actualPrijs = factuur.getTotaalPrijsVanFactuur();
        assertEquals(expectedPrijs, actualPrijs, 0.01);
    }
}
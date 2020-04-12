import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FactuurRegelTest {

    @Test
    public void testKortingBijHoudbaarheidsdatumMinderDan10DagenOverschreden () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(6);
        Product product = new ProductPerStuk ("Product", 0.88, houdbaarheidsdatum);
        FactuurRegel factuurRegel = new FactuurRegel(1, product);
        double expected = 50.0;
        double actual = new Korting (factuurRegel).bepaalKortingVanwegeHoudbaarheidsdatum();
        assertEquals(expected, actual, 0.1);
    }

    @Test
    public void testKortingBijHoudbaarheidsdatumMinderDan10DagenOverschredenEnMeerDan100Producten () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(6);
        Product product = new ProductPerColli ("Product", 0.88, houdbaarheidsdatum, 6);
        FactuurRegel factuurRegel = new FactuurRegel(20, product);
        double expected = 8.624; // 20 * 0,88 * 0,98 (120 stuks) * 0,5 (vanwege houdbaarheidsdatum)
        double actual = factuurRegel.getTotaalprijs();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testKortingBijHoudbaarheidsdatumMinderDan10DagenOverschredenEnMeerDan1000Producten () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(6);
        Product product = new ProductPerColli ("Product", 0.88, houdbaarheidsdatum, 6);
        FactuurRegel factuurRegel = new FactuurRegel(200, product);
        double expected = 85.36; // 200 * 0,88 * 0,97 (1200 stuks) * 0,5 (vanwege houdbaarheidsdatum)
        double actual = factuurRegel.getTotaalprijs();
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testKortingBijHoudbaarheidsdatumMeerDan10DagenOverschreden () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(11);
        Product product = new ProductPerStuk ("Product", 0.88, houdbaarheidsdatum);
        FactuurRegel factuurRegel = new FactuurRegel(1, product);
        double expected = 100.0;
        double actual = new Korting (factuurRegel).bepaalKortingVanwegeHoudbaarheidsdatum();
        assertEquals(expected, actual, 0.1);
    }

    @Test
    public void testKortingBijNietVerlopenHoudbaarheidsdatum () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(-2);
        Product product = new ProductPerStuk ("Product", 0.88, houdbaarheidsdatum);
        FactuurRegel factuurRegel = new FactuurRegel(1, product);
        double expected = 0.0;
        double actual = new Korting(factuurRegel).bepaalKortingVanwegeHoudbaarheidsdatum();
        assertEquals(expected, actual, 0.1);
    }

    @Test
    public void testKortingBijNietVerlopenHoudbaarheidsdatumEnMeerDan100Producten () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(-2);
        Product product = new ProductPerColli ("Product", 0.88, houdbaarheidsdatum, 6);
        FactuurRegel factuurRegel = new FactuurRegel(20, product);
        double expected = 17.248; // 20 * 0,88 * 0,98 (120 stuks)
        double actual = factuurRegel.getTotaalprijs();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testOfProductPerGewichtWordtGetoond () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(-2);
        ProductPerGewicht product = new ProductPerGewicht ("Product (kg)", 1.00, houdbaarheidsdatum, 2.88, "kg");
        FactuurRegel factuurRegel = new FactuurRegel(product);
        double kortingspercentage = new Korting(factuurRegel).bepaalKortingVanwegeHoudbaarheidsdatum();
        System.out.println (product.getFormattedFactuurRegelString(factuurRegel.getAantalProducten(), kortingspercentage, factuurRegel.getTotaalprijs()));
        assertFalse(factuurRegel.productIsNietGeleverd());
    }

}
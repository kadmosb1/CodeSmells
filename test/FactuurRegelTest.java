import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FactuurRegelTest {

    @Test
    public void testKortingBijHoudbaarheidsdatumMinderDan10DagenOverschreden () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(6);
        Product product = new Product ("Product", 0.88, houdbaarheidsdatum);
        FactuurRegel factuurRegel = new FactuurRegel(product);
        double expected = 50.0;
        double actual = factuurRegel.bepaalKortingVanwegeHoudbaarheidsdatum(new Date(), product.getHoudbaarheidsdatum());
        assertEquals(expected, actual, 0.1);
    }

    @Test
    public void testKortingBijHoudbaarheidsdatumMeerDan10DagenOverschreden () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(11);
        Product product = new Product ("Product", 0.88, houdbaarheidsdatum);
        FactuurRegel factuurRegel = new FactuurRegel(product);
        double expected = 100.0;
        double actual = factuurRegel.bepaalKortingVanwegeHoudbaarheidsdatum(new Date(), product.getHoudbaarheidsdatum());
        assertEquals(expected, actual, 0.1);
    }

    @Test
    public void testKortingBijNietVerlopenHoudbaarheidsdatum () {
        String houdbaarheidsdatum = DatumUtil.getDatumStringMetAantalDagenVoorVandaag(-2);
        Product product = new Product ("Product", 0.88, houdbaarheidsdatum);
        FactuurRegel factuurRegel = new FactuurRegel(product);
        double expected = 0.0;
        double actual = factuurRegel.bepaalKortingVanwegeHoudbaarheidsdatum(new Date(), product.getHoudbaarheidsdatum());
        assertEquals(expected, actual, 0.1);
    }
}
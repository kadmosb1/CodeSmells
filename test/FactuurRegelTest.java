import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FactuurRegelTest {

    @Test
    public void testKortingBijHoudbaarheidsdatum() throws ParseException {
        Date vandaag = new Date ();
        FactuurRegel fr = new FactuurRegel("Product 1", 10.0, "08-04-2020");
        Date houdbaarheidsdatum = DatumUtil.sdf.parse ("08-04-2020");
        long verschilInMillisecondes = Math.abs(vandaag.getTime() - houdbaarheidsdatum.getTime ());
        long verschilInDagen = TimeUnit.DAYS.convert(verschilInMillisecondes, TimeUnit.MILLISECONDS);
        assertEquals(50.0, fr.bepaalKortingVanwegeHoudbaarheidsdatum (vandaag, verschilInDagen, houdbaarheidsdatum), 0.1);
        houdbaarheidsdatum = DatumUtil.sdf.parse ("22-03-2020");
        verschilInMillisecondes = Math.abs(vandaag.getTime() - houdbaarheidsdatum.getTime());
        verschilInDagen = TimeUnit.DAYS.convert(verschilInMillisecondes, TimeUnit.MILLISECONDS);
        assertEquals(100.0, fr.bepaalKortingVanwegeHoudbaarheidsdatum (vandaag, verschilInDagen, houdbaarheidsdatum), 0.1);
        houdbaarheidsdatum = DatumUtil.sdf.parse ("08-05-2021");
        verschilInMillisecondes = Math.abs(vandaag.getTime() - houdbaarheidsdatum.getTime());
        verschilInDagen = TimeUnit.DAYS.convert(verschilInMillisecondes, TimeUnit.MILLISECONDS);
        assertEquals(0.0, fr.bepaalKortingVanwegeHoudbaarheidsdatum (vandaag, verschilInDagen, houdbaarheidsdatum), 0.1);
    }
}
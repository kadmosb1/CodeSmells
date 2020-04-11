import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DatumUtil {
    /*
     * Deze variabele wordt gebruikt om new Date-objects aan te kunnen maken op basis van de gegeven format
     */
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy", new Locale("nl_NL"));

    /*
     * Op basis van een datumstring (in het formaat dd-mm-jjjj) wordt een object
     * van het type Date terug gegeven.
     */
    public static Date getDatum(String datumString) {

        /*
         * Try-catch is een structuur in Java die de volgende stappen toelaat (in dit voorbeeld):
         *
         * - Je probeert de datumString (bijv. "08-12-2021") om te zetten naar een Date-Object. Als dat lukt
         *   wordt de methode gewoon afgesloten.
         * - Als dat niet lukt, wordt de code binnen de catch uitgevoerd (de uitzondering of ParseException wordt
         *   opgevangen. In deze catch wordt de code uitgevoerd die nodig is omdat er in dit geval iets fout is
         *   gegaan bij het parsen van de datumString (er was bijv. "12/08/2021" meegegeven).
         *
         * - De laatste statement wordt nooit bereikt, maar is nodig om ervoor te zorgen dat Java kan zien dat
         *   er altijd een Date-Object wordt teruggegeven (dat is het type van de return-waarde van de method).
         */
        try {
            return sdf.parse(datumString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return new Date();
    }

    /*
     * Met deze methode wordt het verschil in dagen tussen datum1 en datum 2 bepaald (datum1 - datum2)
     */
    public static int getAantalDagenTussenData(Date datum1, Date datum2) {
        long verschilInMillisecondes = Math.abs(datum1.getTime() - datum2.getTime ());
        return (int) TimeUnit.DAYS.convert(verschilInMillisecondes, TimeUnit.MILLISECONDS);
    }

    public static String getDatumStringMetAantalDagenVoorVandaag (int aantalDagen) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date eerdereDatum = new Date(System.currentTimeMillis() - (aantalDagen * DAY_IN_MS));
        return sdf.format(eerdereDatum);
    }
}
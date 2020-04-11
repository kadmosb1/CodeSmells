import java.util.Date;

public class Korting {
    private Product product;
    private int aantalProducten;

    public Korting (FactuurRegel regel) {
        this.product = regel.getProduct ();
        this.aantalProducten = regel.getAantalProducten();
    }

    /*
     * Als een product minder dan 10 dagen over datum is, wordt een korting gegeven van 50%.
     * Vanaf 10 dagen over datum wordt een product gratis weg gegeven.
     */
    protected double bepaalKortingVanwegeHoudbaarheidsdatum () {

        Date vandaag = new Date ();
        Date houdbaarheidsdatum = product.getHoudbaarheidsdatum();
        int verschilInDagen = DatumUtil.getAantalDagenTussenData(vandaag, houdbaarheidsdatum);

        if (vandaag.before (houdbaarheidsdatum)) {
            return 0.0;
        }
        else if (verschilInDagen < 10) {
            return 50.0;
        }
        else {
            return 100.0;
        }
    }

    /*
     * Om 'long method' aan te pakken, is 'replace temp with query' gebruikt. De tijdelijke
     * variabele totaalAantalProducten is verwijderd uit toString en hier als
     * query/methode toegevoegd.
     *
     * Om de korting op basis van verkochte aantallen van een product te kunnen bepalen
     * moet het totaal aantal producten worden bepaald. Als in een doos bijv. 6 flessen
     * zitten, is het totaal aantal verkochte producten bij 11 dozen 66.
     */
    private int getTotaalAantalProducten () {
        return aantalProducten * product.getAantalProductenInVerpakking();
    }

    public double bepaalKortingsPercentageOpBasisvanProduct () {

        double kortingVanwegeAantalProducten = 0.0;
        double kortingVanwegeHoudbaarheidsdatum;

        /*
         * Vanaf 100 stuks van een product geldt een korting van 2%.
         * Vanaf 1.000 stuks geldt een korting van 3%.
         *
         * Deze korting is ook van toepassing als het aantal producten
         * vermenigvuldigd met het aantal producten per verpakking hoger is
         * dan die aantallen. Bij 1 verpakking met 250 stuks wordt met andere
         * woorden ook een korting van 2% gegeven.
         */
        if (getTotaalAantalProducten () >= 1000) {
            kortingVanwegeAantalProducten = 3.0;
        }
        else if (getTotaalAantalProducten () >= 100) {
            kortingVanwegeAantalProducten = 2.0;
        }

        /*
         * Vervolgens wordt op basis van een houdbaarheidsdatum ook nog een korting
         * gegeven (die bovenop de korting komt die hierboven op basis van de
         * aantallen verkochte producten is bepaald). De klant ontvangt 50% korting
         * over een product waarvan de houdbaarheidsdatum minder dan 10 dagen is
         * verlopen. Als de houdbaarheidsdatum meer dan 10 dagen is verlopen,
         * wordt 100% korting gegeven (dan is het product gratis).
         */
        kortingVanwegeHoudbaarheidsdatum = bepaalKortingVanwegeHoudbaarheidsdatum ();

        /*
         * Het totale kortingspercentage wordt berekend.
         */
        return 100.0 - (100.0 - kortingVanwegeAantalProducten) / 100.0 * (100.0 - kortingVanwegeHoudbaarheidsdatum);
    }
}

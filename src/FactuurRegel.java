import java.util.Date;

/*
 * Een factuur bestaat uit een aantal Factuurregels, waar op basis van een aantal
 * producten en kortingsregels een totaalprijs voor die regel bepaald kan worden.
 */
public class FactuurRegel {

    private Product product;
    private int aantalProducten;
    private double kortingspercentage;

    public FactuurRegel (Product product) {
        this.product = product;
        this.kortingspercentage = 0.0;
        this.aantalProducten = 1;
    }

    /*
     * Met deze methode kan een aantal variabelen worden ingesteld:
     *
     * - Als aantal een waarde groter dan 1 heeft, dan wordt het aantal producten
     *   in de factuurregel ingesteld met dit aantal.
     * - Als aantalProductenInVerpakking een waarde groter dan 1 heeft, dan wordt
     *   de omvang van de verpakking (bijv. 6 flessen) voor een product ingesteld.
     *   Als deze variabele een waarde heeft, moet ook een waarde voor aantal
     *   zijn opgegeven.
     * - Als gewicht een waarde heeft (groter dan 0.0), worden gewicht en eenheid
     *   van het product ingesteld (bijv. 1.2 kg).
     *
     * Variabelen mogen alleen in de volgende combinaties voorkomen (dit wordt
     * niet gecontroleerd):
     *
     * - Er wordt alleen een aantal producten opgegeven.
     * - Er worden een aantal producten en een aantalProductenInVerpakking opgegeven.
     * - Er wordt een gewicht met eenheid opgegeven.
     */
    public void setAantal (int aantal, int aantalProductenInVerpakking, double gewicht, String eenheid) {

        if (aantal < 1) {
            this.aantalProducten = 0;
        }
        else if (aantal > 1) {
            this.aantalProducten = aantal;
        }

        if (aantalProductenInVerpakking < 1) {
            this.aantalProducten = 0;
        }
        else if (aantalProductenInVerpakking > 1) {
            product.setAantalProductenInVerpakking(aantalProductenInVerpakking);
        }

        if (gewicht > 0.0) {
            product.setGewicht(gewicht, eenheid);
        }
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

    /*
     * De string waarmee een factuurregel op het scherm getoond kan worden,
     * kan met behulp van de methode toString worden opgevraagd.
     */
    public String toString () {

        double prijsMetKorting;
        double kortingVanwegeAantalProducten = 0.0;
        double kortingVanwegeHoudbaarheidsdatum;

        /*
         * De initiële prijs met korting (bij initialisatie nog zonder korting)
         * wordt als volgt bepaald:
         *
         * - Als een gewicht bekend is, wordt de totaalprijs bepaald op basis
         *   van de eenheidprijs * het gewicht (1,5 kg * € 10/kg = € 15).
         * - Als geen gewicht bekend is, wordt uitgegaan van een stuksprijs en
         *   wordt die eenheidsprijs vermenigvuldigd met het aantal producten
         *   dat in deze factuurregel afgerekend moet worden.
         */
        if (product.getGewicht() > 0.0) {
            prijsMetKorting = product.getTotaalPrijs();
        }
        else {
            prijsMetKorting = product.getEenheidsPrijs() * aantalProducten;
        }

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
            prijsMetKorting *= (100.0 - kortingVanwegeAantalProducten) / 100.0;
        }
        else if (getTotaalAantalProducten () >= 100) {
            kortingVanwegeAantalProducten = 2.0;
            prijsMetKorting *= (100.0 - kortingVanwegeAantalProducten) / 100.0;
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
         * Het totale kortingspercentage en de prijs met korting worden bijgewerkt.
         */
        kortingspercentage = 100.0 - (100.0 - kortingVanwegeAantalProducten) / 100.0 * (100.0 - kortingVanwegeHoudbaarheidsdatum);
        prijsMetKorting *= (100.0 - kortingVanwegeHoudbaarheidsdatum) / 100.0;

        /*
         * Als gewicht van het product onbekend is en als het aantal producten
         * in deze factuurregel 0 is, dan wordt een lege regel terug gegeven.
         * Dat gebeurt ook als de korting vanwege de houdbaarheidsdatum 100% is.
         */
        if ((aantalProducten == 0) && (product.getGewicht() <= 0.0)) {
            return "";
        }
        else if (kortingVanwegeHoudbaarheidsdatum == 100.0) {
            return "";
        }
        /*
         * Een factuurregel voor verschillende combinaties wordt als volgt
         * opgebouwd:
         *
         * - Als het aantal producten per verpakking meer dan 1 is (bijv. bij 6
         *   flessen), dan wordt in de kolom met eenheid vermeld hoeveel
         *   eenheden een verpakking bevat.
         * - Als het gewicht bekend is, wordt gewicht met 1 cijfer achter de komma
         *   getoond en wordt in de kolom eenheid de eenheid getoond (bijv. kg).
         * - Als alleen het aantal producten bekend is, wordt in de kolom met
         *   eenheid 'per stuk' getoond.
         */
        else {
            if (product.getAantalProductenInVerpakking() > 1) {
                return String.format("%6d %-8s %-30s  €%8.2f     %3.0f%%  €%8.2f%n", aantalProducten, "per " + product.getAantalProductenInVerpakking(), product.getNaam(), product.getEenheidsPrijs(), kortingspercentage, prijsMetKorting);
            }
            else if (product.getGewicht() > 0.0) {
                return String.format("%6.1f %-8s %-30s  €%8.2f     %3.0f%%  €%8.2f%n", product.getGewicht(), product.getEenheid(), product.getNaam(), product.getEenheidsPrijs(), kortingspercentage, prijsMetKorting);
            }
            else {
                return String.format("%6d %-8s %-30s  €%8.2f     %3.0f%%  €%8.2f%n", aantalProducten, "per stuk", product.getNaam(), product.getEenheidsPrijs(), kortingspercentage, prijsMetKorting);
            }
        }
    }

    public double getTotaalprijs () {

        double prijsMetKorting;
        double kortingVanwegeAantalProducten = 0.0;
        double kortingVanwegeHoudbaarheidsdatum;

        /*
         * De initiële prijs met korting (bij initialisatie nog zonder korting)
         * wordt als volgt bepaald:
         *
         * - Als een gewicht bekend is, wordt de totaalprijs bepaald op basis
         *   van de eenheidprijs * het gewicht (1,5 kg * € 10/kg = € 15).
         * - Als geen gewicht bekend is, wordt uitgegaan van een stuksprijs en
         *   wordt die eenheidsprijs vermenigvuldigd met het aantal producten
         *   dat in deze factuurregel afgerekend moet worden.
         */
        if (product.getGewicht() > 0.0) {
            prijsMetKorting = product.getTotaalPrijs();
        }
        else {
            prijsMetKorting = product.getEenheidsPrijs() * aantalProducten;
        }

        /*
         * Vanaf 100 stuks van een product geldt een korting van 2%.
         * Vanaf 1.000 stuks geldt een korting van 3%.
         *
         * Deze korting is ook van toepassing als het aantal producten
         * vermenigvuldigd met het aantal producten per verpakking hoger is
         * dan die aantallen. Bij 1 verpakking met 250 stuks wordt met andere
         * woorden ook een korting van 2% gegeven.
         */
        int totaalAantalProducten = aantalProducten * product.getAantalProductenInVerpakking();

        if (totaalAantalProducten >= 1000) {
            kortingVanwegeAantalProducten = 3.0;
            prijsMetKorting *= (100.0 - kortingVanwegeAantalProducten) / 100.0;
        }
        else if (totaalAantalProducten >= 100) {
            kortingVanwegeAantalProducten = 2.0;
            prijsMetKorting *= (100.0 - kortingVanwegeAantalProducten) / 100.0;
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
         * Het totale kortingspercentage en de prijs met korting worden bijgewerkt.
         */
        kortingspercentage = 100.0 - (100.0 - kortingVanwegeAantalProducten) / 100.0 * (100.0 - kortingVanwegeHoudbaarheidsdatum);
        prijsMetKorting *= (100.0 - kortingVanwegeHoudbaarheidsdatum) / 100.0;

        return prijsMetKorting;
    }
}
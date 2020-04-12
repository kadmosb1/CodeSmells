import java.util.Date;

/*
 * Een factuur bestaat uit een aantal Factuurregels, waar op basis van een aantal
 * producten en kortingsregels een totaalprijs voor die regel bepaald kan worden.
 */
public class FactuurRegel {

    private Product product;
    private int aantalProducten;

    public FactuurRegel (Product product) {
        this.product = product;
        this.aantalProducten = 1;
    }

    public Product getProduct () {
        return product;
    }

    public int getAantalProducten () {
        return aantalProducten;
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
     * Deze methode is het resultaat van het verwijderen van de code smell 'duplicate code' in de
     * methods toString en getTotaalprijs (de gemeenschappelijke code is verplaatst naar deze
     * method en naar de method getKortingspercentage hieronder.
     */
    public double getPrijsZonderKorting () {

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
            return product.getTotaalPrijs();
        }
        else {
            return product.getEenheidsPrijs() * aantalProducten;
        }
    }

    /*
     * Deze methode is het resultaat van het verwijderen van de code smell 'duplicate code' in de
     * methods toString en getTotaalprijs (de gemeenschappelijke code is verplaatst naar deze
     * method en naar de method getKortingspercentage
     */
    public double getKortingspercentage () {
        Korting korting = new Korting (this);
        return korting.bepaalKortingsPercentageOpBasisvanProduct();
    }

    /*
     * Deze methode is het resultaat van de aanpak 'Decompose Conditional' om de code smell 'long method'
     * te bestrijden.
     * 
     * Voordat een factuurregel op het scherm wordt getoond wordt nog gecontroleerd of er eigenlijk
     * wel een product wordt geleverd.
     */
    private boolean productIsNietGeleverd () {
        return (aantalProducten == 0) && (product.getGewicht() <= 0.0);
    }

    private boolean isLegeRegel (double kortingspercentage) {
        return productIsNietGeleverd() || (Math.abs(kortingspercentage - 100.0) < 0.01);
    }

    /*
     * De string waarmee een factuurregel op het scherm getoond kan worden,
     * kan met behulp van de methode toString worden opgevraagd.
     */
    public String toString () {

        /*
         * Eerst wordt de prijs zonder korting bepaald.
         */
        double prijsMetKorting = getPrijsZonderKorting();
        double kortingspercentage = getKortingspercentage();
        prijsMetKorting *= (100.0 - kortingspercentage) / 100.0;

        /*
         * Als gewicht van het product onbekend is en als het aantal producten
         * in deze factuurregel 0 is, dan wordt een lege regel terug gegeven.
         * Dat gebeurt ook als de korting vanwege de houdbaarheidsdatum 100% is.
         */
        if (isLegeRegel (kortingspercentage)) {
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
        return getPrijsZonderKorting() * (100.0 - getKortingspercentage ()) / 100.0;
    }
}
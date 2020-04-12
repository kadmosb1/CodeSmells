import java.util.Date;

/*
 * Een factuur bestaat uit een aantal Factuurregels, waar op basis van een aantal
 * producten en kortingsregels een totaalprijs voor die regel bepaald kan worden.
 */
public class FactuurRegel {

    private Product product;
    private int aantalProducten;

    public FactuurRegel (int aantalProducten, Product product) {
        this.product = product;
        this.aantalProducten = aantalProducten;
    }

    public FactuurRegel (ProductPerGewicht product) {
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
     * Deze methode is het resultaat van het verwijderen van de code smell 'duplicate code' in de
     * methods toString en getTotaalprijs (de gemeenschappelijke code is verplaatst naar deze
     * method en naar de method getKortingspercentage hieronder.
     */
    public double getPrijsZonderKorting () {
        return aantalProducten * product.getTotaalPrijs();
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
    protected boolean productIsNietGeleverd () {
        return aantalProducten == 0;
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
            return product.getFormattedFactuurRegelString(aantalProducten, kortingspercentage, prijsMetKorting);
        }
    }

    public double getTotaalprijs () {
        return getPrijsZonderKorting() * (100.0 - getKortingspercentage ()) / 100.0;
    }
}
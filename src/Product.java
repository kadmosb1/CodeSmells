import java.util.Date;

public abstract class Product {

    private String naam;
    private double eenheidsprijs;
    private Date houdbaarheidsdatum;

    public Product (String naam, double eenheidsprijs, String houdbaarheidsdatum) {
        this.naam = naam;
        this.eenheidsprijs = eenheidsprijs;
        this.houdbaarheidsdatum = DatumUtil.getDatum (houdbaarheidsdatum);
    }

    public String getNaam () {
        return naam;
    }

    public double getEenheidsprijs () {
        return eenheidsprijs;
    }

    public Date getHoudbaarheidsdatum () {
        return houdbaarheidsdatum;
    }

    /*
     * De prijs voor het product wordt in subclasses van Product berekend.
     */
    public abstract double getTotaalPrijs ();

    /*
     * Elk soort product wordt op een eigen manier als factuurregel getoond waarvan het format
     * in de subclasses van Product wordt bepaald.
     */
    public abstract String getFormattedFactuurRegelString (int aantalProducten, double kortingspercentage, double prijsMetKorting);

    /*
     * Om de korting op basis van aantal producten te kunnen bepalen, moet een abstract methode
     * getAantalProductenPerVerpakking worden toegevoegd aan Product die in de subclasses
     * wordt overschreven.
     */
    public abstract int getAantalProductenPerVerpakking ();
}
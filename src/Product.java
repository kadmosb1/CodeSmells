import java.util.Date;

public class Product {

    private String naam;

    /*
     * De eenheidsprijs is de prijs per eenheid:
     * - Bij een gewichtsprijs is dat de prijs per eenheid (bijv. per kg).
     * - Bij een aantal producten per verpakking is dat de prijs per verpakking (bijv. per doos van 6 flessen).
     */
    private double eenheidsprijs;
    private Date houdbaarheidsdatum;
    private int aantalProductenInVerpakking;
    private double gewicht;
    private String eenheid;

    public Product (String naam, double prijsPerStukOfKilo, String houdbaarheidsdatum) {
        this.naam = naam;
        this.eenheidsprijs = prijsPerStukOfKilo;
        this.houdbaarheidsdatum = DatumUtil.getDatum (houdbaarheidsdatum);
        this.aantalProductenInVerpakking = 1;
        this.gewicht = 0.0;
    }

    public String getNaam () {
        return naam;
    }

    public double getEenheidsPrijs () {
        return eenheidsprijs;
    }

    /*
     * Als een gewicht voor een product is gegeven, wordt de totaalprijs
     * bepaald op basis van het gewicht vermenigvuldigd met de eenheidsprijs.
     */
    public double getTotaalPrijs () {

        if (gewicht > 0.0) {
            return gewicht * eenheidsprijs;
        }

        else return eenheidsprijs;
    }

    public Date getHoudbaarheidsdatum () {
        return houdbaarheidsdatum;
    }

    public void setAantalProductenInVerpakking (int aantal) {
        this.aantalProductenInVerpakking = aantal;
    }

    public int getAantalProductenInVerpakking () {
        return aantalProductenInVerpakking;
    }

    /*
     * Bij een gewicht moet altijd de eenheid bij dat gewicht worden opgegeven
     * (gram of kg).
     */
    public void setGewicht (double gewicht, String eenheid) {
        this.gewicht = gewicht;
        this.eenheid = eenheid;
    }

    public double getGewicht () {
        return gewicht;
    }

    public String getEenheid () {
        return eenheid;
    }
}
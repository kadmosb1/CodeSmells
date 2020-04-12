public class ProductPerGewicht extends Product {

    double gewicht;
    String eenheid;

    public ProductPerGewicht (String naam, double prijsPerEenheid, String houdbaarheidsdatum,
                              double gewicht, String eenheid) {
        super (naam, prijsPerEenheid, houdbaarheidsdatum);
        this.gewicht = gewicht;
        this.eenheid = eenheid;
    }

    public double getGewicht () {
        return gewicht;
    }

    public String getEenheid () {
        return eenheid;
    }

    public double getTotaalPrijs () {
        return getEenheidsprijs() * gewicht;
    }

    public String getFormattedFactuurRegelString (int aantalProducten, double kortingspercentage, double prijsMetKorting) {
        return String.format("%6.1f %-8s %-30s  €%8.2f     %3.0f%%  €%8.2f%n", gewicht, eenheid, getNaam(), getEenheidsprijs(), kortingspercentage, prijsMetKorting);
    }

    public int getAantalProductenPerVerpakking () {
        return 0;
    }
}

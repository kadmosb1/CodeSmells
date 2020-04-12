public class ProductPerStuk extends Product {

    public ProductPerStuk (String naam, double prijsPerStuk, String houdbaarheidsdatum) {
        super(naam, prijsPerStuk, houdbaarheidsdatum);
    }

    public double getTotaalPrijs () {
        return getEenheidsprijs();
    }

    public String getFormattedFactuurRegelString (int aantalProducten, double kortingspercentage, double prijsMetKorting) {
        return String.format("%6d %-8s %-30s  €%8.2f     %3.0f%%  €%8.2f%n", aantalProducten, "per stuk", getNaam(), getEenheidsprijs(), kortingspercentage, prijsMetKorting);
    }

    public int getAantalProductenPerVerpakking () {
        return 1;
    }
}
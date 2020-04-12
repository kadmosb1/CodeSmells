public class ProductPerColli extends Product {
    int aantalProductenPerVerpakking;

    public ProductPerColli (String naam, double prijsPerVerpakking, String houdbaarheidsdatum, int aantalProductenPerVerpakking) {
        super(naam, prijsPerVerpakking, houdbaarheidsdatum);
        this.aantalProductenPerVerpakking = aantalProductenPerVerpakking;
    }

    public double getTotaalPrijs () {
        return getEenheidsprijs();
    }

    public int getAantalProductenPerVerpakking () {
        return aantalProductenPerVerpakking;
    }

    public String getFormattedFactuurRegelString (int aantalProducten, double kortingspercentage, double prijsMetKorting) {
        return String.format("%6d %-8s %-30s  €%8.2f     %3.0f%%  €%8.2f%n", aantalProducten, "per " + aantalProductenPerVerpakking, getNaam(), getEenheidsprijs(), kortingspercentage, prijsMetKorting);
    }
}

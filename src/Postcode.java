public class Postcode {
    private int cijfers;
    private String letters;

    public Postcode (int cijfers, String letters) {
        this.cijfers = cijfers;
        this.letters = letters;
    }

    public String toString () {
        return String.format("%4d %s", cijfers, letters);
    }
}

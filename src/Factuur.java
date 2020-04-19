import java.util.ArrayList;

/*
 * Een factuur voor een klant bestaat uit een aantal regels met de onderdelen:
 * - Aantal of gewicht
 * - De gebruikte eenheid (bijv. kg of per stuk)
 * - De prijs per eenheid (bijv. per kg of per stuk).
 * - Een eventuele korting op de prijs.
 * - Het totaalbedrag dat voor het aantal producten (incl. korting) betaald
 *   moet worden.
 */
public class Factuur {

    private Klant klant;

    /*
     * De beperkte set met factuurregels wordt vervangen door een dynamische
     * arraylist waar een onbeperkt aantal factuurregels aan toegevoegd kan worden.
     */
    private ArrayList<FactuurRegel> factuurRegels;

    public Factuur (Klant klant) {
        this.klant = klant;
        factuurRegels = new ArrayList<> ();
    }

    public Klant getKlant () {
        return klant;
    }

    public ArrayList<FactuurRegel> getFactuurRegels () {
        return factuurRegels;
    }

    /*
     * Het toevoegen van factuurregels wordt met 'Extract Method' verplaatst van
     * de methode maakFactuur en aangeroepen vanuit Main. Met deze methode wordt
     * een regel aan de verzameling met FactuurRegels toegevoegd.
     */
    public void addFactuurRegel (FactuurRegel regel) {
        factuurRegels.add(regel);
    }

    public double getTotaalPrijsVanFactuur () {

        double totaalprijs = 0.0;

        for (FactuurRegel regel : factuurRegels) {
            totaalprijs += regel.getTotaalprijs();
        }

        return totaalprijs;
    }
}

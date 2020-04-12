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

    /*
     * Het toevoegen van factuurregels wordt met 'Extract Method' verplaatst van
     * de methode maakFactuur en aangeroepen vanuit Main. Met deze methode wordt
     * een regel aan de verzameling met FactuurRegels toegevoegd.
     */
    public void addFactuurRegel (FactuurRegel regel) {
        factuurRegels.add(regel);
    }

    /*
     * De factuur opent met de gegevens van een klant.
     */
    private void printKlant (String naam, FactuurAdres adres) {
        System.out.format("%s%n%s%n%n", naam, adres);
    }

    /*
     * De gehele factuur wordt op het scherm getoond.
     */
    public void maakFactuur () {

        /*
         * Het totaalbedrag dat een klant moet betalen wordt op 0.0 geïnitialiseerd,
         * zodat alle bedragen (incl. kortingen) bij elkaar opgeteld kunnen worden.
         */
        double totaalprijs = 0.0;

        for (FactuurRegel regel : factuurRegels) {
            totaalprijs += regel.getTotaalprijs();
        }

        /*
         * Bovenaan de factuur worden de gegevens van een klant getoond.
         */
        printKlant(klant.getNaam(), klant.getAdres());

        /*
         * Na een paar witregels wordt eerst de titelregel getoond met daarin
         * de namen boven de kolommen.
         */
        System.out.format ("Aantal Eenheid  %-30s   %8s  %7s   %8s%n", "Naam product", "prijs/st", "korting", "totaal");

        /*
         * Er wordt gecontroleerd of de BTW verlegd moet worden (wij doen dat als
         * de klant niet in Nederland 'woont'. Consequentie van dat verleggen is
         * dat we bij deze klant geen BTW in rekening brengen. Dat doen we uiteraard
         * voor klanten in Nederland wel. Als een klant buiten Nederland 'woont'
         * wordt als eerste regel in de factuur opgenomen dat de BTW wordt verlegd
         * naar de klant in het buitenland (met zijn BTW-nummer).
         */
        if (klant.btwMoetWordenVerlegd()) {
            System.out.format ("%15s BTW wordt verlegd naar %s%n", "", klant.getBTWNummer());
        }

        /*
         * Vervolgens worden de factuurregels geprint met daaronder een streep
         * om zichtbaar te maken dat we de bedragen voor de factuurregels bij
         * elkaar optellen.
         */
        for (FactuurRegel regel : factuurRegels) {
            System.out.print (regel);
        }

        System.out.format ("       %-39s   %8s  %7s  %9s%n", "", "", "", "_________ +");

        /*
         * Als de BTW niet moet worden verlegd (de klant 'woont' in Nederland),
         * Wordt een BTW-regel toegevoegd en wordt opnieuw zichtbaar gemaakt,
         * dat de BTW bij het totaalbedrag van de factuurregels opgeteld moet
         * worden.
         */
        if (!klant.btwMoetWordenVerlegd()) {
            System.out.format ("       %-39s   %17s  €%8.2f%n", "", "Subtotaal", totaalprijs);
            System.out.format ("       %-39s   %8s  %7s  €%8.2f%n", "", "", "21% BTW", 0.21 * totaalprijs);
            System.out.format ("       %-39s   %8s  %7s  %9s%n", "", "", "", "_________ +");
        }

        if (!klant.btwMoetWordenVerlegd()) {
            totaalprijs *= 1.21;
        }

        /*
         * Als een klant onderdeel is van de overheid, dan ontvangt de klant
         * over zijn gehele rekening een korting van 2%.
         */
        double kortingspercentageVanwegeTypeKlant = 0.0;

        if (klant.getTypeKlant() == Klant.OVERHEID) {
            kortingspercentageVanwegeTypeKlant = 2.0;
        }
        else if (klant.getTypeKlant() == Klant.HORECA) {
            kortingspercentageVanwegeTypeKlant = 4.0;
        }

        /*
         * Als de klant geen korting krijgt, volgt hierna de laatste regel van
         * de factuur met het kenmerk "Totaal te betalen". Als wel korting
         * wordt gegeven, is de volgende regel nog maar een subtotaal (de korting
         * moet er nog van af worden getrokken.
         */
        String totaalOfSubtotaal = "Totaal te betalen";

        if (kortingspercentageVanwegeTypeKlant > 0.0) {
            totaalOfSubtotaal = "Subtotaal";
        }

        System.out.format ("       %-39s  %18s  €%8.2f%n", "", totaalOfSubtotaal, totaalprijs);

        /*
         * Als een klant korting ontvangt, worden de laatste regels met de
         * verwerking van deze korting onderaan de factuur toegevoegd.
         */
        if (kortingspercentageVanwegeTypeKlant > 0.0) {
            double korting = kortingspercentageVanwegeTypeKlant / 100.0 * totaalprijs;
            totaalprijs -= korting;

            System.out.format ("       %-39s  %18s  €%8.2f%n", "", String.format ("%2.0f%14s", kortingspercentageVanwegeTypeKlant, "% Klantkorting"), korting);
            System.out.format ("       %-39s   %17s  %9s%n", "", "", "_________ -");
            System.out.format ("       %-39s   %17s  €%8.2f%n", "", "Totaal te betalen", totaalprijs);
        }
    }

    public double getTotaalPrijsVanFactuur () {

        double totaalprijs = 0.0;

        for (FactuurRegel regel : factuurRegels) {
            totaalprijs += regel.getTotaalprijs();
        }

        return totaalprijs;
    }
}

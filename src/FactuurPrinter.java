import java.util.ArrayList;

public class FactuurPrinter {

    Klant klant;
    ArrayList<FactuurRegel> factuurRegels;

    public FactuurPrinter (Factuur factuur) {
        this.klant = factuur.getKlant ();
        this.factuurRegels = factuur.getFactuurRegels ();
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
    public void printFactuur () {

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
         * Door toepassing van Replace Conditional with Polymorphism is een switch-statement (hier
         * in de vorm van een if-then-else) verwijderd.
         *
         * Als een klant onderdeel is van de overheid, dan ontvangt de klant
         * over zijn gehele rekening een korting van 2%.
         */
        double kortingspercentageVanwegeTypeKlant = klant.getKortingspercentage();

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
}

package cellularland1;

/**
 * This is just a static class to contain all the text of help - so that I don't
 * have to put it in a place where it would clutter the code.
 *
 * @author Deas
 */
public class NapovedaText {

    public static String text = "Vítejte ve hře Cellular Land!\n\n"
            + "V této hře je Vaším cílem z omezených informací "
            + "zjistit přechodovou funkci náhodně vygenerovaného celulárního automatu, který běží na hrací ploše.\n\n"
            + "Na začátku hry je celá hrací plocha zakrytá, nejprve je tedy třeba kliknutím odkrýt nějaká políčka, "
            + "kde je potom možné sledovat vývoj automatu. Odkrývání políček stojí malé množství many. "
            + "Interval, po kterém automat mění generace, je určen zvolenou obtížností.\n\n"
            + "Během běhu automatu je možné používat speciální akce - pozastavení hry, nebo restart automatu. "
            + "Při zastavení běhu se rychle vyčerpává mana, ale máte čas pořádně si prohlédnout momentální stav. "
            + "Také je během zastavení možné klikat na buňky - při kliknutí na živou buňku se tato buňka stane mrtvou a naopak. "
            + "Tyto akce ovšem stojí další manu.\n\n"
            + "Dále je možné restartovat automat - tedy přesunout ho do počátečního stavu. Stav odhalení hrací plochy zůstane stejný. "
            + "Tuto akci lze provést pouze s nadpolovičním množstvím many a celá mana bude vynulována.\n\n"
            + "Ve chvíli, kdy si myslíte, že jste přišli na přechodovou funkci, zadejte ji ve formátu S/B do připravených políček.\n\n"
            + "Formát S/B: \"S\" značí seznam počtů živých buněk okolo zkoumané živé buňky, při kterých tato buňka přežije. "
            + "\"B\" značí seznam počtů živých buněk okolo zkoumané mrtvé buňky, při kterých tato buňka ožije. "
            + "Pokud pro živou buňku počet okolních buněk není v \"S\", tato buňka zemře. Analogicky pro mrtvou buňku. "
            + "Příklad korektního zadání přechodové funkce ve formátu S/B je 23/3 - přechodová funkce pro Game of Life.";
}

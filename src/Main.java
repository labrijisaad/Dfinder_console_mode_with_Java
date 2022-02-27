import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // ASCII ART Dfinder logo.
        System.out.println("=============================================================================================================================");
        System.out.println("""

                oooooooooo.       .o88o.  o8o                    .o8                   \s
                `888'   `Y8b      888 `"  `"'                   "888                   \s
                 888      888    o888oo  oooo  ooo. .oo.    .oooo888   .ooooo.  oooo d8b
                 888      888     888    `888  `888P"Y88b  d88' `888  d88' `88b `888""8P
                 888      888     888     888   888   888  888   888  888ooo888  888   \s
                 888     d88'     888     888   888   888  888   888  888    .o  888   \s
                o888bood8P'      o888o   o888o o888o o888o `Y8bod88P" `Y8bod8P' d888b  \s""".indent(24));

        System.out.println();

        System.out.println("""
                             >>>>>>>>>>>> [RECHERCHE APPROXIMATIVE]  [RECHERCHE EXCLUSIVE]  [RECHERCHE MINIMALE] <<<<<<<<<<<<

                =============================================================================================================================
                 [SYSTEM] * Voici les méthodes de recherche prises en charge :
                                1) Recherche approximative. \s
                                2) Recherche exclusive. \s
                                3) Recherche minimale. \s
                =============================================================================================================================
                """
        );
        Scanner keyboard = new Scanner(System.in);
        System.out.println("[SYSTEM] * Au choix de l'une des méthodes mentionnées, vous aurez les explications de son fonctionnement." + "\n"
                + "         * Veuillez choisir la méthode de recherche : [1/2/3]");
        System.out.print("[USER] : ");
        int meth_choice = keyboard.nextInt();
        keyboard.nextLine();

        // Creation de la liste de choix :  choix = [1 ,2 ,3]
        ArrayList<Integer> choix = new ArrayList<>();
        choix.add(1);
        choix.add(2);
        choix.add(3);

        // Boucle de vérification des données fournis par l'utilisateur : s'assurer que le choix de l'utilisateur ∈ {1, 2, 3}, sinon demander à nouveau un choix valide.
        while (!choix.contains(meth_choice)) {
            System.out.println("[SYSTEM] : *** WARNING **** le choix doit être 1,2 ou 3, Réessayer...");
            System.out.print("[USER] : ");
            meth_choice = keyboard.nextInt();
            keyboard.nextLine();
        }
        System.out.println("=============================================================================================================================");

        try {
            switch (meth_choice) { // Choix de la méthode de recherche selon le choix de l'utilisateur.
                case 1:
                    Console_result console_result_approximative = Approximative.run();
                    break;
                case 2:
                    Console_result console_result_exclusive = Exclusive.run();
                    break;
                case 3:
                    Console_result console_result_minimale = Minimale.run();
                    break;
                default:
            }
        } catch (Exception exception) {
            System.out.println("[SYSTEM] : *** WARNING **** Un problème est survenu :/ ");
            System.out.println("         : error type -> " + exception);
        }

        // signature de la fin.
        System.out.println("=============================================================================================================================");
        System.out.println();
        System.out.println("""
                   ___                     \s
                 ( / \\ /)o          /     \s
                  /  ///,  _ _   __/ _  _ \s
                (/\\_///_(_/ / /_(_/_(/_/ (_
                    /)  ISBAINE mohamed                  \s
                   (/   LABRIJI saad                \s""".indent(99));
    }
}

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Approximative extends Exclusive {

    // les attributs de la classe Approximative :
    int user_choice;
    ArrayList<String> ArraySentenceInputUser;

    // le constructeur de la classe Approximative :
    Approximative(String sentenceInputUser, String inputUserPath, int user_choice) {
        super(inputUserPath);
        this.user_choice = user_choice;
        this.ArraySentenceInputUser = sentenceToArray(sentenceInputUser);
    }

    /* la methode .display_sorted_file_names_continuation(SimpleDateFormat sdf, TreeMap<Integer, ArrayList<File>> sorted_tuples) permet  :
     **                             -
     **                             -
     */
    static void display_sorted_file_names_continuation(SimpleDateFormat sdf, TreeMap<Integer, ArrayList<File>> sorted_tuples) throws IOException {  // utile pour l'héritage entre les classes

        System.out.println("         : ( les fichiers sont triés selon un score, date de modification  ");
        System.out.println("         :    la plus récente la taille la plus petite. )");
        Set<Integer> set = Exclusive.cptCounter(sorted_tuples);
        for (Integer key : set) {
            System.out.printf("[Score = %d] : \n", key);
            Exclusive.display_sorted_file_names_continuation2(sdf, key, sorted_tuples);
        }
        System.out.println("=============================================================================================================================");
    }

    /* la methode .explanationScoreCalculation() permet  :
     **                             -
     **                             -
     */
    public static void explanationScoreCalculation() {
        System.out.println("""
                =============================================================================================================================
                 [SYSTEM] *  le calcul de score se fait par deux methodes ( selon le choix de l'utilisateur )\s
                                +  METH 1 : tous les mots ont la même importance! , donc le score est la somme totale \s
                                             du nombre d'occurrences de chaque mot.\s
                                +  METH 2 : les mots les plus à gauche sont plus importants! , donc le score est la somme totale\s
                                             du nombre d'occurrences de chaque mot multiplié par son coefficient d'importance.
                ============================================================================================================================="""
        );
    }

    /* la methode .run() permet  :
     **                             -
     **                             -
     */
    public static Console_result run() throws IOException {
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH;mm;ss");
        Date date = new Date();
        System.out.println("""
                                                  >>>>>>>>>>>> [RECHERCHE APPROXIMATIVE] <<<<<<<<<<<
                =============================================================================================================================
                 [EXPLIATIONS] * Trouve les fichiers qui contiennent les mots de la phrase donnée par l'utilisateur ( pas forcément dans\s
                                  le même ordre de saisie des mots ).
                               * Les fichiers supportés sont ceux d'extension : txt, pdf, doc et docx
                               * La recherche ne prend pas en compte les lettres majuscules et minuscules.
                               * Les fichiers (de toute extension) dont le nom contient les mots recherchés seront affichés en premier.\s
                               * Les fichiers dont le contenu contient les mots recherchés sont triés et\s
                                  affichés selon un score, la date de modification la plus récente et la taille la plus petite.\s
                               * La recherche est effectuée dans l'arborescence du répertoire donné. \s
                               * Lorsque la recherche est terminée, un fichier ( de la forme : output_Approximative[MM-dd-yyyy HH;mm;ss].txt )                    
                                  est créé par défaut dans le dossier History ( ∈ repertoire courant du projet ) \s"""
        );
        explanationScoreCalculation();

        Scanner scanner = new Scanner(System.in);
        System.out.println("[SYSTEM] : Veuillez choisir la methode de calcul du score. [1/2] ");
        System.out.print("[USER] : ");
        int user_choice = scanner.nextInt();
        scanner.nextLine();

        // Creation de la liste de choix :  choices = [1 ,2]
        ArrayList<Integer> choices = new ArrayList<>();
        choices.add(1);
        choices.add(2);

        // Boucle de vérification des données fournis par l'utilisateur : s'assurer que le choix de l'utilisateur ∈ {1, 2}, sinon demander à nouveau un choix valide.
        while (!choices.contains(user_choice)) {
            System.out.println("[SYSTEM] : *** WARNING **** le choix doit être 1 ou 2, Réessayer...");
            System.out.print("[USER] : ");
            user_choice = scanner.nextInt();
            scanner.nextLine();
        }

        // Demander à l'utilisateur de saisir les informations nécessaires.
        System.out.println();
        System.out.println("[SYSTEM] : Donner le chemin du répertoire ");
        System.out.print("[USER] : ");
        String path = scanner.nextLine();
        System.out.println();
        System.out.println("[SYSTEM] : Donnez le/les mot(s) à rechercher :");
        System.out.print("[USER] : ");
        String userInput = scanner.nextLine();
        System.out.println();

        long startTime = System.currentTimeMillis(); // l'instant initial pour le calcul du temps d'exécution.
        Approximative approximative = new Approximative(userInput, path, user_choice);
        approximative.getFile();
        approximative.addFilesToHashTable();

        // Creation du fichier output_Approximative[MM-dd-yyyy HH;mm;ss].txt
        String history_file_name = "output_Approximative" + "[" + sdf.format(date) + "] " + ".txt";
        FileOutputStream file = new FileOutputStream("history-saves\\" + history_file_name);
        TeePrintStream tee = new TeePrintStream(file, System.out);
        System.setOut(tee); // point de départ du remplissage du fichier output_Approximative[MM-dd-yyyy HH;mm;ss].txt
        System.out.println("========== Récapitulatif ====================================================================================================" + "\n"
                + " [SYSTEM] :  la methode de recherche choisie : Recherche Approximative" + "\n"
                + "          :  la methode de calcul du score choisie : METH" + user_choice + "\n"
                + "          :  le chemin du répertoire choisi : " + approximative.inputUserPath + "\n"
                + "          :  le/les mot(s) à rechercher : " + userInput
        );
        Exclusive.finalDisplayMethode(scanner, startTime, approximative, history_file_name); // Héritage de la methode : finalDisplayMethode
        console_result = new Console_result(approximative.inputUserPath, userInput, approximative.inputInName, approximative.sorted_tuples, approximative.user_choice);
        return console_result;
    }

    /* la methode .getFile() permet  :
     **                             -
     **                             -
     */
    public void getFile() {
        File directory = new File(inputUserPath);
        Scanner scanner = new Scanner(System.in); // scanner pour demander à nouveau le lien à l'utilisateur en cas d'invalidité du l'ancien lien fourni.
        while (!directory.isDirectory()) {
            System.out.println("[SYSTEM] : Ce n'est pas un répertoire valide !, assurez-vous de saisir le lien d'un répertoire valide !");
            System.out.print("[USER] : ");
            inputUserPath = scanner.next();
            directory = new File(inputUserPath);
        }
        scanner.close();
        System.out.println("[SYSTEM] : Recherche en cours...  ");
        get_all_files(directory, repFile);
    }

    /* la methode .sentenceToArray(String str) permet de :
     **                             - transformer une phrase en mots
     **                             - ajouter les mots obtenus dans une Arraylist<String> puis retourner cette Arraylist
     */
    ArrayList<String> sentenceToArray(String str) {
        str = str.toLowerCase();
        str.split("[ *|,.:/!?+]+");
        String[] words = str.split("\\s+");
        return new ArrayList<>(Arrays.asList(words));
    }

    /* la methode .occNumber_sentence(ArrayList<String> inputUser,ArrayList<String> fileContent) permet de :
     **                             - retourne  la somme totale du nombre d'occurence de chaque mot de *inputUser* dans *fileContent*
     */
    public int occNumber_sentence(ArrayList<String> inputUser, ArrayList<String> fileContent, int user_choice) {
        int cpt = 0;
        if (user_choice == 1) {
            for (String word : inputUser) {
                cpt += Collections.frequency(fileContent, word);
            }
        } else {
            for (String word : inputUser) {
                cpt = cpt + Collections.frequency(fileContent, word) * (inputUser.size() - (inputUser.indexOf(word) + 1) + 1);
            }
        }
        return cpt;
    }

    /* la methode .checkFileName(File file) permet de :
     **                             - vérifie si *fileArrayName* ( nom du fichier sous forme d'Arraylist ) contient  *ArraySentenceInputUser*
     **                                ( phrase donnée par l'utilisateur sous forme d'Arraylist ), l'ordre n'est pas pris en considération.
     */
    public Boolean checkFileName(File file) {
        String fileName;
        String name = file.getName();
        fileName = name.replaceFirst("[.][^.]+$", "");
        ArrayList<String> fileArrayName = this.sentenceToArray(fileName.toLowerCase());

        int index = fileName.lastIndexOf('.');
        String extension = null;
        if (index > 0) extension = fileName.substring(index + 1);
        fileArrayName.add(extension);
        return fileArrayName.containsAll(this.ArraySentenceInputUser);
    }

    /* la methode .containThisArray(ArrayList<String> array1,ArrayList<String> array2) permet  :
     **                             -
     **                             -
     */
    public boolean containThisArray(ArrayList<String> array1, ArrayList<String> array2) {
        for (String word : array1) {
            if (!array2.contains(word)) return false;
        }
        return true;
    }

    /* la methode .addFilesToHashTable() permet  :
     **                             -
     **                             -
     */
    public void addFilesToHashTable() throws IOException {
        for (File file : this.repFile) {
            this.occNumberFile(file);
        }
    }

    /* la methode .occNumberFile(File file) permet  :
     **                             -
     **                             -
     */
    public void occNumberFile(File file) throws IOException {
        if (checkFileName(file)) {
            this.inputInName.add(file);
        } else {
            ArrayList<String> fileContent = new ArrayList<>();
            List<String> lines = new ArrayList<>();

            if (this.is_file_extension(file, "txt")) { // txt file
                String fileName = file.toString();
                lines = Files.readAllLines(Paths.get(fileName), Charset.forName("CP1251"));
            } else if (this.is_file_extension(file, "pdf")) { // pdf file
                PDDocument document = PDDocument.load(file);
                PDFTextStripper stripper = new PDFTextStripper(); // stripper s'occupe de récupérer le texte dans le fichier pdf.
                String text = stripper.getText(document); // *text* contient le contenu du fichier pdf
                document.close();
                lines = new ArrayList<>(Arrays.asList(text.split(",")));
            } else if (this.is_file_extension(file, "docx") || this.is_file_extension(file, "doc")) { // doc docx files
                FileInputStream fileInputStream = new FileInputStream(file);
                XWPFDocument document = new XWPFDocument(fileInputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                String content = extractor.getText();
                document.close();
                fileInputStream.close();
                lines = new ArrayList<>(Arrays.asList(content.split(",")));
            }
            for (String s : lines) {
                for (String w : s.split("\\s+")) {
                    w = w.replaceAll("\\p{Punct}", "");
                    fileContent.add(w.toLowerCase());
                }
            }
            int occurrence = this.occNumber_sentence(this.ArraySentenceInputUser, fileContent, user_choice);
            if (containThisArray(this.ArraySentenceInputUser, fileContent)) { // Si le
                if (tuples.containsKey(occurrence)) {  // on ajoute le path du fichier et le nombre d'occurence au hashmap tuples
                    tuples.get(occurrence).add(file);
                } else {
                    ArrayList<File> newFile = new ArrayList<>();
                    newFile.add(file);
                    tuples.put(occurrence, newFile);
                }
            }
        }
    }

    /* la methode .display_file_names() permet  :
     **                             -
     **                             -
     */
    public void display_file_names() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("=============================================================================================================================");
        if (!this.inputInName.isEmpty()) {
            System.out.println("[SYSTEM] : Voici les fichiers dont le nom contient les mots donnés ( ordre non pris en considération )");
            System.out.println("         :  ( Total = " + inputInName.size() + " )");
            System.out.println();
            for (int i = 0; i < inputInName.size(); i++) {
                System.out.printf("   [file N°%d] : ", i + 1);
                System.out.print("   file name : " + inputInName.get(i).getName() + "\n                " + "\n" +
                        "   last modification date : " + sdf.format(inputInName.get(i).lastModified()) + "\n                " +
                        "   file path : " + inputInName.get(i) + "\n                ");
                Path path = Paths.get(String.valueOf(inputInName.get(i)));
                long size = Files.size(path);
                System.out.print("   file size : " + size + " octets");
                System.out.println("\n");
            }
        } else {
            System.out.println("[SYSTEM] : Aucun nom de fichier contient les mots donnés !");
        }
        System.out.println("=============================================================================================================================");
    }

    /* la methode .display_sorted_file_names() permet  :
     **                             -
     **                             -
     */
    public void display_sorted_file_names() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.sorted_tuples = sortTuples(this.tuples);
        if (!this.sorted_tuples.isEmpty()) {
            System.out.println("[SYSTEM] : Voici les fichiers qui contiennent les mots donnés :");
            display_sorted_file_names_continuation(sdf, sorted_tuples);
        } else {
            System.out.println("[SYSTEM] : Aucun fichier contient les mots donnés !");
        }
        System.out.println("=============================================================================================================================");
    }
}



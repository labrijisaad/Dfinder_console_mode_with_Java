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
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Minimale extends Approximative {

    //Attributs de la classe:
    HashMap<Integer, ArrayList<File>> InputInName2 = new HashMap<>(); // va contenir le score des titres
    TreeMap<Integer, ArrayList<File>> sorted_InputInNAme = new TreeMap<>(); //va contenire les scores tri�s par ordre décroissant:

    //Constructeur de la classe Minimale:
    Minimale(String sentenceInputUser, String inputUserPath, int user_choice) {
        super(sentenceInputUser, inputUserPath, user_choice);
    }

    //Méthodes de la classes Minimale:
    // checkfileName2 est une redefinition de CheckFileName de la classe Approximative,elle permet de tester si le nom du fichier contient au moins un mot de la phrase recherchée

    public static Console_result run() throws IOException {
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH;mm;ss");
        Date date = new Date();
        System.out.println("""
                                                  >>>>>>>>>>>> [RECHERCHE Minimale] <<<<<<<<<<<
                =============================================================================================================================
                 [EXPLIATIONS] * Trouve les fichiers qui contiennent au moins un mot la phrase donnée par l'utilisateur.\s
                               * Les fichiers supportés sont ceux d'extension : txt, pdf, doc et docx
                               * La recherche ne prend pas en compte les lettres majuscules et minuscules.
                               * Les fichiers (de toute extension) dont le nom contient au moins l'un des mots recherchés seront triés et affichés en premier                   
                                 selon un score, la date de modification la plus récente et la taille la plus petite.\s
                               * Les fichiers dont le contenu contient au moins l'un des mots recherchés recherchés sont triés et\s
                                  affichés selon un score, la date de modification la plus récente et la taille la plus petite.\s
                               * La recherche est effectuée dans l'arborescence du répertoire donné. \s
                               * Lorsque la recherche est terminée, un fichier ( de la forme : output_Minimale[MM-dd-yyyy HH;mm;ss].txt ) \s
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

        long startTime = System.currentTimeMillis();  // l'instant initial pour le calcul du temps d'exécution.
        Minimale minimale = new Minimale(userInput, path, user_choice);
        minimale.getFile();
        minimale.addFilesToHashTable();

        // Creation du fichier output_Minimale[MM-dd-yyyy HH;mm;ss].txt
        String history_file_name = "output_Minimale" + "[" + sdf.format(date) + "] " + ".txt";
        FileOutputStream file = new FileOutputStream("history-saves\\" + history_file_name);
        TeePrintStream tee = new TeePrintStream(file, System.out);
        System.setOut(tee); // point de départ du remplissage du fichier output_Minimale[MM-dd-yyyy HH;mm;ss].txt
        System.out.println("========== Récapitulatif ====================================================================================================" + "\n"
                + " [SYSTEM] :  la methode de recherche choisie : Recherche Minimale" + "\n"
                + "          :  la methode de calcul du score choisie : METH" + user_choice + "\n"
                + "          :  le chemin du répertoire choisi : " + minimale.inputUserPath + "\n"
                + "          :  le/les mot(s) à rechercher : " + userInput + "\n"
        );
        Exclusive.finalDisplayMethode(scanner, startTime, minimale, history_file_name); // Héritage de la methode : finalDisplayMethode
        console_result = new Console_result(minimale.inputUserPath, userInput, minimale.inputInName, minimale.sorted_tuples, minimale.user_choice);
        return console_result;
    }

    public Integer checkFileName2(File file) {
        // convert the file name into string
        String fileName1 = file.getName();
        fileName1 = fileName1.replaceFirst("[.][^.]+$", "");
        ArrayList<String> fileArrayName = sentenceToArray(fileName1.toLowerCase());

        String fileName2 = file.getName();
        int index = fileName2.lastIndexOf('.');
        String extension = null;
        if (index > 0) extension = fileName2.substring(index + 1);
        fileArrayName.add(extension);

        return occNumber_sentence(this.ArraySentenceInputUser, fileArrayName, user_choice);
    }

    public void occNumberFile(File file) throws IOException {
        if (checkFileName2(file) > 0) {
            int score = checkFileName2(file);
            if (this.InputInName2.containsKey(score)) {  //on ajoute le path du fichier et le nombre d'occurence au hashmap tuples
                InputInName2.get(score).add(file);
            } else {
                ArrayList<File> temp = new ArrayList<>();
                temp.add(file);
                this.InputInName2.put(score, temp);
            }
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
            } else if (this.is_file_extension(file, "docx") || this.is_file_extension(file, "doc")) { // doc docx file
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
            if (occurrence > 0) { // Si le
                if (tuples.containsKey(occurrence)) {  //on ajoute le path du fichier et le nombre d'occurence au hashmap tuples
                    tuples.get(occurrence).add(file);
                } else {
                    ArrayList<File> newFile = new ArrayList<>();
                    newFile.add(file);
                    tuples.put(occurrence, newFile);
                }
            }
        }
    }

    public void display_file_names() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.sorted_InputInNAme = sortTuples(this.InputInName2);
        if (!this.sorted_InputInNAme.isEmpty()) {
            System.out.println("[SYSTEM] : Voici les fichiers dont le nom contient au moins un mot de la phrase saisie :");
            Approximative.display_sorted_file_names_continuation(sdf, sorted_InputInNAme);
        } else {
            System.out.println("[SYSTEM] : Aucun fichier contient les mots donnés !");
        }
        System.out.println("=============================================================================================================================");
    }
}

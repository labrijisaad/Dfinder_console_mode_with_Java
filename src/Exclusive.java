import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Exclusive {
    public static Console_result console_result;
    // les attributs de la classe Exclusive :
    String inputUserPath;
    HashMap<Integer, ArrayList<File>> tuples = new HashMap<>();
    TreeMap<Integer, ArrayList<File>> sorted_tuples = new TreeMap<>();
    ArrayList<File> inputInName = new ArrayList<>();
    ArrayList<File> repFile = new ArrayList<>();

    // le constructeur de la classe Exclusive :
    Exclusive(String inputUserPath) {
        this.inputUserPath = inputUserPath;
    }

    /* la methode .fileDescription(SimpleDateFormat sdf, ArrayList<File> inputInName) permet  :
     **                             -
     **                             -
     */
    static void fileDescription(SimpleDateFormat sdf, ArrayList<File> inputInName) throws IOException {
        for (int i = 0; i < inputInName.size(); i++) {
            System.out.printf("   [file N°%d] : ", i + 1);
            System.out.print("   file name : " + inputInName.get(i).getName() + "\n                ");
            System.out.print("   last modification date : " + sdf.format(inputInName.get(i).lastModified()) + "\n                ");
            System.out.print("   file path : " + inputInName.get(i) + "\n                ");
            Path path = Paths.get(String.valueOf(inputInName.get(i)));
            long size = Files.size(path);
            System.out.print("   file size : " + size + " octets");
            System.out.println("\n");
        }
    }

    /* la methode .cptCounter(TreeMap<Integer, ArrayList<File>> sorted_tuples) permet  :
     **                             -
     **                             -
     */
    static Set<Integer> cptCounter(TreeMap<Integer, ArrayList<File>> sorted_tuples) {
        Set<Integer> keySet = sorted_tuples.descendingKeySet();
        int cpt = 0;
        for (Integer key : keySet) {
            cpt += sorted_tuples.get(key).size();
        }
        System.out.println("         : ( Total = " + cpt + " )");
        System.out.println();
        return keySet;
    }

    /* la methode .sortDate(ArrayList<File> FileAyantMemeOcc) permet  :
     **                             - utile pour l'héritage entre les classes
     **                             -
     */
    static void display_sorted_file_names_continuation2(SimpleDateFormat sdf, Integer key, TreeMap<Integer, ArrayList<File>> sorted_tuples) throws IOException {
        for (int i = 0; i < sorted_tuples.get(key).size(); i++) {
            System.out.printf("   [file N°%d] : ", i + 1);
            System.out.print("   file name : " + sorted_tuples.get(key).get(i).getName() + "\n                ");
            System.out.print("   last modification date : " + sdf.format(sorted_tuples.get(key).get(i).lastModified()) + "\n                ");
            System.out.print("   file path : " + sorted_tuples.get(key).get(i) + "\n                ");
            Path path = Paths.get(String.valueOf(sorted_tuples.get(key).get(i)));
            long size = Files.size(path);
            System.out.print("   file size : " + size + " octets");
            System.out.println("\n");
        }
    }

    /* la methode .sortDate(ArrayList<File> FileAyantMemeOcc) permet  :
     **                             -
     **                             -
     */
    public static ArrayList<File> sortDate(ArrayList<File> FileAyantMemeOcc) {
        if (FileAyantMemeOcc.size() > 1) {
            HashMap<Long, ArrayList<File>> tt = new HashMap<>();
            for (File temp : FileAyantMemeOcc) {
                if (tt.containsKey(temp.lastModified())) {
                    tt.get(temp.lastModified()).add(temp);
                } else {
                    ArrayList<File> intern = new ArrayList<>(1);
                    intern.add(temp);
                    tt.put(temp.lastModified(), intern);
                }
            }
            TreeMap<Long, ArrayList<File>> sorted = new TreeMap<>(tt);
            ArrayList<File> sortedFiles = new ArrayList<>();
            Set<Long> keySet = sorted.descendingKeySet();
            for (Long key : keySet) {
                sortedFiles.addAll(sortSize(sorted.get(key)));
            }
            return sortedFiles;
        }
        return FileAyantMemeOcc;
    }

    /* la methode .sortSize(ArrayList<File> FileAyantMemeDate) permet  :
     **                             -
     **                             -
     */
    public static ArrayList<File> sortSize(ArrayList<File> FileAyantMemeDate) {
        if (FileAyantMemeDate.size() > 1) {
            HashMap<Long, ArrayList<File>> ttt = new HashMap<>();
            ArrayList<File> sortedArray = new ArrayList<>();

            for (File temp : FileAyantMemeDate) {
                if (ttt.containsKey(temp.length())) {
                    ttt.get(temp.length()).add(temp);
                } else {
                    ArrayList<File> intern1 = new ArrayList<>(1);
                    intern1.add(temp);
                    ttt.put(temp.length(), intern1);
                }
            }
            TreeMap<Long, ArrayList<File>> sorted_tt = new TreeMap<>(ttt);
            Collection<ArrayList<File>> sortedDate = sorted_tt.values();
            for (Object files : sortedDate) {
                sortedArray.addAll((Collection<? extends File>) files);
            }
            return sortedArray;
        }
        return FileAyantMemeDate;
    }

    /* la methode .run() permet  :
     **                             -
     **                             -
     */
    public static Console_result run() throws IOException {
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH;mm;ss");
        Date date = new Date();
        System.out.println("""
                                                    >>>>>>>>>>>> [RECHERCHE EXCLUSIVE] <<<<<<<<<<<
                ===========================================================================================================================
                 [EXPLIATIONS] * Recherche les fichiers contenant la phrase saisie par l'utilisateur telle quelle.
                               * Les fichiers supportés sont ceux d'extension : txt, pdf, doc et docx
                               * La recherche ne prend pas en compte les lettres majuscules et minuscules.
                               * Les fichiers (de toute extension) dont le nom contient la phrase donnée telle quelle seront affichés en premier.\s
                               * Les fichiers dont le contenu contient la phrase donnée telle quelle sont triés et affichés selon le\s
                                   nombre d'occurence, la date de modification la plus récente et la taille la plus petite.\s
                               * La recherche est effectuée dans l'arborescence du répertoire donné. \s
                               * Lorsque la recherche est terminée, un fichier ( de la forme : output_Exclusive[MM-dd-yyyy HH;mm;ss].txt ) \s
                                  est créé par défaut dans le dossier history-saves ( ∈ repertoire courant du projet ) \s
                ===========================================================================================================================
                """
        );

        // Demander à l'utilisateur de saisir les informations nécessaires.
        Scanner scanner = new Scanner(System.in);
        System.out.println("[SYSTEM] : Donner le chemin du répertoire :");
        System.out.print("[USER] : ");
        String path = scanner.nextLine();
        System.out.println("[SYSTEM] : Donnez la phrase à rechercher : ");
        System.out.print("[USER] : ");
        String userInput = scanner.nextLine();
        long startTime = System.currentTimeMillis(); // l'instant initial pour le calcul du temps d'exécution.
        Exclusive exclusive = new Exclusive(path);
        exclusive.getFile();
        exclusive.searchFiles(userInput);

        // Creation du fichier output_Exclusive[MM-dd-yyyy HH;mm;ss].txt
        String history_file_name = "output_Exclusive" + "[" + sdf.format(date) + "] " + ".txt";

        File directory = new File("history-saves");
        if (!directory.exists() || !directory.isDirectory()) Files.createDirectory(Path.of(directory.getPath()));
        FileOutputStream file = new FileOutputStream("history-saves\\" + history_file_name);
        TeePrintStream tee = new TeePrintStream(file, System.out);
        System.setOut(tee); // point de départ du remplissage du fichier output_Exclusive[MM-dd-yyyy HH;mm;ss].txt
        System.out.println("========== Récapitulatif ====================================================================================================" + "\n"
                + " [SYSTEM] :  la methode de recherche choisie : Recherche Exclusive" + "\n"
                + "          :  le chemin du répertoire choisi : " + exclusive.inputUserPath + "\n"
                + "          :  la phrase à rechercher : " + userInput
        );
        finalDisplayMethode(scanner, startTime, exclusive, history_file_name);
        console_result = new Console_result(exclusive.inputUserPath, userInput, exclusive.inputInName, exclusive.sorted_tuples);
        return console_result;
    }

    /* la methode .finalDisplayMethode(Scanner scanner, long startTime, Exclusive exclusive, String history_file_name) permet  :
     **                             -
     **                             -
     */
    static void finalDisplayMethode(Scanner scanner, long startTime, Exclusive exclusive, String history_file_name) throws IOException { // utile pour l'héritage entre les classes
        System.out.println("=============================================================================================================================");
        exclusive.display_file_names();
        exclusive.display_sorted_file_names();
        long endTime = System.currentTimeMillis(); // l'instant final pour le calcul du temps d'exécution.
        scanner.close();
        System.out.println("[SYSTEM] * Temps d'exécution : " + (endTime - startTime) + " Millisecondes." + "\n"
                + "[SYSTEM] * fin ! ( le fichier d'historique de recherche " + history_file_name + " a été enregistré avec succès )"
        );
    }

    /* la methode .OccNumber(String text ,String userInput) permet  :
     **                             -
     **                             -
     */
    public int OccNumber(String text, String userInput) {
        int position;
        int nbOcc = 0;
        String texte1 = text.toLowerCase();
        while ((texte1.contains(userInput.toLowerCase())) && (!texte1.equals(""))) {
            position = texte1.indexOf(userInput);
            texte1 = texte1.substring(position + userInput.length());
            nbOcc++;
        }
        return nbOcc;
    }

    /* la methode .getFile() permet  :
     **                             -
     **                             -
     */
    public void getFile() {
        File dir = new File(this.inputUserPath);
        Scanner scanner = new Scanner(System.in); // scanner pour demander à nouveau le lien à l'utilisateur en cas d'invalidité du l'ancien lien fourni.
        while (!this.isDirectory()) {
            System.out.print("[USER] : ");
            this.inputUserPath = scanner.next();
            dir = new File(this.inputUserPath);
        }
        scanner.close();
        get_all_files(dir, this.repFile);
    }

    /* la methode .get_all_files(File directory, ArrayList<File> arrayFile) permet  :
     **                             -
     **                             -
     */
    public void get_all_files(File directory, ArrayList<File> arrayFile) {
        ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
        for (File file : files) {
            if (file.isDirectory()) {
                get_all_files(file, arrayFile); // Calls same method again.
            } else {
                arrayFile.add(file);
            }
        }
    }

    /* la methode .isDirectory() permet  :
     **                             -
     **                             -
     */
    public boolean isDirectory() {
        File dir = new File(this.inputUserPath);
        if (!dir.isDirectory()) {
            System.out.println("[SYSTEM] : Ce n'est pas un répertoire !, assurez-vous de saisir le lien d'un répertoire valide !");
            return false;
        } else {
            System.out.println("[SYSTEM] : Recherche en cours...  ");
            return true;
        }
    }

    /* la methode .checkFileName(File fichier,String userInput) permet  :
     **                             -
     **                             -
     */
    public Boolean checkFileName(File fichier, String userInput) {
        String Filename = fichier.getName().toLowerCase();
        return Filename.contains(userInput.toLowerCase());
    }

    /* la methode .is_file_extension(File file,String ext) permet  :
     **                             -
     **                             -
     */
    public boolean is_file_extension(File file, String ext) {
        String fileName = file.toString();
        int index = fileName.lastIndexOf('.');
        String extension = null;
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return Objects.equals(extension, ext);
    }

    /* la methode .searchFiles(String userInput) permet  :
     **                             -
     **                             -
     */
    public void searchFiles(String userInput) throws IOException {
        for (File file : this.repFile) {
            if (this.is_file_extension(file, "txt")) { // txt files
                if (checkFileName(file, userInput)) {
                    this.inputInName.add(file);
                } //si le nom du fichier contient la phrase recherchée, on l'ajoute directement à FileNames
                else {
                    String fileName = file.toString();
                    long size = Files.size(Path.of(fileName)); // taille du fichier en octets.
                    char[] array = new char[(int) (8 * size)]; // array est fléxible par rapport a la taille du fichier.
                    try {
                        FileReader fichier = new FileReader(file.getPath()); // Creation d'un objet FileReader.
                        BufferedReader input = new BufferedReader(fichier); // Creation d'un objet BufferedReader.
                        input.read(array); // lecture des caractères.
                        String content = String.valueOf(array); // Convertit le tableau des caractères en chaîne de caractères.
                        add_file(userInput, file, content);
                        input.close(); // Ferme reader.
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }

            } else if (this.is_file_extension(file, "pdf")) { // pdf files
                try {
                    if (checkFileName(file, userInput)) {
                        this.inputInName.add(file);
                    } //si le nom du fichier contient la phrase recherchée, on l'ajoute directement à FileNames
                    else {
                        PDDocument document = PDDocument.load(file);
                        if (!document.isEncrypted()) { // le fichier ne doit pas être chiffré.
                            PDFTextStripper stripper = new PDFTextStripper(); // stripper s'occupe de récupérer le texte dans le fichier pdf.
                            String text = stripper.getText(document); // *text* contient le contenu du fichier pdf
                            add_file(userInput, file, text);
                        }
                        document.close();
                    }
                } catch (Exception ignored) {
                }
            } else if (this.is_file_extension(file, "docx") || this.is_file_extension(file, "doc")) {  // doc / docx files
                try {
                    if (checkFileName(file, userInput)) {
                        this.inputInName.add(file);
                    } // si le nom du fichier contient la phrase recherchée, on l'ajoute directement à FileNames
                    else {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        XWPFDocument document = new XWPFDocument(fileInputStream);
                        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                        String content = extractor.getText();
                        document.close();
                        fileInputStream.close();
                        add_file(userInput, file, content);
                    }
                } catch (Exception ignored) {
                }
            } else {
                if (checkFileName(file, userInput)) {
                    this.inputInName.add(file);
                } // si le nom du fichier contient la phrase recherchée, on l'ajoute directement à FileNames
            }
        }
        this.sorted_tuples = sortTuples(this.tuples);    // Nous trions le tuple par ordre décroissant
    }

    /* la methode .add_file(String userInput, File file, String content) permet  :
     **                             -
     **                             -
     */
    public void add_file(String userInput, File file, String content) {
        int occurrence = OccNumber(content, userInput);
        if (occurrence != 0) { // Si le fichier contient : *userInput*, on le garde !
            if (tuples.containsKey(occurrence)) {  //on ajoute le path du fichier et le nombre d'occurence au hashmap tuples
                tuples.get(occurrence).add(file);
            } else {
                ArrayList<File> newFile = new ArrayList<>();
                newFile.add(file);
                tuples.put(occurrence, newFile);
            }
        }
    }

    /* la methode .sortTuples(HashMap<Integer,ArrayList<File>> Map) permet  :
     **                             -
     **                             -
     */
    public TreeMap<Integer, ArrayList<File>> sortTuples(HashMap<Integer, ArrayList<File>> Map) {
        TreeMap<Integer, ArrayList<File>> sortedMap = new TreeMap<>(Map);
        Set<Integer> keySet = sortedMap.keySet();
        for (Integer key : keySet) {
            ArrayList<File> temp = sortDate(sortedMap.get(key));
            sortedMap.put(key, temp);
        }
        return sortedMap;
    }

    /* la methode .display_file_names() permet  :
     **                             -
     **                             -
     */
    public void display_file_names() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (!this.inputInName.isEmpty()) {
            System.out.println("=============================================================================================================================" + "\n"
                    + "[SYSTEM] : Voici les fichiers dont le nom contient la phrase donnée telle quelle:" + "\n"
                    + "         :  ( Total = " + inputInName.size() + " )" + "\n"
            );
            fileDescription(sdf, inputInName);
            System.out.println("=============================================================================================================================");
        } else
            System.out.println("""
                    =============================================================================================================================
                    [SYSTEM] : Aucun nom de fichier contient la phrase donnée telle quelle !
                    =============================================================================================================================
                    """
            );
    }

    /* la methode .display_sorted_file_names() permet  :
     **                             - d'afficher fichiers dont le contenu contient la phrase donnée telle quelle ( affiche *this.fileNames* )
     **                             - ( les fichiers sont triés par nombre d'occurence de la phrase donnée, date modification la plus récente la taille la plus petite. )
     */
    public void display_sorted_file_names() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (!this.sorted_tuples.isEmpty()) {
            System.out.println("[SYSTEM] : Voici les fichiers qui contiennent la phrase donnée telle quelle :");
            System.out.println("         : ( les fichiers sont triés par nombre d'occurence de la phrase donnée, ");
            System.out.println("         :    date de modification la plus récente la taille la plus petite. )");
            Set<Integer> set = cptCounter(sorted_tuples);
            for (Integer key : set) {
                System.out.printf("[Occurence = %d] : \n", key);
                display_sorted_file_names_continuation2(sdf, key, sorted_tuples);
            }
        } else {
            System.out.println("=============================================================================================================================");
            System.out.println("[SYSTEM] : Aucun fichier contient la phrase donnée telle quelle !");

        }
        System.out.println("=============================================================================================================================");
    }
}
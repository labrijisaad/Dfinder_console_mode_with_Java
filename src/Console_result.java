import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class Console_result {
    String inputUserPath;
    String userInput;
    ArrayList<File> inputInName;
    TreeMap<Integer, ArrayList<File>> sorted_tuples;
    int user_choice;

    // Constructor for Exclusive
    Console_result(String inputUserPath, String userInput, ArrayList<File> inputInName, TreeMap<Integer, ArrayList<File>> sorted_tuples) {
        this.inputUserPath = inputUserPath;
        this.inputInName = inputInName;
        this.userInput = userInput;
        this.sorted_tuples = sorted_tuples;
    }

    // Constructor for Approximative & Minimale
    Console_result(String inputUserPath, String userInput, ArrayList<File> inputInName, TreeMap<Integer, ArrayList<File>> sorted_tuples, int user_choice) {
        this.inputUserPath = inputUserPath;
        this.inputInName = inputInName;
        this.userInput = userInput;
        this.sorted_tuples = sorted_tuples;
        this.user_choice = user_choice;
    }
}

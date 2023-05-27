import java.io.File;
import java.util.Scanner;

public class Dispatcher{
    public static void main(String[] args) {

        System.out.println("Choose action \n1 - Clean \n2 - Count \n3 - Rename folder \n4 - Archive folder \n5 - Folder size");

        Scanner scanner = new Scanner(System.in);
        int method = scanner.nextInt();

        System.out.println("Enter path to folder: ");

        Scanner scanner1 = new Scanner(System.in);
        String folderPath = scanner1.nextLine();

        if (FileMethods.fileOrFolderExists(folderPath)) {
            File folder = new File(folderPath);
            Controller.toDo(folder, method);
        }
    }
}
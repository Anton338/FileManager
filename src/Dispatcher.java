import java.io.File;
import java.util.Scanner;

class Dispatcher{
    public static void main(String[] args) {

        System.out.println("Choose action \n1 - Clean \n2 - Count");

        Scanner scanner = new Scanner(System.in);
        int method = scanner.nextInt();

        System.out.println("Enter path to folder");

        Scanner scanner1 = new Scanner(System.in);
        String folderPath = scanner1.nextLine();

        if (FileMethods.fileOrFolderExists(folderPath)) {
            File folder = new File(folderPath);
            Controller.toDo(folder, method);
        }
    }
}
class Controller{

    static public void toDo(File f, int i) {
        if(i == 1) {
            FileMethods.cleanFolderFromFiles(f);
        }else if(i == 2) {
            FileMethods.listFilesForFolder(f);
        }  else {
            System.out.println("number " + i + " out of the range");
        }
    }
}

class FileAdditionalCommands{

    public static int fileSize(File folder) {

        int result = 0;

        for (File fileEntry : folder.listFiles()) {
            result++;
        }
        return result;
    }
}

class FileMethods {

    private static int amount = 0;
    private static String mainFolderName = "";

    static public void listFilesForFolder(File folder) {

        if (mainFolderName.equals("")) {
            mainFolderName = folder.getName();
        }

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                amount++;
            }
        }
        if (folder.getName().equals(mainFolderName)) {
            System.out.println("Folder " + mainFolderName + " have: " + amount + " files");
        }
    }

    static public void cleanFolderFromFiles(File folder) {

        int deletes = 0;
        int expectedDeletes = FileAdditionalCommands.fileSize(folder);

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.delete()) {
                deletes++;
            }
        }
        if (deletes == 0) {
            System.out.println("Folder is empty");

        } else {
            if (deletes == expectedDeletes) {
                System.out.println("Successfully deleted " + deletes + " files");
            } else {
                System.out.println("Something went Wrong");
                System.out.println("File xpected to delete " + expectedDeletes + "\nWas deleted " + deletes);
            }
        }

    }

    public static boolean fileOrFolderExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            System.out.println("File or folder does not exist.");
            return false;
        }
    }
}
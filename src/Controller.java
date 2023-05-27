import java.io.File;
import java.util.Scanner;

public class Controller {
    static public void toDo(File f, int i) {
        if(i == 1) {
            FileMethods.cleanFolderFromFiles(f);
        }else if(i == 2) {
            FileMethods.listFilesForFolder(f);
        } else if (i==3) {
            renameFolderWithInput(f);
        } else if (i==4) {
            createZipArchive(f);
        } else if (i==5) {
            FileMethods.getWesFolder(f);
        }else {
            System.out.println("number " + i + " out of the range");
        }
    }
    static public void renameFolderWithInput(File f){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter old folder name: ");
        String oldFolderName = scanner.nextLine().toLowerCase();

        System.out.println("Enter new folder name: ");
        String newFolderName = scanner.nextLine();

        FileMethods.renameFoldersInFolder(f, oldFolderName, newFolderName);
    }
    public static int fileSize(File folder) {

        int result = 0;

        for (File fileEntry : folder.listFiles()) {
            result++;
        }
        return result;
    }
    public static void createZipArchive(File f) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter directory path to save the archive: ");
        String saveDirectory = scanner.nextLine();

        System.out.println("Enter archive file name (without extension): ");
        String archiveFileName = scanner.nextLine();

        String archiveFilePath = saveDirectory + File.separator + archiveFileName + ".zip";
        FileMethods.archiveFolder(f, archiveFilePath);
    }

}

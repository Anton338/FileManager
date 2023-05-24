import java.io.File;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Dispatcher{
    public static void main(String[] args) {

        System.out.println("Choose action \n1 - Clean \n2 - Count \n3 - Rename folder \n4 - Archive folder");

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
        } else if (i==3) {
            renameFolderWithInput(f);
        } else if (i==4) {
            createZipArchive(f);
        } else {
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
        int expectedDeletes = Controller.fileSize(folder);

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
                System.out.println("File expected to delete " + expectedDeletes + "\nWas deleted " + deletes);
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

    static public void renameFoldersInFolder(File folder, String oldFolderName, String newFolderName) {
        int renamedFolders = 0;

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() && fileEntry.getName().equals(oldFolderName)) {
                File renamedFolder = new File(fileEntry.getParent() + File.separator + newFolderName);
                if (fileEntry.renameTo(renamedFolder)) {
                    renamedFolders++;
                } else {
                    System.out.println("Failed to rename folder: " + fileEntry.getName());
                }
            }
        }
        if (renamedFolders > 0) {
            System.out.println("Renamed " + renamedFolders + " folders successfully");
        } else {
            System.out.println("No folders were renamed");
        }
    }
    static public void archiveFolder(File folder, String archiveFileName) {
        try {
            FileOutputStream fos = new FileOutputStream(archiveFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            addFolderToArchive("", folder, zos);

            zos.close();
            fos.close();
            System.out.println("Folder archived successfully");

        } catch (IOException e) {
            System.out.println("Error occurred during archiving: " + e.getMessage());
        }
    }

    private static void addFolderToArchive(String parentPath, File folder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addFolderToArchive(parentPath + file.getName() + File.separator, file, zos);
            } else {
                FileInputStream fis = new FileInputStream(file);
                String entryPath = parentPath + file.getName();
                ZipEntry zipEntry = new ZipEntry(entryPath);
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, bytesRead);
                }

                zos.closeEntry();
                fis.close();
            }
        }
    }
}
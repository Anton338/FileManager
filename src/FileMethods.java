import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileMethods {
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
    static public void getWesFolder(File folder){
        long fileSizeInBytes = calculateFolderSize(folder);
        System.out.println("Folder size: " + fileSizeInBytes + " bytes");
    }
    private static long calculateFolderSize(File folder){
        long size =0;
        if(folder.isDirectory()){
            File[] files= folder.listFiles();
            if(files !=null){
                for(File file : files){
                    if(file.isFile()){
                        size +=  file.length();
                    }else if(file.isDirectory()){
                        size += calculateFolderSize(file);
                    }
                }
            }
        }
        return size;
    }
}

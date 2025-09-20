// 代码生成时间: 2025-09-20 19:22:52
 * It includes error handling and follows Java best practices for maintainability and scalability.
 */

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BatchRenameTool {

    /**
     * Renames all files in the specified directory with a new name pattern.
     *
     * @param directoryPath the path to the directory containing files to rename
     * @param newNamePattern the pattern for the new file names, e.g., "file_#.ext"
     * @return a list of successfully renamed files
     * @throws Exception if an error occurs during the renaming process
     */
    public List<String> renameFiles(String directoryPath, String newNamePattern) throws Exception {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        List<String> renamedFiles = new ArrayList<>();
        int fileIndex = 1;

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String newFileName = String.format(newNamePattern, fileIndex++) + getFileExtension(file);
                File newFile = new File(directory, newFileName);
                try {
                    Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    renamedFiles.add(newFile.getName());
                } catch (Exception e) {
                    System.err.println("Failed to rename file: " + file.getName() + ". Reason: " + e.getMessage());
                }
            }
        }

        return renamedFiles;
    }

    /**
     * Extracts the file extension from a file.
     *
     * @param file the file from which to extract the extension
     * @return the file extension, e.g., ".txt"
     */
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }

    /**
     * Main method to test the batch renaming functionality.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            BatchRenameTool tool = new BatchRenameTool();
            List<String> renamedFiles = tool.renameFiles("./path/to/directory", "file_%d.txt");
            System.out.println("Renamed files: " + renamedFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

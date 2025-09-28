// 代码生成时间: 2025-09-29 00:00:41
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;

public class FileBackupAndSyncTool {

    // Define the source and destination directories
    private static final String SOURCE_DIR = "/path/to/source";
    private static final String DESTINATION_DIR = "/path/to/destination";

    // Define the Javalin app
    public static Javalin app = Javalin.create().start(7000);

    // Main method to run the application
    public static void main(String[] args) {
        // Define API routes
        app.routes(() -> {
            // Route to initiate file backup and synchronization
            get("/backup", ctx -> {
                try {
                    backupAndSyncFiles();
                    ctx.status(200).result("Backup and sync completed successfully.");
                } catch (IOException e) {
                    ctx.status(500).result("Error during backup and sync: " + e.getMessage());
                }
            });
        });
    }

    // Method to backup and synchronize files
    private static void backupAndSyncFiles() throws IOException {
        // Check if source and destination directories exist
        if (!Files.exists(Paths.get(SOURCE_DIR))) {
            throw new IOException("Source directory does not exist.");
        }

        if (!Files.exists(Paths.get(DESTINATION_DIR))) {
            Files.createDirectories(Paths.get(DESTINATION_DIR));
        }

        // Create a new directory in destination for each backup
        String backupDirName = "backup_" + new Date().getTime();
        Path backupDirPath = Paths.get(DESTINATION_DIR, backupDirName);
        Files.createDirectories(backupDirPath);

        // Walk through the source directory and copy files to destination
        Files.walkFileTree(Paths.get(SOURCE_DIR), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = backupDirPath.resolve(SOURCE_DIR.substring(SOURCE_DIR.lastIndexOf(File.separator) + 1) + dir.toString().substring(SOURCE_DIR.length()));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = backupDirPath.resolve(SOURCE_DIR.substring(SOURCE_DIR.lastIndexOf(File.separator) + 1) + file.toString().substring(SOURCE_DIR.length()));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

        // Sync files by removing files in destination that do not exist in source
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(DESTINATION_DIR), "*")) {
            for (Path path : stream) {
                Path relativePath = path.toAbsolutePath().normalize()
                        .subpath(DESTINATION_DIR.length() + 1, path.toAbsolutePath()
                                .length());
                Path targetPath = Paths.get(SOURCE_DIR, relativePath.toString());

                if (!Files.exists(targetPath)) {
                    Files.delete(path);
                }
            }
        }
    }
}

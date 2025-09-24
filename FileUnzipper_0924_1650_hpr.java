// 代码生成时间: 2025-09-24 16:50:47
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipFile;

public class FileUnzipper {

    private static final String ZIP_MIME_TYPE = "application/zip";
    private static final String TAR_MIME_TYPE = "application/x-tar";
    private static final String GZIP_MIME_TYPE = "application/gzip";

    public static void unzipFile(Context ctx) {
        try {
            String filePath = ctx.pathParam("filePath");
            String destination = ctx.pathParam("destination");
            Path sourcePath = Paths.get(filePath);
            Path destinationPath = Paths.get(destination);

            if (!Files.exists(sourcePath)) {
                ctx.status(404).result("File not found");
                return;
            }

            if (ZIP_MIME_TYPE.equals(ctx.contentType())) {
                unzipZip(sourcePath, destinationPath);
            } else if (TAR_MIME_TYPE.equals(ctx.contentType())) {
                unzipTar(sourcePath, destinationPath);
            } else if (GZIP_MIME_TYPE.equals(ctx.contentType())) {
                unzipGzip(sourcePath, destinationPath);
            } else {
                ctx.status(400).result("Unsupported file type");
            }

            ctx.status(200).result("File unzipped successfully");
        } catch (IOException | ArchiveException e) {
            ctx.status(500).result("Error unzipping file: " + e.getMessage());
        }
    }

    private static void unzipZip(Path sourcePath, Path destinationPath) throws IOException, ArchiveException {
        try (ZipFile zipFile = new ZipFile(sourcePath.toFile())) {
            zipFile.stream().forEach(entry -> {
                try {
                    Files.copy(zipFile.getInputStream(entry), destinationPath.resolve(entry.getName()));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private static void unzipTar(Path sourcePath, Path destinationPath) throws IOException, ArchiveException {
        try (TarArchiveInputStream tarInput = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", sourcePath.toFile())) {
            ArchiveEntry entry;
            while ((entry = tarInput.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    Files.createDirectories(destinationPath.resolve(entry.getName()));
                } else {
                    Files.copy(tarInput, destinationPath.resolve(entry.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static void unzipGzip(Path sourcePath, Path destinationPath) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(sourcePath.toFile()));
             FileOutputStream fileOutputStream = new FileOutputStream(destinationPath.toFile())) {
            IOUtils.copy(gzipInputStream, fileOutputStream);
        }
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.post("/unzip/:filePath/:destination", FileUnzipper::unzipFile);
    }
}

// 代码生成时间: 2025-10-02 03:18:27
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentConverter {

    private static final Logger log = LoggerFactory.getLogger(DocumentConverter.class);
    private static final int PORT = 7000; // Port number for Javalin server
    private static final String DOCX_TO_PDF_ROUTE = "/convert/docx";
    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final String PDF_DIRECTORY = "pdfs";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);
        app.routes(() -> {
            // POST route to convert a DOCX file to PDF
            ApiBuilder.post(DOCX_TO_PDF_ROUTE, DocumentConverter::handleDocxToPdf);
        });
        log.info("Server started on port {}", PORT);
    }

    private static void handleDocxToPdf(Context ctx) {
        try {
            // Check if the uploaded file is present
            if (ctx.uploadedFiles().isEmpty()) {
                ctx.status(400).result("No file uploaded.");
                return;
            }

            // Get the uploaded file
            String uploadedFile = ctx.uploadedFiles().get(0).getFileName();
            File file = new File(UPLOAD_DIRECTORY + File.separator + uploadedFile);
            ctx.uploadedFiles().get(0).writeTo(file);

            // Convert DOCX to PDF and save it to the PDF directory
            String pdfFileName = uploadedFile.replace(".docx", ".pdf");
            File pdfFile = new File(PDF_DIRECTORY + File.separator + pdfFileName);
            convertDocxToPdf(file, pdfFile);

            // Send the PDF file as a response
            ctx.result(new FileInputStream(pdfFile));
            ctx.contentType("application/pdf");

        } catch (IOException | InvalidFormatException e) {
            ctx.status(500).result("Error converting file: " + e.getMessage());
            log.error("Error converting file", e);
        }
    }

    private static void convertDocxToPdf(File docxFile, File pdfFile) throws IOException, InvalidFormatException {
        XWPFDocument document = new XWPFDocument(Files.newInputStream(docxFile.toPath()));
        try (OutputStream pdfOutputStream = new FileOutputStream(pdfFile)) {
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, pdfOutputStream, options);
        }
        document.close();
    }
}

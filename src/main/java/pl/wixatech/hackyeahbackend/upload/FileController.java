package pl.wixatech.hackyeahbackend.upload;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@RestController
@RequiredArgsConstructor
public class FileController {
    
    private final DocumentService documentService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public UploadFileResponse uploadFile(@RequestPart("file") MultipartFile file) {

        //TODO validacja czy to jest wgl pdf
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream())) {

            File fileToSave = new File(file.getName());

            copyInputStreamToFileJava9(inputStream, fileToSave);

            documentService.saveDocument(null, fileToSave.getAbsolutePath());
        } catch (IOException e) {
            out.println(e.getMessage());
            return new UploadFileResponse(false);
        }

        return new UploadFileResponse(true);
    }
    private static void copyInputStreamToFileJava9(InputStream input, File file)
            throws IOException {

        // append = false
        try (OutputStream output = new FileOutputStream(file, false)) {
            input.transferTo(output);
        }

    }

    @GetMapping(path = "/document/{id}")
    public String getFile (@PathVariable Long id){
        Document byId = documentService.getById(id);
        File file = new File(byId.getFilePath());
        return file.getName();
    }

}

//    ContentHandler textHandler;
//    Metadata metadata;

//            textHandler = new BodyContentHandler();
//            metadata = new Metadata();
//            PDFParser parser = new PDFParser();
//            ParseContext context = new ParseContext();
//            parser.parse(inputStream, textHandler, metadata, context);

//        out.println("Title: " + metadata.get("title"));
//        out.println("Author: " + metadata.get("Author"));
//        documentService.saveDocument(metadata.get("dc:title"));

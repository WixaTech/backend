package pl.wixatech.hackyeahbackend.upload;

import lombok.Data;
import lombok.RequiredArgsConstructor;
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
import pl.wixatech.hackyeahbackend.document.DocumentService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.out;

@RestController
@RequiredArgsConstructor
public class FileController {
    
    private final DocumentService documentService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public UploadFileResponse uploadFile(@RequestPart("file") MultipartFile file) {
        ContentHandler textHandler;
        Metadata metadata;
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            textHandler = new BodyContentHandler();
            metadata = new Metadata();
            PDFParser parser = new PDFParser();
            ParseContext context = new ParseContext();
            parser.parse(inputStream, textHandler, metadata, context);
        } catch (IOException | TikaException | SAXException e) {
            return new UploadFileResponse(false);
        }

        out.println("Title: " + metadata.get("title"));
        out.println("Author: " + metadata.get("Author"));
        documentService.saveDocument(metadata.get("dc:title"));
        
        return new UploadFileResponse(true);
    }

//    @GetMapping("/download/{id}")
//    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
//        //TODO find file by id
//
//        return ResponseEntity
//                .ok()
//                .contentLength(taskInstruction.getContent().length)
//                .contentType(MediaType.asMediaType(taskInstruction.getMimeType()))
//                .body(taskInstruction.getContent());
//    }

}

package pl.wixatech.hackyeahbackend.metadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileMetadataUpdaterService {
    private final DocumentService documentService;

    public void updateMetadata(Document document) {
        try (PDDocument doc = findPdDocument(document)) {
            if (doc == null) {
                log.error("Document is null");
                return;
            }

            PDDocumentInformation info = doc.getDocumentInformation();
            if (info == null) {
                info = new PDDocumentInformation();
            }

            info.setCustomMetadataValue("addressee_name", "test");
            info.setCustomMetadataValue("addressee_surname", "test");
            info.setCustomMetadataValue("addressee_street", "test");
            info.setCustomMetadataValue("addressee_house_number", "test");
            info.setCustomMetadataValue("addressee_local_number", "test");
            info.setCustomMetadataValue("addressee_post_code", "test");
            info.setCustomMetadataValue("addressee_city", "test");
            info.setCustomMetadataValue("addressee_country", "test");

            info.setCustomMetadataValue("sender_name", "test");
            info.setCustomMetadataValue("sender_surname", "test");
            info.setCustomMetadataValue("sender_street", "test");
            info.setCustomMetadataValue("sender_house_number", "test");
            info.setCustomMetadataValue("sender_local_number", "test");
            info.setCustomMetadataValue("sender_post_code", "test");
            info.setCustomMetadataValue("sender_city", "test");
            info.setCustomMetadataValue("sender_country", "test");

            info.setCustomMetadataValue("unp", "test");
            info.setCustomMetadataValue("case_number", "test");
            info.setCustomMetadataValue("signature_data", "test");

            doc.save(document.getFilePath());
            log.info("Metadata added to file: {}", document.getFilePath());

        } catch (IOException e) {
            documentService.error(document);
            throw new RuntimeException(e);
        }
    }

    private PDDocument findPdDocument(Document document) {
        PDDocument doc = null;
        File file = new File(document.getFilePath());
        try (InputStream inputStream = new FileInputStream(file)) {

            doc = PDDocument.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
}

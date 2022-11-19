package pl.wixatech.hackyeahbackend.document;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void saveDocument(final String contentType, String filePath) {
        documentRepository.save(new Document(contentType, filePath));
    }

    @Transactional(readOnly = true)
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getById(long l) {
        return documentRepository.findById(l).orElseThrow();
    }

    public List<Document> getAllNewDocuments() {
        return documentRepository.findAllByDocumentStatus(DocumentStatus.NEW);
    }

    @Transactional
    public void documentInProgress(Document document) {
        document.setParseStartAt(LocalDateTime.now());
        document.setDocumentStatus(DocumentStatus.IN_PROGRESS);
    }

    @Transactional
    public void validationFailed(Document document) {
        document.setDocumentStatus(DocumentStatus.VALIDATION_FAIL);
    }

    @Transactional
    public void completed(Document document) {
        document.setDocumentStatus(DocumentStatus.COMPLETED);
    }
}

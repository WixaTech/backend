package pl.wixatech.hackyeahbackend.document;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DocumentService {
  
  private final DocumentRepository documentRepository;

  public DocumentService(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
  }

  @Transactional
  public void saveDocument(final String contentType) {
    documentRepository.save(new Document(contentType));
  }
  
  @Transactional(readOnly = true)
  public List<Document> getAllDocuments() {
    return documentRepository.findAll();
  }
}

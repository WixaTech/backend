package pl.wixatech.hackyeahbackend.document;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wixatech.hackyeahbackend.validation.report.Report;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentRestController {

  private final DocumentService documentService;

  @GetMapping
  public List<Document> getAllDocuments() {
    return documentService.getAllDocuments();
  }

  @GetMapping(value = "/{documentId}/recentReport")
  public Report getRecentReport(@PathVariable("documentId") Long documentId) {
    return documentService.getRecentReport(documentId);
  }

}

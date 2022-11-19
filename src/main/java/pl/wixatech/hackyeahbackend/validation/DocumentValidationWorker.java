package pl.wixatech.hackyeahbackend.validation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;
import pl.wixatech.hackyeahbackend.document.DocumentStatus;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentValidationWorker {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10, new CustomizableThreadFactory("workerThread"));

    private final DocumentService documentService;
    private final ValidatorEngineService validatorEngineService;

    @Scheduled(fixedDelay = 1000)
    public void execute() {
        log.info("Checking if there is document to parse");
        List<Document> allNewDocuments = documentService.getAllNewDocuments();
        if (allNewDocuments.isEmpty()) {
            log.debug("No new documents to parse");
            return;
        }
        allNewDocuments.forEach(document -> {
            documentService.documentInProgress(document);
            executorService.execute(() -> validatorEngineService.execute(document));
        });
    }

    private void parseDocument(Document document) {
//        List<PDPage> pages;
//        PDDocument doc = PDDocument.load(inputStream);
//        out.println(doc.getDocumentId());
//        doc.close();
//        log.debug("Parsing");
    }
}

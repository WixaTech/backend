package pl.wixatech.hackyeahbackend.validation;


import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;

@Service
@Slf4j
public class DocumentValidationWorker {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10, new CustomizableThreadFactory("workerThread"));

    @Scheduled(fixedDelay = 1000)
    public void execute() {
        log.info("Checking if there is document to parse");
        executorService.execute(() -> parseDocument());
    }

    private void parseDocument() {
//        List<PDPage> pages;
//        PDDocument doc = PDDocument.load(inputStream);
//        out.println(doc.getDocumentId());
//        doc.close();
        log.debug("Parsing");
    }
}

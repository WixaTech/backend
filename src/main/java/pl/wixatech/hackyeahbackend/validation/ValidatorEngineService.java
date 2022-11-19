package pl.wixatech.hackyeahbackend.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPlugin;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidatorEngineService {
    private final List<ValidationPlugin> validationPluginList;
    private final List<ValidationPluginWithInput> validationWithDocPluginList;
    private final DocumentService documentService;
    public void execute(Document document) {
        List<ValidationResult> validationResults = validationPluginList.stream()
                .sorted(Comparator.comparing(ValidationPlugin::getPriority))
                .map(validationPlugin -> validationPlugin.validate(document))
                .collect(Collectors.toList());

        List<ValidationResult> validationResults1;
        try (PDDocument doc = findPdDocument(document)) {
            if (doc == null) {
                log.error("Document is null");
            }

            validationResults1 = validationWithDocPluginList.stream()
                    .sorted(Comparator.comparing(ValidationPluginWithInput::getPriority))
                    .map(validationPluginWithInput -> validationPluginWithInput.validate(doc))
                    .toList();
            validationResults.addAll(validationResults1);

        } catch (IOException e) {
            documentService.error(document);
            throw new RuntimeException(e);
        }


        documentService.addReportToDocument(document, validationResults);
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

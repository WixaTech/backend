package pl.wixatech.hackyeahbackend.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPlugin;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidatorEngineService {
    private final List<ValidationPlugin> validationPluginList;
    private final DocumentService documentService;
    public void execute(Document document) {
        List<ValidationResult> validationResults = validationPluginList.stream()
                .sorted(Comparator.comparing(ValidationPlugin::getPriority))
                .map(validationPlugin -> validationPlugin.validate(document))
                .toList();

        boolean validationFailed = validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
        if (validationFailed) {
            documentService.validationFailed(document);
            //TODO create report with errors
        } else {
            documentService.completed(document);
        }
    }
}

package pl.wixatech.hackyeahbackend.validation.plugin.document;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FileContentValidationPlugin implements ValidationPluginWithInput {

    public static String GROUP_NAME = "fileContent";

    // TODO: figure out extracting date
    public static final List<String> EXPECTED_CONTENT =
        List.of("UNP", "Sprawa", "Znak sprawy", "Kontakt", "Załączniki", "Korenspondencję otrzymują");

    @Override
    public ValidationResult validate(PDDocument doc) {
//        StreamSupport.stream(doc.getPage(0).getContentStreams(), false)

        return ValidationResult.builder()
            .valid(true)
            .groupName(GROUP_NAME)
            .messageErrors(List.of())
            .build();
    }

}

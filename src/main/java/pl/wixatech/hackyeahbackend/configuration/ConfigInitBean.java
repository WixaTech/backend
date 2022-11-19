package pl.wixatech.hackyeahbackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.wixatech.hackyeahbackend.validation.plugin.FileNameValidationPlugin;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigInitBean {
    private final ConfigService configService;
    private final ObjectMapper objectMapper;

    @Transactional
    @PostConstruct
    public void init() {
//        ConfigValidation filename = new ConfigValidation();
//        filename.setConfigMap(Map.of("maxsize", "100"));
//        filename.setValidationGroup("filename");
//        configService.add(filename);
//
//        ConfigValidation configValidation = new ConfigValidation();
//        configValidation.setConfigMap(Map.of(
//                "name", "Aerial",
//                "size", "10"
//                )
//        );
//        configValidation.setValidationGroup("font");
//
//        configService.add(configValidation);

        fileNameParams();
        fileParams();
        technicalParams();


    }

    @SneakyThrows
    private void fileParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("type")
                .content(objectMapper.writeValueAsString("pdf"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("format")
                .content(objectMapper.writeValueAsString("A4"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("orientation")
                .content(objectMapper.writeValueAsString("VERTICAL"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("pdf_version")
                .content(objectMapper.writeValueAsString(List.of("A-2", "A-4")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());

        validations.add(ValidationField.builder()
                .keyName("margin_top")
                .content(objectMapper.writeValueAsString("10"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("margin_bottom")
                .content(objectMapper.writeValueAsString("8"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("margin_left")
                .content(objectMapper.writeValueAsString("15"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("margin_right")
                .content(objectMapper.writeValueAsString("15"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());

        validations.add(ValidationField.builder()
                .keyName("forbiden_restrictions")
                .content(objectMapper.writeValueAsString(List.of("password", "print", "edition", "copy", "edit", "other_based_on_certification")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());

        addConfig("file_params", validations);
    }

    @SneakyThrows
    private void fileNameParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.FORBIDDEN_CHARS)
                .content(objectMapper.writeValueAsString(List.of("~", "\"", "#", "%", "&", "*", ":", "<", ">", "?", "!", "/", "\\", "{", "|", "}")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.TRIM)
                .content(objectMapper.writeValueAsString(List.of("space_before", "space_after")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.CHAR_CODE)
                .content(objectMapper.writeValueAsString("UTF-8"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.FULL_FILENAME_LENGTH)
            .content(objectMapper.writeValueAsString("255"))
            .validationFieldType(ValidationFieldType.INT)
            .build());
        addConfig(FileNameValidationPlugin.GROUP_NAME, validations);
    }

    @SneakyThrows
    private void technicalParams() {

        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("print_color")
                .content(objectMapper.writeValueAsString(List.of("monochromatic", "color")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
                .keyName("print_pages")
                .content(objectMapper.writeValueAsString(List.of("single", "double")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
                .keyName("grammage")
                .content(objectMapper.writeValueAsString("80"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("paper_format")
                .content(objectMapper.writeValueAsString("A4"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("paper_color")
                .content(objectMapper.writeValueAsString("white"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        addConfig("technical_params", validations);
    }

    private void addConfig(String group, Set<ValidationField> configList) {
        ConfigValidation configValidation = ConfigValidation.builder()
                .configMap(configList)
                .validationGroup(group)
                .build();
        configService.add(configValidation);
    }
}

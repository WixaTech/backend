package pl.wixatech.hackyeahbackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        technicalParams();


    }

    @SneakyThrows
    private void technicalParams() {

        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("print_color")
                .content(objectMapper.writeValueAsString(List.of("a", "b")))
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

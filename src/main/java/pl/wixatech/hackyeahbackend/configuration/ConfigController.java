package pl.wixatech.hackyeahbackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/config")
    public Map<String, Map<String, Object>> getConfig() {
        List<ConfigValidation> allConfigValidation = configService.getAllConfig();

        return allConfigValidation.stream()
                .collect(Collectors.toMap(ConfigValidation::getValidationGroup, this::mapValidationType));
    }

    private Map<String, Object> mapValidationType(ConfigValidation configValidation) {
        return configValidation.getConfigMap().stream()
                .collect(Collectors.toMap(ValidationField::getKeyName, this::mapValue));
    }

    @SneakyThrows
    private Object mapValue(ValidationField validationField) {
        return switch (validationField.getValidationFieldType()) {
            case LIST -> objectMapper.readValue(validationField.getContent(), List.class);
            case STRING -> objectMapper.readValue(validationField.getContent(), String.class);
            case INT -> objectMapper.readValue(validationField.getContent(), Integer.class);
            case DOUBLE -> objectMapper.readValue(validationField.getContent(), Double.class);
        };
    }

    @PostMapping(path = "/config/update")
    public void updateConfig(@RequestBody Map<String, Map<String, Object>> configMap) {

        configService.updateConfig(configMap);
    }

    @ExceptionHandler(ConfigService.NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementFoundException(
            ConfigService.NoSuchElementFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

}

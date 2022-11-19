package pl.wixatech.hackyeahbackend.validation.plugin;

import pl.wixatech.hackyeahbackend.document.Document;

public interface ValidationPlugin {
    ValidationResult validate(Document id);

    int getPriority();
}

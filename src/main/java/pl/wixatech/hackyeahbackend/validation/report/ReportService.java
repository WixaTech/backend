package pl.wixatech.hackyeahbackend.validation.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

  @Transactional
  public Report createReport(List<ValidationResult> validationResults) {
    boolean isSuccess = validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    return new Report(isSuccess);
  }
}

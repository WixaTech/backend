package pl.wixatech.hackyeahbackend.validation.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

  @Transactional
  public Report createReport(List<ValidationResult> validationResults) {
    boolean isSuccess = validationResults.stream().allMatch(ValidationResult::isValid);
    final var errorGroups = getErrorGroups(validationResults);
    return new Report(isSuccess, errorGroups);
  }

  private Set<ErrorGroup> getErrorGroups(List<ValidationResult> validationResults) {
    return validationResults.stream().collect(Collectors.groupingBy(ValidationResult::getGroupName))
        .entrySet()
        .stream()
        .filter(entry -> !getMessagesInGroup(entry).isEmpty())
        .map(entry -> new ErrorGroup(entry.getKey(), getMessagesInGroup(entry)))
        .collect(Collectors.toSet());
  }

  private List<String> getMessagesInGroup(java.util.Map.Entry<String, List<ValidationResult>> entry) {
    return entry.getValue().stream().map(ValidationResult::getMessageErrors).flatMap(
        Collection::stream).collect(Collectors.toList());
  }
}

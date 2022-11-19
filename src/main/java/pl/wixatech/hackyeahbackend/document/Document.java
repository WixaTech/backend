package pl.wixatech.hackyeahbackend.document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.wixatech.hackyeahbackend.validation.report.Report;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "document")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Document {

  public Document(String contentType, String filePath) {
    this.contentType = contentType;
    this.filePath = filePath;
    this.documentStatus = DocumentStatus.NEW;
  }

  @Id
  @GeneratedValue
  private Long id;

  private String contentType;

  private String filePath;

  @Enumerated(EnumType.STRING)
  private DocumentStatus documentStatus;

  private Instant parseStartAt;

  @OneToMany(cascade = CascadeType.ALL)
  @Fetch(FetchMode.JOIN)
  private Set<Report> reports;
}

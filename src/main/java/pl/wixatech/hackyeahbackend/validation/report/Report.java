package pl.wixatech.hackyeahbackend.validation.report;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "report")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Report {

  public Report(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  @Id
  @GeneratedValue
  private Long id;

  private boolean isSuccess;

  @CreationTimestamp
  private Instant created;
//  private List<String> errorMessages;

//  @ManyToOne
//  @JoinColumn(name ="document_id", referencedColumnName = "id")
//  private Document document;
}

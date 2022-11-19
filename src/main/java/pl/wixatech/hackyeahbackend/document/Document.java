package pl.wixatech.hackyeahbackend.document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
  }

  @Id
  @GeneratedValue
  private Long id;
  
  private String contentType;

  private String filePath;

}

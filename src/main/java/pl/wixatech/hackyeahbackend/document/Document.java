package pl.wixatech.hackyeahbackend.document;

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
public class Document {

  public Document(String contentType) {
    this.contentType = contentType;
  }

  @Id
  @GeneratedValue
  private Long id;
  
  private String contentType;
}

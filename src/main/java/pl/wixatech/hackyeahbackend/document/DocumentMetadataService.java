package pl.wixatech.hackyeahbackend.document;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;

import java.awt.Rectangle;
import java.util.Map;

@Service
public class DocumentMetadataService {

  public Map<String, String> getMetadataFromDoc(PDDocument pdfDocument) {
    Map<String, String> footerMetadata = extractMetadataFromFooter(pdfDocument);

    return footerMetadata;
  }

  @SneakyThrows
  private Map<String, String> extractMetadataFromFooter(PDDocument pdfDocument) {
    final var footerRectangle = new Rectangle(0, 780, 490, 110);

    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
    stripper.setSortByPosition(true);

    // add the defined rectangle to the area defined above
    stripper.addRegion("class1", footerRectangle);

    // extract the page in which the area has to be extracted from
    PDPage firstPage = pdfDocument.getPage(0);

    // extract content from the page
    stripper.extractRegions(firstPage);
    String actualTextContent = stripper.getTextForRegion("class1");

    return Map.of("footer", actualTextContent);
  }
}

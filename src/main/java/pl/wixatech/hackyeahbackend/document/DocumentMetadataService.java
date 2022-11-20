package pl.wixatech.hackyeahbackend.document;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentMetadataService {

  private static final Set<String> expectedFooterMetadata = Set.of("e-mail: ", "ePUAP ");

  public Map<String, String> getMetadataFromDoc(PDDocument pdfDocument) {
    Map<String, String> footerMetadata = extractMetadataFromFooter(pdfDocument);

    return footerMetadata;
  }

  // adresat (Imię i nazwisko/nazwa, adres pocztowy, NIP/PESEL)
  // nadawca ( nazwa, adres pocztowy, e-mail, skrytka e-PUAP, nr telefonu)
  // UNP
  // numer sprawy
  // data na piśmie
  // imię i nawisko osoby podpisującej doka

  @SneakyThrows
  private Map<String, String> extractMetadataFromFooter(PDDocument pdfDocument) {
    final var footerRectangle = new Rectangle(0, 780, 530, 110);

    String actualTextContent = extractTextFromRegion(pdfDocument, footerRectangle);

    return Arrays.stream(actualTextContent.split("\n"))
        .flatMap(line -> Arrays.stream(line.split("●")))
        .filter(value -> expectedFooterMetadata.stream().anyMatch(value::contains))
        .collect(Collectors.toMap(fragment -> expectedFooterMetadata.stream().filter(fragment::contains).findAny().orElseThrow(),
            Function.identity())).entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().replace(entry.getKey(), "")));
  }

  private String extractTextFromRegion(PDDocument pdfDocument, Rectangle footerRectangle) throws IOException {
    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
    stripper.setSortByPosition(true);

    // add the defined rectangle to the area defined above
    stripper.addRegion("class1", footerRectangle);

    // extract the page in which the area has to be extracted from
    PDPage firstPage = pdfDocument.getPage(0);

    // extract content from the page
    stripper.extractRegions(firstPage);
    return stripper.getTextForRegion("class1");
  }
}

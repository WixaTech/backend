package pl.wixatech.hackyeahbackend.document;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentMetadataService {

  public static final String ADDRESSEE_NAME = "addressee_name";
  public static final String ADDRESSEE_SURNAME = "addressee_surname";
  public static final String ADDRESSEE_STREET = "addressee_street";
  public static final String ADDRESSEE_POST_CODE = "addressee_post_code";
  public static final String ADDRESSEE_CITY = "addressee_city";
  public static final String SENDER_NAME = "sender_name";
  public static final String SENDER_SURNAME = "sender_surname";
  public static final String SENDER_STREET = "sender_street";
  public static final String SENDER_POST_CODE = "sender_post_code";
  public static final String SENDER_CITY = "sender_city";
  public static final String UNP = "unp";
  public static final String CASE_NUMBER = "case_number";

  private static final Set<String> expectedFooterMetadata = Set.of("e-mail: ", "ePUAP ");

  private static final Set<String> expectedHeaderMetadata = Set.of("e-mail: ", "ePUAP ");

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

  private Map<String, String> extractMetadataFromHeader(PDDocument pdfDocument) {
    Map<String, String> adreseeMetadata = getAdreseeMetadata(pdfDocument);
    Map<String, String> senderMetadata = getSenderMetadata(pdfDocument);

    final var resultMap = new HashMap<String, String>();
    resultMap.putAll(adreseeMetadata);
    resultMap.putAll(senderMetadata);

    return resultMap;
  }

  private Map<String, String> getAdreseeMetadata(PDDocument pdfDocument) {
    final var adreseeRectangle = new Rectangle(400, 80, 300, 80);
    final var adreseeText = extractTextFromRegion(pdfDocument, adreseeRectangle);

    return Map.of("adresee", adreseeText);
  }

  private Map<String, String> getSenderMetadata(PDDocument pdfDocument) {
    final var senderRectangle = new Rectangle(0, 230, 300, 80);
    final var senderText = extractTextFromRegion(pdfDocument, senderRectangle);

    return Map.of("sender", senderText);
  }

  @SneakyThrows
  private String extractTextFromRegion(PDDocument pdfDocument, Rectangle footerRectangle) {
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

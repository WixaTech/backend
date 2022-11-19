package pl.wixatech.hackyeahbackend;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/qr")
public class HackYeahRestController {

  private QRCodeWriter qrCodeWriter;
  private ObjectMapper objectMapper;

  @Value("#{environment.ACCESS_KEY}")
  private String accessKey;

  @Value("#{environment.SECRET_KEY}")
  private String secretKey;

  public HackYeahRestController(QRCodeWriter qrCodeWriter, ObjectMapper objectMapper) {
    this.qrCodeWriter = qrCodeWriter;
    this.objectMapper = objectMapper;
  }

  @GetMapping(value = "/generate/{qrContent}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> qrEncode(@PathVariable("qrContent") String qrContent) throws WriterException {
    final var encodedQrCode = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
    BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(encodedQrCode);
    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bufferedImage);
  }

  @GetMapping(value = "/generate-lambda/{qrContent}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> qrEncodeLambda(@PathVariable("qrContent") String qrContent) throws IOException {

    final var lambdaClient = buildLambdaClient();
    final InvokeRequest invokeRequest = buildInvokeRequest(qrContent);
    final var result = lambdaClient.invoke(invokeRequest);

    final BufferedImage bufferedImage = getBufferedImageFromLambdaResponse(result);

    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bufferedImage);
  }
  
  @GetMapping(value = "/test") 
  public String test() {
    return "test endpoint";
  }

  private AWSLambda buildLambdaClient() {
    final var credentials = new BasicAWSCredentials(accessKey, secretKey);
    return AWSLambdaClientBuilder.standard()
        .withRegion(Regions.US_EAST_1)
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }

  private InvokeRequest buildInvokeRequest(String qrContent) throws JsonProcessingException {
    final var functionArn = "arn:aws:lambda:us-east-1:242422306324:function:qr_code_j11";
    final var invokeRequest = new InvokeRequest().withFunctionName(functionArn)
        .withPayload(objectMapper.writeValueAsString(Map.of("content", qrContent)));
    return invokeRequest;
  }

  private BufferedImage getBufferedImageFromLambdaResponse(InvokeResult result) throws IOException {
    byte[] arr = new byte[result.getPayload().remaining()];
    result.getPayload().get(arr);
    byte[] decodedResponse = Base64.decodeBase64(arr);
    return ImageIO.read(new ByteArrayInputStream(decodedResponse));
  }
}

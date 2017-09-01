package gov.ehawaii.hacc.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
@RequestMapping("/reports")
public class ReportsController {

  private static final Logger LOGGER = LogManager.getLogger(ReportsController.class);

  @RequestMapping(value = "/org", method = RequestMethod.POST)
  public ResponseEntity<byte[]> generateOrganizationDataOverTimeReport(@RequestBody String json,
      HttpServletResponse response) {
    Map<String, String> map = new LinkedHashMap<>();
    for (String s : json.split("&")) {
      String[] pair = s.split("=");
      try {
        map.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        LOGGER.error("Unable to decode string: " + pair[1], e);
      }
    }

    byte[] decodedBytes = Base64.decodeBase64(map.get("base64").split(",")[1]);

    response.setHeader("Content-Disposition", "attachment; filename=output.pdf");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    createPdfFile(new Document(), decodedBytes, baos);
    return new ResponseEntity<byte[]>(Base64.encodeBase64(baos.toByteArray()), HttpStatus.OK);
  }

  private void createPdfFile(Document document, byte[] decodedBytes, OutputStream os) {
    try {
      PdfWriter.getInstance(document, os);
      document.open();
      Image image = Image.getInstance(decodedBytes);
      image.scalePercent(25.0f);
      document.add(image);
      document.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}

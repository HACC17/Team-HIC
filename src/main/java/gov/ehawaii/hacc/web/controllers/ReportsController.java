package gov.ehawaii.hacc.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
  public ResponseEntity<byte[]> generateOrganizationDataOverTimeReport(@RequestBody String json) {
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

    Document document = new Document();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    createPdfFile(document, decodedBytes, baos);

    document = new Document();
    File tempFile;
    try {
      tempFile = File.createTempFile("output", ".pdf");
    }
    catch (IOException ioe) {
      throw new RuntimeException("Unable to create temp file.", ioe);
    }
    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      createPdfFile(document, decodedBytes, fos);
    }
    catch (Exception fnfe) {
      throw new RuntimeException("Unable to create temp file.", fnfe);
    }
    LOGGER.info("Path of PDF file: " + tempFile.getAbsolutePath());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/pdf"));
    String filename = "output.pdf";
    headers.setContentDispositionFormData(filename, filename);
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    ResponseEntity<byte[]> response =
        new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
    return response;
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

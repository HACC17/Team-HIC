package gov.ehawaii.hacc.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
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
import gov.ehawaii.hacc.pdf.PdfUtils;

@Controller
@RequestMapping("/reports")
public class ReportsController {

  private static final Logger LOGGER = LogManager.getLogger(ReportsController.class);

  @RequestMapping(value = "/fiscalYear", method = RequestMethod.POST)
  public ResponseEntity<byte[]> generateFiscalYearReport(@RequestBody String json) {
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

    String tableHeading = "Total Amount of Money for Fiscal Year " + map.get("year");
    String[] columnHeadings = { map.get("columnOne"), map.get("columnTwo") };
    String[] columnOneData = map.get("labels").split(";");
    String[] columnTwoData = map.get("dataset").split(",");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfUtils.createPdfFile(new Document(), Base64.decodeBase64(map.get("base64").split(",")[1]),
        baos, PdfUtils.PIE_CHART_IMAGE_SCALE_PERCENT, tableHeading, columnHeadings, columnOneData,
        columnTwoData, true);

    return new ResponseEntity<byte[]>(Base64.encodeBase64(baos.toByteArray()), HttpStatus.OK);
  }

  @RequestMapping(value = "/top", method = RequestMethod.POST)
  public ResponseEntity<byte[]> generateTopNReport(@RequestBody String json) {
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

    String tableHeading = "Top " + map.get("n") + " " + map.get("columnOne") + "s by " + map.get("columnTwo");
    String[] columnHeadings = { map.get("columnOne"), map.get("columnTwo") };
    String[] columnOneData = map.get("labels").split(";");
    String[] columnTwoData = map.get("dataset").split(",");
    boolean isFiscal = Boolean.parseBoolean(map.get("isFiscal"));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfUtils.createPdfFile(new Document(), Base64.decodeBase64(map.get("base64").split(",")[1]),
        baos, PdfUtils.PIE_CHART_IMAGE_SCALE_PERCENT, tableHeading, columnHeadings, columnOneData,
        columnTwoData, isFiscal);

    return new ResponseEntity<byte[]>(Base64.encodeBase64(baos.toByteArray()), HttpStatus.OK);
  }

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

    String tableHeading = map.get("organization");
    String[] columnHeadings = { map.get("columnOne"), map.get("columnTwo") };
    String[] columnOneData = map.get("labels").split(",");
    String[] columnTwoData = map.get("dataset").split(",");
    boolean isFiscal = Boolean.parseBoolean(map.get("isFiscal"));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfUtils.createPdfFile(new Document(), Base64.decodeBase64(map.get("base64").split(",")[1]),
        baos, PdfUtils.LINE_CHART_IMAGE_SCALE_PERCENT, tableHeading, columnHeadings, columnOneData,
        columnTwoData, isFiscal);

    return new ResponseEntity<byte[]>(Base64.encodeBase64(baos.toByteArray()), HttpStatus.OK);
  }

}

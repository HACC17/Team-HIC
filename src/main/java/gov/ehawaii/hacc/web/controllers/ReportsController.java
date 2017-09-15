package gov.ehawaii.hacc.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import gov.ehawaii.hacc.pdf.PdfUtils;

/**
 * This controller handles all requests going to the <code>/reports</code> endpoint. The methods in
 * this class generate a PDF file with a chart and table in it, and send the PDF file back to the
 * client.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@RequestMapping("/reports")
public class ReportsController {

  private static final Logger LOGGER = LogManager.getLogger(ReportsController.class);

  /**
   * Generates a PDF file that includes a pie graph that displays the total amount of money for a
   * given fiscal year for the top 5 organizations. The table in the file will display the total
   * amount in dollars.
   * 
   * @param json A JSON string that contains the data for the top 5 organizations.
   * @return A byte array representing a PDF file is sent back to the client.
   */
  @RequestMapping(value = "/fiscalYear", method = RequestMethod.POST)
  public final ResponseEntity<byte[]> generateFiscalYearReport(@RequestBody final String json) {
    Map<String, String> map = getParameters(json);

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

  /**
   * Generates a PDF file that contains a pie graph that displays the top 5 or 10 organizations or
   * projects by a given type of data. The table in the file will display properly formatted
   * information for each organization or project.
   * 
   * @param json A JSON string that contains the data for the top 5 or 10 organizations or projects.
   * @return A byte array representing a PDF file is sent back to the client.
   */
  @RequestMapping(value = "/top", method = RequestMethod.POST)
  public final ResponseEntity<byte[]> generateTopNReport(@RequestBody final String json) {
    Map<String, String> map = getParameters(json);

    String tableHeading =
        "Top " + map.get("n") + " " + map.get("columnOne") + "s by " + map.get("columnTwo");
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

  /**
   * Generates a PDF file that contains a time series (line) chart for an organization over a period
   * of time. The table in the file will contain information about each data point on the graph.
   * 
   * @param json A JSON string that contains time series data for an organization.
   * @return A byte array representing a PDF file is sent back to the client.
   */
  @RequestMapping(value = "/org", method = RequestMethod.POST)
  public final ResponseEntity<byte[]> generateOrganizationDataOverTimeReport(
      @RequestBody final String json) {
    Map<String, String> map = getParameters(json);

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

  /**
   * Generates a PDF file that contains a pie graph that displays the top N organizations for a
   * location. The table in the file will display properly formatted information for each
   * organization.
   * 
   * @param json A JSON string that contains the data for the top N organizations for a location.
   * @return A byte array representing a PDF file is sent back to the client.
   */
  @RequestMapping(value = "/locations", method = RequestMethod.POST)
  public final ResponseEntity<byte[]> generateLocationsReport(@RequestBody final String json) {
    Map<String, String> map = getParameters(json);

    String tableHeading = map.get("title");
    String[] columnHeadings = { map.get("columnOne"), "Total " + map.get("columnTwo") };

    int index = 0;
    List<Map<String, String>> totalsList;
    try {
      totalsList = new ObjectMapper().readValue(map.get("totals"),
          new TypeReference<List<Map<String, String>>>() {
          });
    }
    catch (Exception e) {
      LOGGER.error("An error occurred while trying to parse JSON data: " + e);
      return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    String[] columnOneData = new String[totalsList.size()];
    long[] columnTwoData = new long[totalsList.size()];
    for (Map<String, String> totalMap : totalsList) {
      columnOneData[index] = totalMap.get("name");
      columnTwoData[index++] = Long.parseLong(totalMap.get("y"));
    }

    for (int n = 0; n < columnTwoData.length; n++) {
      for (int m = 0; m < columnTwoData.length - 1 - n; m++) {
        if (columnTwoData[m] < columnTwoData[m + 1]) {
          long swapLong = columnTwoData[m];
          columnTwoData[m] = columnTwoData[m + 1];
          columnTwoData[m + 1] = swapLong;
          String swapString = columnOneData[m];
          columnOneData[m] = columnOneData[m + 1];
          columnOneData[m + 1] = swapString;
        }
      }
    }

    String[] columnTwoDataStrings =
        Arrays.stream(columnTwoData).mapToObj(String::valueOf).toArray(size -> new String[size]);

    boolean isFiscal = Boolean.parseBoolean(map.get("isFiscal"));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfUtils.createPdfFile(new Document(), Base64.decodeBase64(map.get("base64")), baos,
        PdfUtils.LOCATIONS_PIE_CHART_IMAGE_SCALE_PERCENT, tableHeading, columnHeadings,
        columnOneData, columnTwoDataStrings, isFiscal);

    return new ResponseEntity<byte[]>(Base64.encodeBase64(baos.toByteArray()), HttpStatus.OK);
  }

  /**
   * Gets the data needed to generate a graph and table from a JSON string and puts them into a
   * {@link Map}. The data in the JSON string will be URL-decoded first before being put in the map.
   * 
   * @param json A JSON string containing data.
   * @return A map containing the same data.
   */
  private static Map<String, String> getParameters(final String json) {
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
    return map;
  }

}

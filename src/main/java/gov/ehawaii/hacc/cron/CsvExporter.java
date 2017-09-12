package gov.ehawaii.hacc.cron;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

/**
 * This background task exports ALL the grants in a repository to a CSV file.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Component("CSVExportTask")
public class CsvExporter implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(CsvExporter.class);

  @Autowired
  private GrantsService grantsService;

  @Value("${exportDirectory}")
  private String exportDirectory;

  @Value("${filenamePrefix}")
  private String filenamePrefix;

  @Value("${filenameSuffix}")
  private String filenameSuffix;

  @Override
  public final void run() {
    LOGGER.info("CSVExportTask started.");
    export(grantsService.getGrants(new HashMap<>()));
  }

  /**
   * Exports the given list of grants to a CSV file. The file is sorted alphabetically in ascending
   * order by organization.
   * 
   * @param grants The list of grants to save to a Microsoft Excel file.
   */
  private void export(final List<Grant> grants) {
    Assert.notNull(grants, "grants must not be null.");

    String filename = exportDirectory + filenamePrefix + "." + filenameSuffix;
    LOGGER.info("Path: " + filename);

    List<String> header = Arrays.asList("Fiscal Year", "Grant Type", "Organization", "Project",
        "Amount", "Location", "Strategic Priority", "Strategic Results",
        "Total Number of People Served", "Number of Native Hawaiians Served", "Grant Status");

    CSVFormat csvFileFormat =
        CSVFormat.DEFAULT.withHeader(header.toArray(new String[header.size()]));

    Collections.sort(grants,
        (grant1, grant2) -> grant1.getOrganization().compareTo(grant2.getOrganization()));

    try (
        OutputStreamWriter fileWriter =
            new OutputStreamWriter(new FileOutputStream(filename), Charset.defaultCharset());
        CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat)) {
      for (Grant grant : grants) {
        List<Object> record = new ArrayList<>();
        record.add(grant.getFiscalYear());
        record.add(grant.getGrantType());
        record.add(grant.getOrganization());
        record.add(grant.getProject());
        record.add(grant.getAmount());
        record.add(grant.getLocation());
        record.add(grant.getStrategicPriority());
        record.add(grant.getStrategicResults());
        record.add(grant.getTotalNumberServed());
        record.add(grant.getNumberNativeHawaiiansServed());
        record.add(grant.getGrantStatus());
        csvFilePrinter.printRecord(record);
      }
      LOGGER.info("Successfully wrote " + grants.size() + " grants to " + filename);
    }
    catch (IOException e) {
      LOGGER.error("Something went wrong while trying to write CSV file: " + e.getMessage(), e);
    }
  }

}

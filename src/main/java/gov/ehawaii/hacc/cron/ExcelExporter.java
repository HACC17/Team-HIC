package gov.ehawaii.hacc.cron;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

/**
 * This background task exports ALL the grants in a repository to a Microsoft Excel file.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Component("ExcelExportTask")
public class ExcelExporter implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(ExcelExporter.class);

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
    LOGGER.info("ExcelExportTask started.");
    export(grantsService.getGrants(new HashMap<>()));
  }

  /**
   * Exports the given list of grants to a Microsoft Excel file. The file is sorted alphabetically
   * in ascending order by organization.
   * 
   * @param grants The list of grants to save to a Microsoft Excel file.
   */
  private void export(final List<Grant> grants) {
    Assert.notNull(grants, "grants must not be null.");

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Grants");
    String[] headings = { "Fiscal Year", "Organization", "Project", "Amount", "Status", "Location",
        "Grant Type", "Strategic Priority", "Strategic Results", "Total Number Served",
        "Number of Native Hawaiians Served" };

    int currentRow = 0;
    Row row = sheet.createRow(currentRow);

    int currentColumn = 0;
    for (String heading : headings) {
      row.createCell(currentColumn++).setCellValue(heading);
    }

    Collections.sort(grants,
        (grant1, grant2) -> grant1.getOrganization().compareTo(grant2.getOrganization()));

    for (Grant grant : grants) {
      row = sheet.createRow(++currentRow);
      currentColumn = 0;
      Cell cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getFiscalYear());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getOrganization());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getProject());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getAmount());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getGrantStatus());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getLocation());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getGrantType());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getStrategicPriority());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getStrategicResults());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getTotalNumberServed());
      cell = row.createCell(currentColumn++);
      cell.setCellValue(grant.getNumberNativeHawaiiansServed());
    }

    String filename = exportDirectory + filenamePrefix + "." + filenameSuffix;
    LOGGER.info("Path: " + filename);

    File file;
    try {
      Files.createDirectories(Paths.get(exportDirectory));
      file = new File(filename);
      if (!file.exists()) {
        Files.createFile(file.toPath());
      }
    }
    catch (IOException ioe) {
      LOGGER.error("An error occurred while trying to create Excel file: " + ioe.getMessage(), ioe);
      try {
        workbook.close();
      }
      catch (IOException e) {
        LOGGER.error("An error occurred while trying to close Excel file: " + e.getMessage(), e);
      }
      return;
    }

    try (FileOutputStream fos = new FileOutputStream(file)) {
      workbook.write(fos);
      workbook.close();
      LOGGER.info("Successfully wrote " + grants.size() + " grants to " + file + ".");
    }
    catch (IOException ioe) {
      LOGGER.error("An error occurred while trying to close Excel file: " + ioe.getMessage(), ioe);
    }
  }

}

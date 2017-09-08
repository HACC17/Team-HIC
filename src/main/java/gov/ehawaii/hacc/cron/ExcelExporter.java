package gov.ehawaii.hacc.cron;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
  public void run() {
    LOGGER.info("GrantsExcelExporter task started.");
    export(grantsService.getGrants(new HashMap<>()));
  }


  private void export(List<Grant> grants) {
    Assert.notNull(grants, "grants must not be null.");

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Grants");
    String[] headings = { "Fiscal Year", "Organization", "Project", "Amount", "Status", "Location", "Grant Type",
        "Strategic Priority", "Strategic Results", "Total Number Served", "Number of Native Hawaiians Served" };

    int currentRow = 0;
    Row row = sheet.createRow(currentRow);

    int currentColumn = 0;
    for (String heading : headings) {
      row.createCell(currentColumn++).setCellValue(heading);
    }

    currentRow++;

    Collections.sort(grants, (grant1, grant2) -> grant1.getOrganization().compareTo(grant2.getOrganization()));

    for (Grant grant : grants) {
      row = sheet.createRow(currentRow);
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
      currentRow++;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");
    String filename =
        exportDirectory + filenamePrefix + "_" + formatter.format(LocalDateTime.now()) + "." + filenameSuffix;
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

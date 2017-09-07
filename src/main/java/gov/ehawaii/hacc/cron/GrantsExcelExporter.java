package gov.ehawaii.hacc.cron;

import java.io.FileOutputStream;
import java.io.IOException;
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
public class GrantsExcelExporter implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(GrantsExcelExporter.class);

  @Autowired
  private GrantsService grantsService;

  @Value("${exportDir}")
  private String exportDir;

  @Value("${exportFilename")
  private String exportFilename;


  @Override
  public void run() {
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

    for (Grant grant : grants) {
      row = sheet.createRow(currentRow);
      currentColumn = 0;
      Cell cell = row.createCell(currentColumn);
      cell.setCellValue(grant.getFiscalYear());
      currentColumn++;
      cell.setCellValue(grant.getOrganization());
      currentColumn++;
      cell.setCellValue(grant.getProject());
      currentColumn++;
      cell.setCellValue(grant.getAmount());
      currentColumn++;
      cell.setCellValue(grant.getGrantStatus());
      currentColumn++;
      cell.setCellValue(grant.getLocation());
      currentColumn++;
      cell.setCellValue(grant.getGrantType());
      currentColumn++;
      cell.setCellValue(grant.getStrategicPriority());
      currentColumn++;
      cell.setCellValue(grant.getStrategicResults());
      currentColumn++;
      cell.setCellValue(grant.getTotalNumberServed());
      currentColumn++;
      cell.setCellValue(grant.getNumberNativeHawaiiansServed());
      currentRow++;
    }

    String filename = exportDir + exportFilename;
    try (FileOutputStream fos = new FileOutputStream(filename)) {
      workbook.write(fos);
      workbook.close();
      LOGGER.info("Successfully created " + filename + ".");
    }
    catch (IOException ioe) {
      LOGGER.error("An error occurred while trying to create Excel file: " + ioe.getMessage(), ioe);
    }
  }

}

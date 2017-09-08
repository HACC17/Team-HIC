package gov.ehawaii.hacc.importers;

import static gov.ehawaii.hacc.importers.Importer.stringToInt;
import static gov.ehawaii.hacc.importers.Importer.stringToLong;
import static gov.ehawaii.hacc.importers.Importer.trim;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import gov.ehawaii.hacc.repositories.impl.Tables;
import gov.ehawaii.hacc.specifications.IdSpecification;

/**
 * This importer will read in a Microsoft Excel file and insert all the grants found in it into the repository.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Component
public class ExcelImporter implements Importer {

  @Autowired
  private GrantsRepository repository;

  @Value(value = "classpath:data/2013_2016_data.xlsx")
  private Resource excelFile;

  @Override
  public boolean importData() {
    try (FileInputStream file = new FileInputStream(excelFile.getFile());
        Workbook workbook = new XSSFWorkbook(file)) {
      Sheet datatypeSheet = workbook.getSheetAt(0);
      Iterator<Row> iterator = datatypeSheet.iterator();

      int count = 0;
      while (iterator.hasNext()) {
        Row currentRow = iterator.next();
        Iterator<Cell> cellIterator = currentRow.iterator();

        while (cellIterator.hasNext()) {
          Grant grant = new Grant();
          grant.setFiscalYear(stringToInt(cellIterator.next().getStringCellValue()));
          grant.setGrantType(trim(cellIterator.next().getStringCellValue()));
          grant.setOrganization(trim(cellIterator.next().getStringCellValue()));
          grant.setProject(trim(cellIterator.next().getStringCellValue()));
          grant.setAmount(stringToLong(cellIterator.next().getStringCellValue()));
          grant.setLocation(trim(cellIterator.next().getStringCellValue()));
          grant.setStrategicPriority(trim(cellIterator.next().getStringCellValue()));
          grant.setStrategicResults(trim(cellIterator.next().getStringCellValue()));
          grant.setTotalNumberServed(stringToInt(cellIterator.next().getStringCellValue()));
          grant.setNumberNativeHawaiiansServed(
              stringToInt(cellIterator.next().getStringCellValue()));
          grant.setGrantStatus(repository.findValueForId(new IdSpecification(Tables.GRANT_STATUSES,
              SqlStatements.GRANT_STATUS, cellIterator.next().getStringCellValue())));
          if (repository.insertGrant(grant)) {
            LOGGER.info("Successfully saved grant [" + grant + "] to repository.");
            count++;
          }
        }
      }

      LOGGER.info("Successfully imported " + count + " grant(s) into repository.");
      return true;
    }
    catch (IOException ioe) {
      LOGGER.error("An error occurred while trying to parse CSV file: " + ioe.getMessage(), ioe);
      return false;
    }
  }

}

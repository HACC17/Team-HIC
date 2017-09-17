package gov.ehawaii.hacc.importers;

import static gov.ehawaii.hacc.importers.Importer.stringToInt;
import static gov.ehawaii.hacc.importers.Importer.stringToLong;
import static gov.ehawaii.hacc.importers.Importer.trim;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.repositories.impl.Columns;
import gov.ehawaii.hacc.repositories.impl.Tables;
import gov.ehawaii.hacc.specifications.IdSpecification;

/**
 * This importer will read in a CSV file and insert all the grants found in it into the repository.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Component
public class CsvImporter implements Importer {

  private static final int NUMBER_OF_COLUMNS = 11;

  @Autowired
  private GrantsRepository repository;

  @Value(value = "classpath:data/2013_2016_data.csv")
  private Resource csvFile;

  @Override
  public final boolean importData() {
    try {
      return importData(csvFile.getFile());
    }
    catch (IOException ioe) {
      LOGGER.error("An error occurred while trying to parse CSV file: " + ioe.getMessage(), ioe);
      return false;
    }
  }

  @Override
  public final boolean importData(final File file) {
    try (
        Reader reader =
            new InputStreamReader(new FileInputStream(file), Charset.defaultCharset());
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withSkipHeaderRecord())) {
      List<CSVRecord> records = parser.getRecords();
      records.remove(0);
      int count = 0;
      for (CSVRecord record : records) {
        if (record.size() != NUMBER_OF_COLUMNS) {
          LOGGER
              .error("Expected " + NUMBER_OF_COLUMNS + " columns but found " + record.size() + ".");
          continue;
        }
        Grant grant = new Grant();
        grant.setFiscalYear(stringToInt(record.get(0)));
        grant.setGrantType(trim(record.get(1)));
        grant.setOrganization(trim(record.get(2)));
        grant.setProject(trim(record.get(3)));
        grant.setAmount(stringToLong(record.get(4)));
        grant.setLocation(trim(record.get(5)));
        grant.setStrategicPriority(trim(record.get(6)));
        grant.setStrategicResults(trim(record.get(7)));
        grant.setTotalNumberServed(stringToInt(record.get(8)));
        grant.setNumberNativeHawaiiansServed(stringToInt(record.get(9)));
        grant.setGrantStatus(repository.findValueForId(
            new IdSpecification(Tables.GRANT_STATUSES, Columns.GRANT_STATUS, record.get(10))));
        if (repository.insertGrant(grant)) {
          LOGGER.info("Successfully saved grant [" + grant + "] to repository.");
          count++;
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

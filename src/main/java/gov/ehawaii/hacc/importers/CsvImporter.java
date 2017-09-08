package gov.ehawaii.hacc.importers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * This importer will read in a CSV file and insert all the grants found in it into the repository.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Component
public class CsvImporter implements Importer {

  private static final Logger LOGGER = LogManager.getLogger(CsvImporter.class);

  private static final int NUMBER_OF_COLUMNS = 11;

  @Autowired
  private GrantsRepository dao;

  @Value(value = "classpath:data/2013_2016_data.csv")
  private Resource csvFile;

  @Override
  public boolean importData() {
    try (
        Reader reader =
            new InputStreamReader(new FileInputStream(csvFile.getFile()), Charset.defaultCharset());
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withSkipHeaderRecord())) {
      List<CSVRecord> records = parser.getRecords();
      records.remove(0);
      int count = 0;
      for (CSVRecord record : records) {
        if (record.size() != NUMBER_OF_COLUMNS) {
          LOGGER.error("Row does not contain " + NUMBER_OF_COLUMNS + " columns, only "
              + record.size() + ".");
          continue;
        }
        Grant grant = new Grant();
        grant.setFiscalYear(stringToInt(record.get(0)));
        grant.setGrantType(trim(record.get(1)));
        grant.setOrganization(trim(record.get(2)));
        grant.setProject(trim(record.get(3)));
        grant.setAmount(stringToInt(record.get(4)));
        grant.setLocation(trim(record.get(5)));
        grant.setStrategicPriority(trim(record.get(6)));
        grant.setStrategicResults(trim(record.get(7)));
        grant.setTotalNumberServed(stringToInt(record.get(8)));
        grant.setNumberNativeHawaiiansServed(stringToInt(record.get(9)));
        grant.setGrantStatus(dao.findValueForId(new IdSpecification(Tables.GRANT_STATUSES,
            SqlStatements.GRANT_STATUS, record.get(10))));
        if (dao.insertGrant(grant)) {
          LOGGER.info("Successfully saved grant [" + grant + "] to database.");
          count++;
        }
      }
      LOGGER.info("Successfully imported " + count + " grant(s) into database.");
      return true;
    }
    catch (IOException ioe) {
      LOGGER.error("An error occurred while trying to parse CSV file: " + ioe.getMessage(), ioe);
      return false;
    }
  }

  /**
   * Trims off any whitespace characters at both ends of the given string.
   * 
   * @param value The string from which to strip whitespace characters at the beginning and end.
   * @return A string with no whitespace characters at either end, or an empty string.
   */
  private static String trim(String value) {
    return value == null || value.isEmpty() ? "" : value.trim();
  }

  /**
   * Converts the given string to an integer.
   * 
   * @param value The string to convert.
   * @return An integer, or 0 if the string cannot be converted.
   */
  private static int stringToInt(String value) {
    if (value == null || value.isEmpty()) {
      return 0;
    }
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException nfe) {
      LOGGER.error("Unable to parse \"" + value + "\" to int.", nfe);
      return 0;
    }
  }

}

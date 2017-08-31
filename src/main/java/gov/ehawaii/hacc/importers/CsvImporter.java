package gov.ehawaii.hacc.importers;

import java.io.FileReader;
import java.io.Reader;
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
import gov.ehawaii.hacc.dao.GrantsDao;
import gov.ehawaii.hacc.model.Grant;

@Component
public class CsvImporter extends Importer {

  private static final Logger LOGGER = LogManager.getLogger(CsvImporter.class);

  private static final int NUMBER_OF_COLUMNS = 11;

  @Autowired
  private GrantsDao dao;

  @Value(value = "classpath:data/2013_2016_data.csv")
  private Resource csvFile;


  @Override
  public Resource getFile() {
    return csvFile;
  }


  @Override
  public boolean importData() {
    try {
      Reader reader = new FileReader(csvFile.getFile());
      try (CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withSkipHeaderRecord())) {
        List<CSVRecord> records = parser.getRecords();
        records.remove(0);
        int count = 0;
        for (CSVRecord record : records) {
          if (record.size() != NUMBER_OF_COLUMNS) {
            LOGGER.error("Row does not contain " + NUMBER_OF_COLUMNS + " columns, only " + record.size() + ".");
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
          grant.setGrantStatus(dao.getGrantStatusForId(stringToInt(record.get(10))));
          if (dao.saveGrant(grant)) {
            LOGGER.info("Successfully saved grant [" + grant + "] to database.");
            count++;
          }
        }
        LOGGER.info("Successfully imported " + count + " grant(s) into database.");
      }
    }
    catch (Exception e) {
      LOGGER.error("An error occurred while trying to parse CSV file: " + e.getMessage(), e);
      return false;
    }
    return true;
  }


  private static String trim(String value) {
    return value == null || value.isEmpty() ? "" : (value.length() < 200 ? value : value.substring(0, 200));
  }


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

package gov.ehawaii.hacc.importers;

import java.io.FileReader;
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
      CSVParser parser = CSVFormat.DEFAULT.parse(new FileReader(csvFile.getFile()));
      List<CSVRecord> records = parser.getRecords();
      int count = 0;
      for (CSVRecord record : records) {
        if (record.size() != NUMBER_OF_COLUMNS) {
          continue;
        }
        Grant grant = new Grant();
        grant.setFiscalYear(Integer.parseInt(record.get(0)));
        grant.setGrantType(record.get(1));
        grant.setOrganization(record.get(2));
        grant.setProject(record.get(3));
        grant.setAmount(Integer.parseInt(record.get(4)));
        grant.setLocation(record.get(5));
        grant.setStrategicPriority(record.get(6));
        grant.setStrategicResults(record.get(7));
        grant.setTotalNumberServed(Integer.parseInt(record.get(8)));
        grant.setNumberNativeHawaiiansServed(Integer.parseInt(record.get(9)));
        grant.setGrantStatus(dao.getGrantStatusForId(Integer.parseInt(record.get(10))));
        if (dao.saveGrant(grant)) {
          LOGGER.info("Successfully saved grant [" + grant + "] to database.");
          count++;
        }
      }
      LOGGER.info("Successfully imported " + count + " grant(s) into database.");
    }
    catch (Exception e) {
      LOGGER.error("An error occurred while trying to parse CSV file: " + e.getMessage(), e);
      return false;
    }
    return true;
  }

}

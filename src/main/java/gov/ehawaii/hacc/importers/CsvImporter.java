package gov.ehawaii.hacc.importers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CsvImporter extends Importer {

  @Value(value = "classpath:data/2013_2016_data.csv")
  private Resource csvFile;

  @Override
  public Resource getFile() {
    return csvFile;
  }

}

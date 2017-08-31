package gov.ehawaii.hacc.importers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class ExcelImporter extends Importer {

  @Value(value = "classpath:data/2013_2016_data.xlsx")
  private Resource excelFile;

  @Override
  public Resource getFile() {
    return excelFile;
  }

}

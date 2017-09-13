package gov.ehawaii.hacc.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;

@Component
public final class PropertiesFileManager {

  private static final Logger LOGGER = LogManager.getLogger(PropertiesFileManager.class);

  private static final String PROPERTIES_FILE_NAME = "settings.properties";

  @Value("${exportDirectory}")
  private String directory;

  public synchronized void saveProperty(final String key, final String value) {
    String path = directory + PROPERTIES_FILE_NAME;
    try (OutputStream out = new FileOutputStream(new File(path))) {
      Properties props = new Properties();
      props.setProperty(key, value);
      DefaultPropertiesPersister p = new DefaultPropertiesPersister();
      p.store(props, out, "Application Settings");
    }
    catch (IOException ioe) {
      LOGGER.error("There was a problem trying to write {" + key + ": " + value + "} to " + path
          + ": " + ioe.getMessage(), ioe);
    }
  }

  public synchronized String getProperty(final String key, final String defaultValue) {
    String path = directory + PROPERTIES_FILE_NAME;
    Properties prop = new Properties();
    try (InputStream stream = new FileInputStream(new File(path))) {
      prop.load(stream);
      return prop.getProperty(key, defaultValue);
    }
    catch (IOException ioe) {
      LOGGER.error("There was a problem trying to read in the properties from " + path + ": "
          + ioe.getMessage(), ioe);
      return defaultValue;
    }
  }

}

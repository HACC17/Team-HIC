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

/**
 * This class contains a method that saves a key-value pair to a properties file. It also contains a
 * method that will retrieve a value given a key.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Component
public final class PropertiesFileManager {

  private static final Logger LOGGER = LogManager.getLogger(PropertiesFileManager.class);

  private static final String PROPERTIES_FILE_NAME = "settings.properties";

  @Value("${exportDirectory}")
  private String directory;

  /**
   * Saves the following key and value to a properties file.
   * 
   * @param key The key.
   * @param value The value.
   */
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

  /**
   * Gets the value for the given key from a properties file.
   * 
   * @param key The key for which to get the value.
   * @param defaultValue The default value that is returned if the key does not exist or a problem
   * was encountered while trying to get the value.
   * @return The value for the given key, or the default value if the key does not exist.
   */
  public synchronized String getProperty(final String key, final String defaultValue) {
    String path = directory + PROPERTIES_FILE_NAME;
    File file = new File(path);
    if (!file.exists()) {
      try {
        if (file.createNewFile()) {
          LOGGER.info(path + " was successfully created.");
        }
      }
      catch (IOException ioe) {
        LOGGER.error("There was a problem trying to create " + path + ": " + ioe.getMessage(), ioe);
        return defaultValue;
      }
    }
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

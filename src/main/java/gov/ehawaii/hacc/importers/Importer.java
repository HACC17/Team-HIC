package gov.ehawaii.hacc.importers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementations of this interface will import grants from a file into a repository.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public interface Importer {

  final Logger LOGGER = LogManager.getLogger(Importer.class);

  /**
   * This method will read in all the grants from a file and then import them into a repository.
   * 
   * @return <code>true</code> if the import was successful, <code>false</code> otherwise.
   */
  boolean importData();

  /**
   * Trims off any whitespace characters at both ends of the given string.
   * 
   * @param value The string from which to strip whitespace characters at the beginning and end.
   * @return A string with no whitespace characters at either end, or an empty string.
   */
  static String trim(String value) {
    return value == null || value.isEmpty() ? "" : value.trim();
  }

  /**
   * Converts the given string to an integer.
   * 
   * @param value The string to convert.
   * @return An integer, or 0 if the string cannot be converted.
   */
  static int stringToInt(String value) {
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

  /**
   * Converts the given string to a long.
   * 
   * @param value The string to convert.
   * @return A long, or 0 if the string cannot be converted.
   */
  static long stringToLong(String value) {
    if (value == null || value.isEmpty()) {
      return 0;
    }
    try {
      return Long.parseLong(value);
    }
    catch (NumberFormatException nfe) {
      LOGGER.error("Unable to parse \"" + value + "\" to long.", nfe);
      return 0;
    }
  }

}

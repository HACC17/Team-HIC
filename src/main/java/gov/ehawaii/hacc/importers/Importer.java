package gov.ehawaii.hacc.importers;

/**
 * Implementations of this interface will import grants from a file into a repository.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public interface Importer {

  /**
   * This method will read in all the grants from a file and then import them into a repository.
   * 
   * @return <code>true</code> if the import was successful, <code>false</code> otherwise.
   */
  boolean importData();

}

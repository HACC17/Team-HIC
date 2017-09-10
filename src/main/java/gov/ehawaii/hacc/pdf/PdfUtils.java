package gov.ehawaii.hacc.pdf;

import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.commons.lang.WordUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This utility class contains methods related to PDFs, such as creating a PDF and adding a table to a PDF.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public final class PdfUtils {

  public static final float PIE_CHART_IMAGE_SCALE_PERCENT = 30.0f;
  public static final float LINE_CHART_IMAGE_SCALE_PERCENT = 50.0f;
  public static final float LOCATIONS_PIE_CHART_IMAGE_SCALE_PERCENT = 80.0f;

  /** Do not instantiate this class. */
  private PdfUtils() {
  }

  /**
   * This method creates a PDF file with an image and a two-column table below the image. The file is
   * written to the given output stream.
   * 
   * @param document The document used to create a PDF.
   * @param decodedBytes A base-64 string that represents an image and is <b>already</b> decoded.
   * @param os The output stream to which to write the PDF.
   * @param imageScalePercent The scale in percent, used to resize the image.
   * @param tableHeading The name for the table.
   * @param columnHeadings An array of column headings.
   * @param columnOneData An array of data for the first column in the table.
   * @param columnTwoData An array of data for the second column in the table.
   * @param isFiscal <code>true</code> if the data in the second column is fiscal, <code>false</code> otherwise.
   */
  @SuppressWarnings("checkstyle:parameternumber")
  public static void createPdfFile(final Document document, final byte[] decodedBytes, final OutputStream os,
      final float imageScalePercent, final String tableHeading, final String[] columnHeadings, final String[] columnOneData,
      final String[] columnTwoData, final boolean isFiscal) {
    try {
      PdfWriter writer = PdfWriter.getInstance(document, os);
      writer.setPageEvent(new Rotate());
      document.open();
      Image image = Image.getInstance(decodedBytes);
      image.scalePercent(imageScalePercent);
      document.add(image);
      document.add(createTable(tableHeading, columnHeadings, columnOneData, columnTwoData, isFiscal));
      document.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates a table that can be inserted into a PDF file.
   * 
   * @param tableHeading The name for the table.
   * @param columnHeadings An array of column headings.
   * @param columnOneData An array of data for the first column in the table.
   * @param columnTwoData An array of data for the second column in the table.
   * @param isFiscal <code>true</code> if the data in the second column is fiscal, <code>false</code> otherwise.
   * @return A table that can be inserted into a PDF file.
   * @throws DocumentException If there are problems creating the table.
   */
  private static PdfPTable createTable(final String tableHeading, final String[] columnHeadings,
      final String[] columnOneData, final String[] columnTwoData, final boolean isFiscal) throws DocumentException {
    PdfPTable table = new PdfPTable(2);
    table.setTotalWidth(new float[] { 250, 250 });
    table.setLockedWidth(true);
    table.setSpacingBefore(20.0f);

    Font font = new Font();
    font.setColor(BaseColor.WHITE);

    PdfPCell cell = createCell(tableHeading, font);
    cell.setColspan(2);
    cell.setBackgroundColor(new BaseColor(45, 65, 84));
    table.addCell(cell);

    // header row
    for (String heading : columnHeadings) {
      cell = createCell(heading, font);
      cell.setBackgroundColor(new BaseColor(45, 65, 84));
      table.addCell(cell);
    }

    for (int idx = 0; idx < Math.max(columnOneData.length, columnTwoData.length); idx++) {
      String data = idx < columnOneData.length ? "" : columnOneData[idx];
      table.addCell(createCell(WordUtils.capitalizeFully(data), null));

      data = idx < columnTwoData.length ? "" : columnTwoData[idx];
      if (!data.isEmpty()) {
        Locale en = new Locale("en", "US");
        int num = Integer.parseInt(data);
        if (isFiscal) {
          data = NumberFormat.getCurrencyInstance(en).format(num);
        }
        else {
          data = NumberFormat.getNumberInstance(en).format(num);
        }
      }
      table.addCell(createCell(WordUtils.capitalizeFully(data), null));
    }

    return table;
  }

  /**
   * Creates a cell that can be inserted into a table.
   * 
   * @param cellValue The value for the cell.
   * @param font Cell font (optional).
   * @return A cell containing a value.
   */
  private static PdfPCell createCell(final String cellValue, final Font font) {
    PdfPCell cell;
    if (font == null) {
      cell = new PdfPCell(new Phrase(cellValue));
    }
    else {
      cell = new PdfPCell(new Phrase(cellValue, font));
    }
    cell.setFixedHeight(20);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setBorder(Rectangle.BOX);
    return cell;
  }

  /**
   * This class is used to set the orientation of a page in a PDF file.
   * 
   * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
   * @version 1.0
   */
  private static class Rotate extends PdfPageEventHelper {

    private final PdfNumber orientation = PdfPage.PORTRAIT;

    @Override
    public void onStartPage(final PdfWriter writer, final Document document) {
      writer.addPageDictEntry(PdfName.ROTATE, orientation);
    }
  }

}

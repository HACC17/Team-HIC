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

public final class PdfUtils {

  public static final float PIE_CHART_IMAGE_SCALE_PERCENT = 30.0f;
  public static final float LINE_CHART_IMAGE_SCALE_PERCENT = 19.0f;
  public static final float LOCATIONS_PIE_CHART_IMAGE_SCALE_PERCENT = 80.0f;

  private PdfUtils() {
  }

  public static void createPdfFile(Document document, byte[] decodedBytes, OutputStream os,
      float imageScalePercent, String tableHeading, String[] columnHeadings, String[] columnOneData,
      String[] columnTwoData, boolean isFiscal) {
    try {
      PdfWriter writer = PdfWriter.getInstance(document, os);
      writer.setPageEvent(new Rotate());
      document.open();
      Image image = Image.getInstance(decodedBytes);
      image.scalePercent(imageScalePercent);
      document.add(image);
      document.add(createTable(writer, tableHeading, columnHeadings, columnOneData, columnTwoData,
          isFiscal));
      document.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static PdfPTable createTable(PdfWriter writer, String tableHeading,
      String[] columnHeadings, String[] columnOneData, String[] columnTwoData, boolean isFiscal)
      throws DocumentException {
    PdfPTable table = new PdfPTable(2);
    table.setTotalWidth(new float[] { 250, 250 });
    table.setLockedWidth(true);
    table.setSpacingBefore(20.0f);

    Font font = new Font();
    font.setColor(BaseColor.WHITE);
    PdfPCell cell = new PdfPCell(new Phrase(tableHeading, font));
    cell.setFixedHeight(20);
    cell.setColspan(2);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setBorder(Rectangle.BOX);
    cell.setBackgroundColor(new BaseColor(45, 65, 84));
    table.addCell(cell);
    // header row
    for (String heading : columnHeadings) {
      cell = new PdfPCell(new Phrase(heading, font));
      cell.setFixedHeight(20);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBorder(Rectangle.BOX);
      cell.setBackgroundColor(new BaseColor(45, 65, 84));
      table.addCell(cell);
    }
    for (int idx = 0; idx < Math.max(columnOneData.length, columnTwoData.length); idx++) {
      String data = "";
      if (idx < columnOneData.length) {
        data = columnOneData[idx];
      }
      cell = new PdfPCell(new Phrase(WordUtils.capitalizeFully(data)));
      cell.setFixedHeight(20);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBorder(Rectangle.BOX);
      table.addCell(cell);
      data = "";
      if (idx < columnTwoData.length) {
        data = columnTwoData[idx];
      }
      if (!data.isEmpty()) {
        int num = Integer.parseInt(data);
        if (isFiscal) {
          data = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(num);
        }
        else {
          data = NumberFormat.getNumberInstance(new Locale("en", "US")).format(num);
        }
      }
      cell = new PdfPCell(new Phrase(WordUtils.capitalizeFully(data)));
      cell.setFixedHeight(20);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBorder(Rectangle.BOX);
      table.addCell(cell);
    }

    return table;
  }

  private static class Rotate extends PdfPageEventHelper {

    protected PdfNumber orientation = PdfPage.PORTRAIT;

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
      writer.addPageDictEntry(PdfName.ROTATE, orientation);
    }
  }

}

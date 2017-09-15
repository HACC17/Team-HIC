package gov.ehawaii.hacc.repositories.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 * This class represents a series on a time series chart.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Data
public final class TimeSeries implements Serializable {

  private static final long serialVersionUID = 1L;

  private String seriesName;
  private int startYear;
  private int endYear;
  private List<TimeSeriesDataPoint> points = new ArrayList<>();

  /**
   * Fills in the missing points in the series with zeroes. For example, if the start year is 2011
   * and the end year is 2017, given the following list:<br />
   * <br />
   * 
   * <code>[ { 2013: 2000 }, { 2015: 3000 } ]</code><br />
   * <br />
   * 
   * after this method returns, the above list will now be:<br />
   * <br />
   * 
   * <code>[ { 2011: 0 }, { 2012: 0 }, { 2013: 2000 }, { 2014: 0 }, { 2015: 3000 }, { 2016: 0 }, { 2017: 0 } ]</code>
   */
  public void fillInMissingData() {
    List<TimeSeriesDataPoint> temp = new ArrayList<>();
    for (int year = startYear; year <= endYear; year++) {
      temp.add(new TimeSeriesDataPoint(year));
    }
    for (TimeSeriesDataPoint point : points) {
      int index = temp.indexOf(point);
      if (index == -1) {
        temp.add(point);
      }
      else {
        temp.remove(index);
        temp.add(index, point);
      }
    }
    points = new ArrayList<>(temp);
    Collections.sort(points, (point1, point2) -> Integer.compare(point1.year, point2.year));
  }

  @Override
  public String toString() {
    return seriesName + "=" + points;
  }

  /**
   * This class represents a point on a time series chart.
   * 
   * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
   * @version 1.0
   */
  @Data
  public static final class TimeSeriesDataPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    private int year;
    private long value;

    /**
     * Constructs a new {@link TimeSeriesDataPoint} with the given year and no value.
     * 
     * @param year The year.
     */
    public TimeSeriesDataPoint(final int year) {
      this.year = year;
      value = 0;
    }

    /**
     * Constructs a new {@link TimeSeriesDataPoint} with the given year and value.
     * 
     * @param year The year.
     * @param value The value.
     */
    public TimeSeriesDataPoint(final int year, final long value) {
      this.year = year;
      this.value = value;
    }

    /**
     * We only care about the year, so only check if the given object's year is the same as this
     * one's.
     * 
     * @param object The object with which to compare.
     * @return <code>true</code> if the two years are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object object) {
      if (!(object instanceof TimeSeriesDataPoint)) {
        return false;
      }
      return ((TimeSeriesDataPoint) object).year == year;
    }

    @Override
    public int hashCode() {
      return year;
    }

    @Override
    public String toString() {
      return "{" + year + ": " + value + "}";
    }
  }

}

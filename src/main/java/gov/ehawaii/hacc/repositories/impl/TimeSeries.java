package gov.ehawaii.hacc.repositories.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public final class TimeSeries implements Serializable {

  private static final long serialVersionUID = 1L;

  private String seriesName;
  private int startYear;
  private int endYear;
  private List<TimeSeriesDataPoint> points = new ArrayList<>();

  public final void addPoint(final TimeSeriesDataPoint point) {
    points.add(point);
  }

  public final void fillInMissingData() {
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

  @Data
  public static final class TimeSeriesDataPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    private int year;
    private long value;

    public TimeSeriesDataPoint(int year) {
      this.year = year;
      value = 0;
    }

    public TimeSeriesDataPoint(int year, long value) {
      this.year = year;
      this.value = value;
    }

    @Override
    public boolean equals(Object object) {
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

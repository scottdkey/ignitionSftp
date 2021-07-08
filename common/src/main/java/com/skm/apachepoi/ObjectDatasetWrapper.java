package com.skm.apachepoi;

import com.inductiveautomation.ignition.common.Dataset;
import com.inductiveautomation.reporting.common.api.QueryResults;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ObjectDatasetWrapper implements Iterable<ObjectDatasetWrapper.Row> {
  private final QueryResults q;
  private final Dataset d;

  public ObjectDatasetWrapper(QueryResults q) {
    this(q, null);
  }

  public ObjectDatasetWrapper(Dataset d) {
    this(null, d);
  }

  public ObjectDatasetWrapper(QueryResults q, Dataset d) {
    this.q = q;
    this.d = d;
  }

  public int getSize() {
    if (q != null) {
      return q.size();
    }

    if (d != null) {
      return d.getRowCount();
    }

    return 0;
  }

  public Object get(int index, String key) {
    if (q != null) {
      return q.get(index).getKeyValue(key);
    }

    if (d != null) {
      return d.getValueAt(index, key);
    }

    return null;
  }

  public Row get(int index) {
    return new Row(index);
  }

  @NotNull
  @Override
  public Iterator<Row> iterator() {
    return new ResultsIterator();
  }

  @Override
  public void forEach(Consumer<? super Row> action) {}

  @Override
  public Spliterator<Row> spliterator() {
    return null;
  }

  class ResultsIterator implements Iterator<Row> {

    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < getSize();
    }

    @Override
    public Row next() {
      return get(index++);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public class Row {
    private final int rowIndex;

    public Row(int rowIndex) {
      this.rowIndex = rowIndex;
    }

    public Object getKeyValue(String key) {
      return get(rowIndex, key);
    }

    public Object getKeyValue(String key, Object defaultValue) {
      Object ret = get(rowIndex, key);
      if (ret == null) {
        return defaultValue;
      }

      return ret;
    }

    public void setKeyValue(String key, XSSFSheet sheet, Integer rowIdx, Integer column) {
      Object ret = get(rowIndex, key);
      if (ret == null) {
        return;
      }
      Double retD = (Double) ret;
      sheet.getRow(rowIdx).getCell(column).setCellValue(retD);
    }
  }
}

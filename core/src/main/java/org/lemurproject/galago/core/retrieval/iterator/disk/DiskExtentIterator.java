// BSD License (http://lemurproject.org/galago-license)
package org.lemurproject.galago.core.retrieval.iterator.disk;

import java.io.IOException;
import java.util.Collections;
import org.lemurproject.galago.core.index.disk.PositionIndexExtentSource;
import org.lemurproject.galago.core.index.source.ExtentSource;
import org.lemurproject.galago.core.index.stats.NodeAggregateIterator;
import org.lemurproject.galago.core.index.stats.NodeStatistics;
import org.lemurproject.galago.core.retrieval.iterator.ExtentIterator;
import org.lemurproject.galago.core.retrieval.query.AnnotatedNode;
import org.lemurproject.galago.core.util.ExtentArray;

/**
 *
 * @author jfoley
 */
public class DiskExtentIterator extends SourceIterator implements NodeAggregateIterator, ExtentIterator {

  ExtentSource extentSrc;
  
  public DiskExtentIterator(ExtentSource src) throws IOException {
    super(src);
    extentSrc = src;
  }
  
  @Override
  public String getValueString() throws IOException {
    StringBuilder builder = new StringBuilder();
    builder.append(getKeyString());
    builder.append(",");
    builder.append(currentCandidate());
    ExtentArray e = extents();
    for (int i = 0; i < e.size(); ++i) {
      builder.append(",");
      builder.append(e.begin(i));
    }
    return builder.toString();
  }

  @Override
  public AnnotatedNode getAnnotatedNode() throws IOException {
    String type = "extents";
    String className = this.getClass().getSimpleName();
    String parameters = this.getKeyString();
    int document = currentCandidate();
    boolean atCandidate = hasMatch(this.context.document);
    String returnValue = extents().toString();
    return new AnnotatedNode(type, className, parameters, document, atCandidate, returnValue, Collections.EMPTY_LIST);
  }

  @Override
  public NodeStatistics getStatistics() {
    return extentSrc.getStatistics();
  }

  @Override
  public int count() {   
    return (int) extentSrc.count(context.document);
  }

  @Override
  public ExtentArray extents() {
    return extentSrc.extents(context.document);
  }

  @Override
  public ExtentArray getData() {
    return extents();
  }
}
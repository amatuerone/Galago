// BSD License (http://lemurproject.org/galago-license)
package org.lemurproject.galago.core.retrieval.iterator;

import org.lemurproject.galago.core.retrieval.processing.ScoringContext;
import org.lemurproject.galago.core.retrieval.query.AnnotatedNode;

import java.io.IOException;

/**
 * This is an interface that represents any kind of
 *  iterator over an inverted list or query operator.
 *  
 * This class is for iteration across document ids, 
 *  as in a document-ordered inverted index.
 * 
 * Since iteration operation is a bit complicated, an example:
 * 
 *********************************** 
 *  ScoringContext sc = itr.getContext();
 *  while( ! itr.isDone() ){ 
 * 
 *    int doc = itr.currentCandidate();
 *    sc.setDocument(doc);
 * 
 *    if( itr.hasMatch()) {
 *      itr.prepareToEvaluate();
 * 
 *      // iterator tree should now point at useful information, 
 *      // e.g calls for counts or scores can go here:
 *      //    itr.score(doc), itr.count(doc), etc
 * 
 *    }
 *    itr.movePast();
 *  }
 * *******************************
 * 
 * @author sjh
 * @author jfoley
 */
public interface BaseIterator extends Comparable<BaseIterator> {

  /**
   * returns the iterator to the first candidate
   */
  void reset() throws IOException;

  /**
   * returns the current document id as a candidate
   * 
   * Specical case:
   *  if isDone() == true 
   *   return Long.MAX_VALUE
   */
  long currentCandidate();

  /**
   * return true if the iterator has no more candidates
   */
  boolean isDone();

  /**
   * Moves to the next candidate
   * 
   * Implementing iterators should call next on children iterators carefully:
   * 
   * for each child:
   *   if (hasAllCandidates() || !child.hasAllCandidates())
   *     child.movePast()
   *  
   * this avoids making small (unnecessary) jumps for iterators that have all candidates
   * 
   */
  void movePast(long identifier) throws IOException;

  /**
   * Moves the iterator to the specified candidate
   * 
   * Unlike the 'movePast' function this should move all iterators.
   * Even where 'hasAllCandidates' is true.
   */
  void syncTo(long identifier) throws IOException;
  
  /**
   * returns true if the iterator is at this candidate,
   * and can return a non-background value.
   * 
   * Often implemented:
   *  return !isDone() && currentCandidate() == identifier;
   * 
   * @param context
   */
  boolean hasMatch(ScoringContext context);

  /**
   * returns true if the iterator has data for ALL candidates
   *  - e.g. priors, lengths, names.
   * 
   * These iterators are assumed to provide supporting information
   *  for the current document to parent iterators. They should not
   *  guide the tree's iteration (e.g. by stopping at every document).
   *  
   */
  boolean hasAllCandidates();

  /**
   * Returns an over estimate of the total entries in the iterator
   */
  long totalEntries();

  /**
   * Returns a string representation of the current candidate + value
   *  Useful for dump index/iterator functions
   */
  String getValueString(ScoringContext sc) throws IOException;

  /**
   * Returns an AnnotatedNode representation of the current state of this iterator
   *  Useful for debugging a query model
   */
  AnnotatedNode getAnnotatedNode(ScoringContext sc) throws IOException;
}

// BSD License (http://lemurproject.org/galago-license)
package org.lemurproject.galago.core.tokenize;

import java.util.Arrays;
import org.lemurproject.galago.core.parse.Document;
import org.lemurproject.galago.tupleflow.FakeParameters;
import org.lemurproject.galago.utility.Parameters;
import org.lemurproject.galago.tupleflow.TupleFlowParameters;

/**
 *
 * @author jfoley
 */
public class NewlineTokenizer extends Tokenizer {
  
  public NewlineTokenizer(TupleFlowParameters tfp) {
    super(tfp);
  }
  public NewlineTokenizer() {
    super(new FakeParameters(Parameters.create()));
  }

  @Override
  public void tokenize(Document input) {
    input.terms = Arrays.asList(input.text.split("\n"));
  }
}

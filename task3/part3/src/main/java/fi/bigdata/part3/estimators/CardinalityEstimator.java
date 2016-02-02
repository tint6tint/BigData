package fi.bigdata.part3.estimators;

import java.io.IOException;

/**
 * Created by kjanowsk on 13.12.2014.
 */
public interface CardinalityEstimator {

    int estimateUniqueValues() throws IOException;
}

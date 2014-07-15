package spiceRetryPolicies;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * Created by anhhnguyen on 7/15/2014.
 */
public class CustomRetryPolicy implements RetryPolicy {
    @Override
    public int getRetryCount() {
        return 0;
    }

    @Override
    public void retry(SpiceException e) {

    }

    @Override
    public long getDelayBeforeRetry() {
        return 0;
    }
}

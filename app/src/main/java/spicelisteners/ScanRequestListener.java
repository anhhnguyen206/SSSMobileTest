package spicelisteners;

import android.content.Intent;

import com.example.anhhnguyen.myapplication.SSSActivity;
import com.example.anhhnguyen.myapplication.ScanResultActivity;
import com.example.anhhnguyen.myapplication.util.ExceptionUtils;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by anhhnguyen on 7/15/2014.
 */
public class ScanRequestListener implements RequestListener<String> {

    private SSSActivity requestActivity;

    public ScanRequestListener(SSSActivity sssActivity){
        super();
        requestActivity = sssActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
// Default error setup
        String error = ExceptionUtils.getErrorMessage(spiceException);
        requestActivity.setProgressBarIndeterminateVisibility(false);

        Intent intent = new Intent(requestActivity, ScanResultActivity.class);
        intent.putExtra("type", "invalid");
        intent.putExtra("message", error);
        requestActivity.startActivity(intent);
    }

    @Override
    public void onRequestSuccess(String s) {
        requestActivity.setProgressBarIndeterminateVisibility(false);

        Intent intent = new Intent(requestActivity, ScanResultActivity.class);
        intent.putExtra("type", "valid");
        intent.putExtra("message", "Valid");
        requestActivity.startActivity(intent);
    }
}

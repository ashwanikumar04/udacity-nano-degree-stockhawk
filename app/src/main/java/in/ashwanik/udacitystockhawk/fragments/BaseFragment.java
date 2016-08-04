package in.ashwanik.udacitystockhawk.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import in.ashwanik.retroclient.entities.ErrorData;
import in.ashwanik.retroclient.utils.Helpers;
import in.ashwanik.udacitystockhawk.activity.MainActivity;
import in.ashwanik.udacitystockhawk.common.BaseApplication;

/**
 * A placeholder fragment containing a simple view.
 */
public class BaseFragment extends Fragment {

    public void showSnackBar(String message) {
        MainActivity baseActivity = (MainActivity) getActivity();
        baseActivity.showSnackBar(message, baseActivity.getCoordinatorLayout());
    }

    public void showSnackBar(ErrorData errorData) {
        MainActivity baseActivity = (MainActivity) getActivity();
        baseActivity.showSnackBar(errorData, baseActivity.getCoordinatorLayout());
    }

    public Snackbar showSnackBar(View.OnClickListener onClickListener) {
        MainActivity baseActivity = (MainActivity) getActivity();
        return baseActivity.showNetworkSnackBar(baseActivity.getCoordinatorLayout(), onClickListener);
    }

    public void logDebug(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Helpers.d(BaseApplication.LOG, message);
    }
}

package io.akwa.traquer.emptrack.common.login;

import android.view.View;

import io.akwa.traquer.emptrack.R;
import io.akwa.traquer.emptrack.forgotPassword.DefaultForgotPasswordActivity;


public class ForgotPasswordActivity extends DefaultForgotPasswordActivity {
    @Override
    public void setAppName() {
        mAppName.setVisibility(View.VISIBLE);
        mAppName.setImageResource(R.drawable.rep_logo);

    }

}

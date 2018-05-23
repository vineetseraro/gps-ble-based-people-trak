package io.akwa.traquer.emptrack.ui.editProfile;

import android.content.Intent;

import io.akwa.traquer.emptrack.common.login.LoginActivity;


public class EditProfileActivity extends DefaultEditProfileActivity{

    @Override
    public Intent getLoginIntent() {
        return new Intent(this, LoginActivity.class);
    }
}

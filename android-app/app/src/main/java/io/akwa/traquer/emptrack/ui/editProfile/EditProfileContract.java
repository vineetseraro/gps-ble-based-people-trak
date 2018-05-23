package io.akwa.traquer.emptrack.ui.editProfile;


import java.io.File;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.model.CountryApiResponse;

public interface EditProfileContract {
    interface View {
        void onEditProfileDone(ApiResponseModel loginResponse, NicbitException e);
        void onCountriesDone(CountryApiResponse responseModel, NicbitException e);

    }
    interface UserActionsListener {
        void doEditProfile(Integer isImageRemove, String city, String password, String firstName, String lastName, String mobileNo, String countryCode, File profileImage);
        void doCountries();
        void getProfile();
    }
}

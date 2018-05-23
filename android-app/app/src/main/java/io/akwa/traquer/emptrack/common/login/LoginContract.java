package io.akwa.traquer.emptrack.common.login;

public interface LoginContract {

    interface View {
        void onUserAuthenticationDone(String user, String password, String message);
    }

    interface UserActionsListener {

        void getUserAuthentication(String email, String password);
    }
}

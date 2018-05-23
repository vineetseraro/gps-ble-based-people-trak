package io.akwa.traquer.emptrack.common.network;


import com.google.gson.JsonObject;

import io.akwa.traquer.emptrack.common.geofence.GeofenceApiResponse;
import io.akwa.traquer.emptrack.common.updatedevice.DeviceInfo;
import io.akwa.traquer.emptrack.model.ApiBaseResponse;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.model.CognitoEditProfileRequest;
import io.akwa.traquer.emptrack.model.CountryApiResponse;
import io.akwa.traquer.emptrack.model.EditProfileRequest;
import io.akwa.traquer.emptrack.model.NotificationApiResponse;
import io.akwa.traquer.emptrack.model.PhoneStatusRequest;
import io.akwa.traquer.emptrack.model.RemoveNotificationRequest;
import io.akwa.traquer.emptrack.model.SaveConsumerGcm;
import io.akwa.traquer.emptrack.model.UpdateSettingsRequest;
import io.akwa.traquer.emptrack.ui.contactsettings.model.ContactSettingRequest;
import io.akwa.traquer.emptrack.ui.contactsettings.model.GetContactSettingsResponse;
import io.akwa.traquer.emptrack.ui.home.model.EmpDashboardResponse;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailResponse;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailUpdateRequest;
import io.akwa.traquer.emptrack.ui.timeSheetDetail.model.TimeSheetDetailResponse;
import io.akwa.traquer.emptrack.ui.timeSheetReport.model.TimeSheetReportResponse;
import io.akwa.trakit.rulehandler.GetTrackingSettingResponse;
import io.akwa.traquer.emptrack.ui.trackingsetting.model.UpdateTrackingSetting;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {




    @POST("things/{environment}/devices")
    Call<ApiBaseResponse> registerDevice(@Path("environment") String environment, @Body DeviceInfo deviceInfor);




    @PUT("things/{environment}/device/link")
    Call<ApiResponseModel> linkDevice(
            @Path("environment") String environment,
            @Body JsonObject appCode
    );


    @GET("locations/{environment}/list/")
    Call<GeofenceApiResponse> getGeofences(
            @Path("environment") String environment
    );
    @PUT("things/{environment}/device/unlink")
    Call<ApiResponseModel> unLinkDevice(
            @Path("environment") String environment,
            @Body JsonObject appCode
    );



    @GET("getCasesHistory")
    Call<ApiResponseModel> getCasesHistory(
            @Header("sid") String token,
            @Query("sortBy") String sortBy, @Query("sortOrder") String sortOrder
    );


    @GET("orders/{environment}/searchOrderAndProducts")
    Call<ApiResponseModel> searchCases(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("query") String query
    );

    @GET("settings/{environment}")
    Call<ApiResponseModel> getSettings(
            @Path("environment") String environment,
            @Header("sid") String token
    );
    @GET("settings/{environment}/trackingHours")
    Call<GetTrackingSettingResponse> getTrackingSetting(
            @Path("environment") String environment,
            @Header("sid") String token
    );
    @PUT("settings/{environment}/trackingHours")
    Call<GetTrackingSettingResponse> updateTrackingSetting(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body UpdateTrackingSetting updateSettingsRequest
    );

    @PUT("settings/{environment}/emergencyContacts")
    Call<GetContactSettingsResponse> updateContactSettings(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body ContactSettingRequest updateSettingsRequest
    );

    @GET("settings/{environment}/emergencyContacts")
    Call<GetContactSettingsResponse> getContactSetting(
            @Path("environment") String environment,
            @Header("sid") String token
    );


    @PUT("settings/{environment}")
    Call<ApiResponseModel> updateSettings(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body UpdateSettingsRequest updateSettingsRequest
    );


    @GET("issues/{environment}")
    Call<ApiResponseModel> getIssueComments(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("caseNo") String caseNo,
            @Query("shippingNo") String shippingNo,
            @Query("issueId") String issueId
    );


    @GET("getCaseIssues")
    Call<ApiResponseModel> getCaseIssue(
            @Header("sid") String token,
            @Query("caseNo") String caseNo
    );

    @GET("notifications/{environment}")
    Call<NotificationApiResponse> getNotifications(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("mobile") int mobile
    );



    @POST("saveConsumerGcm")
    Call<ApiResponseModel> consumerGcm(
            @Header("sid") String token,
            @Body SaveConsumerGcm gcmId
    );

    @POST("updateProfile")
    Call<ApiResponseModel> editProfile(
            @Header("sid") String token,
            @Body EditProfileRequest timeZone
    );

    @GET("common/{environment}/countries")
    Call<CountryApiResponse> getCountries(
            @Path("environment") String environment
    );

    @GET("getItems")
    Call<ApiResponseModel> getItems(
            @Header("sid") String token,
            @Query("query") String query
    );

    @GET("searchItems")
    Call<ApiResponseModel> getSearchItems(
            @Header("sid") String token,
            @Query("itemId") String itemId
    );

    @GET("orders/{environment}/salesrep/products")
    Call<ApiResponseModel> getItemDetails(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("caseNo") String caseNo,
            @Query("skuId") String skuId

    );

    @GET("getCaseHistoryDetails")
    Call<ApiResponseModel> getCaseHistoryDetails(
            @Header("sid") String token,
            @Query("caseNo") String caseNo
    );

    @Multipart
    @POST("updateProfile")
    Call<ApiResponseModel> updateProfile(
            @Header("sid") String token,
            @Part("city") RequestBody city,
            @Part("password") RequestBody password,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("mobile") RequestBody mobileNo,
            @Part("countryCode") RequestBody countryCode,
            @Part("deleteProfileImage") RequestBody deleteProfileImage,
            @Part MultipartBody.Part file);




    @PUT("users/{environment}/profile")
    Call<ApiResponseModel> updateProfileCognito(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body CognitoEditProfileRequest reportIssueRequest
    );


    @GET("getCaseItemComments")
    Call<ApiResponseModel> getCaseItemComments(
            @Header("sid") String token,
            @Query("caseNo") String caseNo,
            @Query("skuId") String skuId
    );

    @GET("getTrackingConfigurations")
    Call<ApiResponseModel> getTrackingConfigurations(
            @Header("sid") String token,
            @Query("app") String appName

    );

    @GET("orders/{environment}/salesrep/completed")
    Call<ApiResponseModel> getCompletedCases(
            @Path("environment") String environment,
            @Header("sid") String token

    );

    @PUT("things/{environment}/deviceStatus")
    Call<ApiResponseModel> phoneStatus(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body PhoneStatusRequest phoneStatusRequest
    );
///
    @GET("locations/{environment}/inventory")
    Call<ApiResponseModel> searchNearLocations(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("locationId") int locationId
    );


    @POST("saveDeviceInformation")
    Call<ApiResponseModel> updateDevice(@Body DeviceInfo deviceInfor);



    @GET("getContentPage")
    Call<ApiResponseModel> getcontent(
            @Header("sid") String token,
            @Query("ctype") String page
    );

    @GET("emptrakapp/{environment}/dashboard")
    Call<EmpDashboardResponse> getDashboardData(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("date") String date
    );

    @GET("emptrakapp/{environment}/inoutdetails")
    Call<TimeSheetDetailResponse> getTimeSheetDetail(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("date") String date
    );

    @GET("emptrakapp/{environment}/inouthistory")
    Call<TimeSheetReportResponse> getTimeSheetReport(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate
    );

    @GET("tasks/{environment}/{taskId}")
    Call<TaskDetailResponse> getTaskDetail(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Path("taskId") String taskId

    );

    @PUT("notifications/{environment}/archive")
    Call<NotificationApiResponse> removeNotifications(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Body RemoveNotificationRequest removeNotificationRequest
    );

    @PUT("tasks/{environment}/{taskId}")
    Call<ApiResponseModel> updateTaskDetail(
            @Path("environment") String environment,
            @Header("sid") String token,
            @Path("taskId") String taskId,
            @Query("mobile") int isMobile,
            @Body TaskDetailUpdateRequest taskDetailUpdateRequest
    );

}
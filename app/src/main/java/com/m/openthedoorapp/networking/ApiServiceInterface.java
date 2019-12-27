package com.m.openthedoorapp.networking;

import com.m.openthedoorapp.model.AboutResponseModel;
import com.m.openthedoorapp.model.AddNoteModel;
import com.m.openthedoorapp.model.AddReviewResponseModel;
import com.m.openthedoorapp.model.CancelServiceModel;
import com.m.openthedoorapp.model.CanceledHistoryModel;
import com.m.openthedoorapp.model.ChangePasswordModel;
import com.m.openthedoorapp.model.CompletedHistoryModel;
import com.m.openthedoorapp.model.ConfirmationResponseModel;
import com.m.openthedoorapp.model.ContactUsModel;
import com.m.openthedoorapp.model.CurrentHistoryModel;
import com.m.openthedoorapp.model.DeleteNotificatiomModel;
import com.m.openthedoorapp.model.EditUserServiceModel;
import com.m.openthedoorapp.model.ForgetPassModel;
import com.m.openthedoorapp.model.InProcessHistoryModel;
import com.m.openthedoorapp.model.RegisterCodeModel;
import com.m.openthedoorapp.model.RegisterationModel;
import com.m.openthedoorapp.model.RegsiterPhoneModel;
import com.m.openthedoorapp.model.ReportAproblemModel;
import com.m.openthedoorapp.model.ReviewsResponseModel;
import com.m.openthedoorapp.model.ScheduledHistoryModel;
import com.m.openthedoorapp.model.SendCodeModel;
import com.m.openthedoorapp.model.SendCopounResponseModel;
import com.m.openthedoorapp.model.ServiceProviderModel;
import com.m.openthedoorapp.model.ServicesResponseModel;
import com.m.openthedoorapp.model.SetNewPasswordModel;
import com.m.openthedoorapp.model.SetPasswordModel;
import com.m.openthedoorapp.model.UserLoginModel;
import com.m.openthedoorapp.model.UserNotificationResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiServiceInterface {

    // ---------------- LogIn -------------------------------
    @POST("api/login")
    Call<UserLoginModel> userLogin(@Query("phone") String phone,
                                   @Query("password") String password
    );


    // ---------------- Registeration-------------------------------
    @Multipart
    @POST("api/register")
    Call<RegisterationModel> userSign(@Part("name") RequestBody name,
                                      @Query("email") String email,
                                      @Query("phone") String phone,
                                      @Query("password") String password,
                                      @Query("password_confirmation") String password_confirmation,
                                      @Part MultipartBody.Part user_image
    );


    // ----------------- Update Profile -----------------------------
    @Multipart
    @POST("api/updateprofile")
    Call<UserLoginModel> updateProfileData(@Part("name") RequestBody name,
                                           @Query("email") String email,
                                           @Query("phone") String phone,
                                           @Query("api_token") String api_token,
                                           @Part MultipartBody.Part user_image);


    // ---------------- Change Password -------------------------------
    @POST("api/changepassword")
    Call<ChangePasswordModel> changePassword(@Query("current_password") String current_password,
                                             @Query("password") String password,
                                             @Query("password_confirmation") String password_confirmation,
                                             @Query("api_token") String api_token
    );


    // ---------------- Regsiter Code ------------------------------
    @POST("api/sendcode")
    Call<RegsiterPhoneModel> registerPhone(@Query("phone") String phone);

    // ---------------- Regsiter Code ------------------------------
    @POST("api/checkcode")
    Call<RegisterCodeModel> registerCode(@Query("code") String code);

    // ---------------- Regsiter Password ------------------------------
    @POST("api/setpassword")
    Call<SetPasswordModel> registerSetPassword(@Query("user_id") int user_id,
                                               @Query("password") String password,
                                               @Query("password_confirmation") String password_confirmation);


    // ---------------- Update Data  ------------------------------
    @POST("api/updateprofile")
    Call<SetPasswordModel> updateProfile(@Query("user_id") int user_id,
                                               @Query("name") String name,
                                               @Query("email") String email,
                                               @Query("phone") String phone,
                                               @Query("api_token") String api_token
    );



    // ---------------- Forget Paassword-------------------------------
    @POST("api/usercheckmobile?")
    Call<ForgetPassModel> sendPhone(@Query("mobile_number") String mobile_number);

    // ---------------- Send Code -------------------------------
    @POST("api/usercheckcode")
    Call<SendCodeModel> sendCode(@Query("code") String code);

    // ---------------- Set New Password -------------------------------
    @POST("api/userchangepassword")
    Call<SetNewPasswordModel> setNewPass(@Query("password") String password,
                                         @Query("password_confirmation") String password_confirmation,
                                         @Query("user_id") String user_id);


    // ----------------- About Us ---------------------------------------
    @GET("api/aboutus")
    Call<AboutResponseModel> aboutUs();


    // ----------------- About Company ---------------------------------------
    @GET("api/appinfo")
    Call<AboutResponseModel> aboutCompany();


    // ----------------- Send Promo Code ---------------------------------------
    @POST("api/adddcoupon")
    Call<SendCopounResponseModel> sendCoupon(@Query("user_id") int user_id,
                                             @Query("coupon") String coupon,
                                             @Query("api_token") String api_token);


    // ----------------- User Notification ------------------------------
    @GET("api/usernotfication")
    Call<UserNotificationResponse> getUserNotification(@QueryMap Map<String, Object> map);


    // ---------------- Delete Notification ------------------------------
    @POST("api/deleteusernotfication")
    Call<DeleteNotificatiomModel> deleteNotification(@Query("not_id") int not_id,
                                                     @Query("api_token") String api_token
    );


    // ----------------- Get Services--------------------------------------
    @GET("api/getuserservices")
    Call<ServicesResponseModel> getUserServices(@Query("page") int page,
                                                @Query("limit") int limit,
                                                @Query("api_token") String api_token);


    // ----------------- Get Services Providers ---------------------------
    @GET("api/getserviceprovider")
    Call<ServiceProviderModel> getServiceProviders(@Query("page") int page,
                                                   @Query("limit") int limit,
                                                   @Query("service_id") int service_id,
                                                   @Query("user_long") double user_long,
                                                   @Query("user_lat") double user_lat,
                                                   @Query("api_token") String api_token,
                                                   @Query("sort") String sort);


    // ----------------- Confirm Service ---------------------------
    @POST("api/adduserservice")
    Call<ConfirmationResponseModel> confirmService(@Query("provider_minutes_to_arrive") int provider_minutes_to_arrive,
                                                   @Query("provider_hour_to_finish") int provider_hour_to_finish,
                                                   @Query("service_id") int service_id,
                                                   @Query("user_id") int user_id,
                                                   @Query("provider_id") int provider_id,
                                                   @Query("status") String status,
                                                   @Query("schedule_time") String schedule_time,
                                                   @Query("notes") String notes,
                                                   @Query("bounce") String bounce,
                                                   @Query("api_token") String api_token);


    // ----------------- Cancel Service ---------------------------
    @POST("api/cancelservice")
    Call<ServiceProviderModel> cancelService(@Query("user_service_id") int user_service_id,
                                             @Query("user_id") int user_id,
                                             @Query("reson_for_cancel") String reson_for_cancel,
                                             @Query("api_token") String api_token);


    // ----------------- All Reviews ---------------------------------------
    @GET("api/getallreview")
    Call<ReviewsResponseModel> getAllReviews(@Query("page") int page,
                                             @Query("limit") int limit,
                                             @Query("api_token") String api_token);


    // ----------------- Add Review -----------------------------------------
    @GET("api/addreview")
    Call<AddReviewResponseModel> addReview(@Query("user_id") int user_id,
                                           @Query("provider_id") int provider_id,
                                           @Query("notes") String notes,
                                           @Query("rate") int rate,
                                           @Query("api_token") String api_token
    );


    // ----------------- Delete Review -----------------------------------------
    @POST("api/deletereview")
    Call<AddReviewResponseModel> deleteReview(@Query("review_id") int review_id,
                                              @Query("api_token") String api_token
    );


    // ----------------- Current History ----------------------------------------
    @GET("api/getuserservicehistory")
    Call<CurrentHistoryModel> currentHistory(@Query("page") int page,
                                             @Query("limit") int limit,
                                             @Query("user_id") int user_id,
                                             @Query("api_token") String api_token);


    // ----------------- inProgress History ----------------------------------------
    @GET("api/getuserservicehistory")
    Call<InProcessHistoryModel> inProcessHistory(@Query("page") int page,
                                                 @Query("limit") int limit,
                                                 @Query("user_id") int user_id,
                                                 @Query("api_token") String api_token);


    // ----------------- Completed History ----------------------------------------
    @GET("api/getuserservicehistory")
    Call<CompletedHistoryModel> completedHistory(@Query("page") int page,
                                                 @Query("limit") int limit,
                                                 @Query("user_id") int user_id,
                                                 @Query("api_token") String api_token);


    // ----------------- Cancled History ----------------------------------------
    @GET("api/getuserservicehistory")
    Call<CanceledHistoryModel> canceledHistory(@Query("page") int page,
                                               @Query("limit") int limit,
                                               @Query("user_id") int user_id,
                                               @Query("api_token") String api_token);


    // ----------------- Current History ----------------------------------------
    @GET("api/getuserservicehistory")
    Call<ScheduledHistoryModel> scheduledHistory(@Query("page") int page,
                                                 @Query("limit") int limit,
                                                 @Query("user_id") int user_id,
                                                 @Query("api_token") String api_token);


    // ------------------ Contact Us ----------------------------------------------
    @GET("api/appinfo")
    Call<ContactUsModel> getContactData();


    // ------------------ Report aProblem ----------------------------------------------
    @POST("api/sendproblemreport")
    Call<ReportAproblemModel> send_Problem(@Query("provider_id") int provider_id,
                                           @Query("user_id") int user_id,
                                           @Query("problem") String problem,
                                           @Query("api_token") String api_token);


    // ------------------ Save Note ----------------------------------------------
    @POST("api/sendprovidernotes")
    Call<AddNoteModel> save_Note(@Query("provider_id") int provider_id,
                                 @Query("user_id") int user_id,
                                 @Query("notes") String problem,
                                 @Query("api_token") String api_token);


    // ------------------ Cancel Service ----------------------------------------------
    @POST("api/cancelservice")
    Call<CancelServiceModel> cancel_Service(@Query("user_service_id") int user_service_id,
                                            @Query("user_id") int user_id,
                                            @Query("reson_for_cancel") String reson_for_cancel,
                                            @Query("api_token") String api_token);


    // ------------------ Edit User Service ----------------------------------------------
    @POST("api/adduserservice")
    Call<EditUserServiceModel> editUser_Service(@Query("provider_minutes_to_arrive") int provider_minutes_to_arrive,
                                                @Query("provider_hour_to_finish") int provider_hour_to_finish,
                                                @Query("service_id") int service_id,
                                                @Query("user_id") int user_id,
                                                @Query("provider_id") int provider_id,
                                                @Query("status") String status,
                                                @Query("api_token") String api_token,
                                                @Query("schedule_time") String schedule_time,
                                                @Query("notes") String notes,
                                                @Query("bounce") int bounce,
                                                @Query("payment_method") String payment_method,
                                                @Query("user_service_id") String user_service_id);

}
package com.softcodeinfotech.helpapp;


import com.softcodeinfotech.helpapp.addHelpPOJO.addHelpBean;
import com.softcodeinfotech.helpapp.allMessagePOJO.allMessageBean;
import com.softcodeinfotech.helpapp.allUsersPOJO.allUsersBean;
import com.softcodeinfotech.helpapp.beanresponse.GetforgotpassResponse;
import com.softcodeinfotech.helpapp.beanresponse.GetmobileverifyResponse;
import com.softcodeinfotech.helpapp.getFollowersPOJO.getFollowersBean;
import com.softcodeinfotech.helpapp.helpDataPOJO.helpDataBean;
import com.softcodeinfotech.helpapp.myHelpsPOJO.myHelpsBean;
import com.softcodeinfotech.helpapp.response.AadharUpdateResponse;
import com.softcodeinfotech.helpapp.response.GetAllHelperListResponse;
import com.softcodeinfotech.helpapp.response.GetCategoryResponse;
import com.softcodeinfotech.helpapp.response.GetIndividualUserResponse;
import com.softcodeinfotech.helpapp.response.PasswordUpdateResponse;
import com.softcodeinfotech.helpapp.response.ProfileResponse;
import com.softcodeinfotech.helpapp.response.SigninResponse;
import com.softcodeinfotech.helpapp.sendMessagePOJO.sendMessageBean;
import com.softcodeinfotech.helpapp.singleMessagePOJO.singleMessageBean;
import com.softcodeinfotech.helpapp.verifyPOJO.verifyBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

   /* @Multipart
    @POST("helpapp/emailverify.php")
    Call<EmailResponse> emailVerify(

            @Part("email") RequestBody email,
            @Part("otp") RequestBody otp,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );*/


    /* //not used to be removed
    @Multipart
    @POST("helpapp/profileupdate.php")
    Call<ProfileupdateResponse> saveProfile(
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part("age") RequestBody age,
            @Part("gender") RequestBody gender,
            @Part("mobile") RequestBody mobile,
            @Part("profilestatus") RequestBody profilestatus

    );*/

   /* @Multipart
    @POST("helpapp/user_signin.php")
    Call<SigninResponse> userlogin(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );*/


   /* @Multipart
    @POST("helpapp/profile_update.php")
    Call<ProfileResponse> profileUpdate(

            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part("age") RequestBody age,
            @Part("gender") RequestBody gender,
            @Part("mobile") RequestBody mobile,
            @Part("aadhar") RequestBody aadhar,
            @Part("address") RequestBody address,
            @Part("state") RequestBody state,
            @Part("pin") RequestBody pin
            // @Part("profilestatus") RequestBody profilestatus
    );*/

    //forgotpassword

    //image upload


    //Aadhar update
    @Multipart
    @POST("helpapp/aadharUpdate.php")
    Call<AadharUpdateResponse> aadharUpdate
    (
            @Part("email") RequestBody email,
            @Part("aadhar") RequestBody aadhar
    );

    //Password update

    @Multipart
    @POST("helpapp/passwordUpdate.php")
    Call<PasswordUpdateResponse> passwordUpdate(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );


    //get category

    @GET("elfee/api/getCategory.php")
    Call<GetCategoryResponse> getCategory();

    //get History

    //get all helper List
    @Multipart
    @POST("helpapp/get_all_helper_list.php")
    Call<GetAllHelperListResponse> getAllHelper(
            @Part("profilestatus")RequestBody profilestatus);

    //get indvidual user info to provide help

    @Multipart
    @POST("helpapp/get_individual_user_details.php")
    Call<GetIndividualUserResponse> getIndividualUserDetails(
            @Part("user_id") RequestBody user_id
    );

//===========================================================================================================
    //new code february

    //Registration using mobile

    @Multipart
    @POST("helpapp/api/mobileverify.php")
    Call<GetmobileverifyResponse> mobileVerify(
            @Part("mobile") RequestBody  mobile,
            @Part("otp") RequestBody otp,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    //forgot password
    @Multipart
    @POST("helpapp/api/forgotpassword.php")
    Call<GetforgotpassResponse> getPasswordOnMobile(
            @Part("mobile") RequestBody mobile
    );


    //login

    @Multipart
    @POST("elfee/api/mobile_login.php")
    Call<SigninResponse> userlogin(
            @Part("phone") RequestBody mobile
    );


    @Multipart
    @POST("helpapp/api/profile_update.php")
    Call<ProfileResponse> profileUpdate(

            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part("age") RequestBody age,
            @Part("gender") RequestBody gender,
            @Part("mobile") RequestBody mobile,
            @Part("aadhar") RequestBody aadhar,
            @Part("address") RequestBody address,
            @Part("state") RequestBody state,
            @Part("pin") RequestBody pin
            // @Part("profilestatus") RequestBody profilestatus
    );


    @Multipart
    @POST("helpapp/api/profile_update.php")
    Call<ProfileResponse> profile(

            @Part("email") String email,
            @Part("name") String name,
            @Part("age") String age,
            @Part("gender") String gender,
            @Part("mobile") String mobile,
            @Part("aadhar") String aadhar,
            @Part("address") String address,
            @Part("state") String state,
            @Part("pin") String pin
            // @Part("profilestatus") RequestBody profilestatus
    );


    @Multipart
    @POST("helpapp/all_message.php")
    Call<allMessageBean> allMessageList(
            @Part("userId") String userId
    );

    @Multipart
    @POST("helpapp/send_message.php")
    Call<sendMessageBean> sendMessage(
            @Part("userId") String userId,
            @Part("friendId") String friendId,
            @Part("message") String message
    );

    @Multipart
    @POST("helpapp/single_user_message.php")
    Call<singleMessageBean> singleChatList(
            @Part("userId") String userId,
            @Part("friendId") String friendId,
            @Part("chatId") String chatId
    );

    @Multipart
    @POST("elfee/api/verify_otp.php")
    Call<verifyBean> verifyOTP(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("elfee/api/social_login.php")
    Call<verifyBean> socialLogin(
            @Part("pid") String pid
    );

    @Multipart
    @POST("elfee/api/update_phone.php")
    Call<SigninResponse> updatePhone(
            @Part("phone") String phone,
            @Part("userId") String userId
    );

    @Multipart
    @POST("elfee/api/resend_otp.php")
    Call<SigninResponse> resendOTP(
            @Part("phone") String phone
    );

    @Multipart
    @POST("elfee/api/update_kyc.php")
    Call<verifyBean> updateKYC(
            @Part("userId") String userId,
            @Part("name") String name,
            @Part("dob") String dob,
            @Part("aadhar") String aadhar,
            @Part("address") String address,
            @Part("wphone") String wphone,
            @Part("g_name") String g_name,
            @Part("gphone") String gphone,
            @Part("profession") String profession,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4,
            @Part MultipartBody.Part file5
    );

    @Multipart
    @POST("elfee/api/update_profile.php")
    Call<verifyBean> editProfile(
            @Part("userId") String userId,
            @Part("name") String name,
            @Part("dob") String dob
    );

    @Multipart
    @POST("elfee/api/get_profile.php")
    Call<verifyBean> getProfile(
            @Part("userId") String userId
    );

    @Multipart
    @POST("elfee/api/update_profile_pic.php")
    Call<verifyBean> updateProfilePic(
            @Part("userId") String userId,
            @Part MultipartBody.Part file1
            );

    @Multipart
    @POST("elfee/api/add_help.php")
    Call<addHelpBean> addHelp(
            @Part("userId") String userId,
            @Part("catId") String catId,
            @Part("how_to") String how_to,
            @Part("need") String need,
            @Part("lat") String lat,
            @Part("lng") String lng,
            @Part("city") String city,
            @Part("address") String address,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4,
            @Part MultipartBody.Part file5,
            @Part MultipartBody.Part file6,
            @Part MultipartBody.Part file7,
            @Part MultipartBody.Part file8,
            @Part MultipartBody.Part file9,
            @Part MultipartBody.Part file10
    );

    @Multipart
    @POST("elfee/api/update_help.php")
    Call<addHelpBean> editHelp(
            @Part("helpId") String helpId,
            @Part("catId") String catId,
            @Part("how_to") String how_to,
            @Part("need") String need,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4,
            @Part MultipartBody.Part file5,
            @Part MultipartBody.Part file6,
            @Part MultipartBody.Part file7,
            @Part MultipartBody.Part file8,
            @Part MultipartBody.Part file9,
            @Part MultipartBody.Part file10
    );

    @Multipart
    @POST("elfee/api/complete_help.php")
    Call<addHelpBean> completeHelp(
            @Part("helpId") String helpId,
            @Part("name") String name,
            @Part("phone") String phone
    );


    @GET("elfee/api/getUsers.php")
    Call<allUsersBean> getUsers();

    @Multipart
    @POST("elfee/api/delete_help.php")
    Call<addHelpBean> deleteHelp(
            @Part("helpId") String helpId
    );

    @Multipart
    @POST("elfee/api/my_helps.php")
    Call<myHelpsBean> myHelps(
            @Part("userId") String userId
    );


    @Multipart
    @POST("elfee/api/getFollowers.php")
    Call<getFollowersBean> followers(
            @Part("userId") String userId
    );


    @Multipart
    @POST("elfee/api/all_helps.php")
    Call<myHelpsBean> allHelps(
            @Part("userId") String userId,
            @Part("state") String state,
            @Part("catId") String catId,
            @Part("lat") String lat,
            @Part("lng") String lng,
            @Part("radius") String radius
    );

    @Multipart
    @POST("elfee/api/completed_helps.php")
    Call<myHelpsBean> sompletedHelps(
            @Part("userId") String userId,
            @Part("state") String state,
            @Part("catId") String catId,
            @Part("lat") String lat,
            @Part("lng") String lng,
            @Part("radius") String radius
    );

    @Multipart
    @POST("elfee/api/help_data.php")
    Call<helpDataBean> helpData(
            @Part("userId") String userId,
            @Part("helpId") String helpId
    );

}

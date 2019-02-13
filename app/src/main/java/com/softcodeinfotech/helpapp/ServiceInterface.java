package com.softcodeinfotech.helpapp;


import com.facebook.internal.ImageResponse;
import com.softcodeinfotech.helpapp.beanresponse.AddHelpListResponse;
import com.softcodeinfotech.helpapp.beanresponse.GetforgotpassResponse;
import com.softcodeinfotech.helpapp.beanresponse.GetmobileverifyResponse;
import com.softcodeinfotech.helpapp.response.AadharUpdateResponse;
import com.softcodeinfotech.helpapp.response.EmailResponse;
import com.softcodeinfotech.helpapp.response.ForgotpassResponse;
import com.softcodeinfotech.helpapp.response.GetAllHelperListResponse;
import com.softcodeinfotech.helpapp.response.GetCategoryResponse;
import com.softcodeinfotech.helpapp.response.GetIndividualUserResponse;
import com.softcodeinfotech.helpapp.response.GethelplistResponse;
import com.softcodeinfotech.helpapp.response.HelpDataInsertResponse;
import com.softcodeinfotech.helpapp.response.HelpHistoryResponse;

import com.softcodeinfotech.helpapp.response.PasswordResponse;
import com.softcodeinfotech.helpapp.response.PasswordUpdateResponse;
import com.softcodeinfotech.helpapp.response.ProfileResponse;
import com.softcodeinfotech.helpapp.response.ProfileupdateResponse;
import com.softcodeinfotech.helpapp.response.SigninResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
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


    //not used to be removed
    @Multipart
    @POST("helpapp/savepassword.php")
    Call<PasswordResponse> savePassword(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

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

    @Multipart
    @POST("helpapp/forgotpassword.php")
    Call<ForgotpassResponse> forgotPassword(

            @Part("email") RequestBody email
    );

    //image upload
    @Multipart
    @POST("helpapp/image.php")
    Call<ImageResponse> uploadImage
    (@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
     @Part("name") RequestBody desc,
     @Part("email") RequestBody email);


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
    @Multipart
    @POST("helpapp/getCategory.php")
    Call<GetCategoryResponse> getCategory(
            @Part("securecode") RequestBody securecode
    );

    //helpData Insertion
    @Multipart
    @POST("helpapp/helpdataInsert.php")
    Call<HelpDataInsertResponse> helpDataInsert(
            @Part("user_id") RequestBody user_id,
            @Part("help_title") RequestBody help_title,
            @Part("help_description") RequestBody help_description,
            @Part("help_category_id") RequestBody help_category_id,
            @Part("state") RequestBody state
    );

    //get History

    @Multipart
    @POST("helpapp/help_history.php")
    Call<HelpHistoryResponse> getHelpHistory(
            @Part("user_id") RequestBody user_id
    );

    //get help List on main Activity
    @Multipart
    @POST("helpapp/get_help_list.php")
    Call<GethelplistResponse> getHelpLitstItem(
            @Part("state") RequestBody state
    );

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
    @POST("helpapp/api/user_signin.php")
    Call<SigninResponse> userlogin(
            @Part("mobile") RequestBody mobile,
            @Part("password") RequestBody password
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
    @POST("helpapp/help_datainsert.php")
    Call<AddHelpListResponse> help_DataInsert(
            @Part("user_id") RequestBody user_id,
            @Part("help_title") RequestBody help_title,
            @Part("help_description") RequestBody help_description,
            @Part("help_category_id") RequestBody help_category_id,
            @Part("state") RequestBody state,
            @Part("image\"; filename=\".jpg\" ") RequestBody file,
            @Part("address") String address,
            @Part("latitude") String latitude,
            @Part("longitude") String longitude


    );

}

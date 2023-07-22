package www.zentorc.com.RetrofiEndPoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import www.zentorc.com.DTO.SignUpDTO;
import www.zentorc.com.DTO.UserLoginDTO;
import www.zentorc.com.Entity.rider;

/*
This is the class where we are going to define methods using retrofit annotations that will allow us to
Make API Requests to our Server.
 */
public interface RetrofitEndPoint {
    /*
    This Below method is going to allow us to Send user entered phonenumber to our server
    which will trigger a two factor authentication to verify the user.
     */
    @POST("user-login")
    Call<Void> LoginUser(@Body UserLoginDTO userLoginDTO);

    /*
    This method below(EndPoint) is going to help us to verify our use sent otp and if the otp is correct
    then we will load the user data and save ii to the local storage.
     */
    @POST("user-login-verification")
    Call<rider>Verify_Login_User(@Header("Verification") String Verification_Header, @Header("otp") int otp);

    /*
    This method is going to help to send the user entered phonenumber to the server for
    verification (if user already exists) and will throw exception if user already exists
    or it will return the OTPToken to the user that will be used to verify the user with 2FA.
     */
    @POST("user-signup-verification")
    Call<Void>UserSignUp(@Body UserLoginDTO userLoginDTO);

    /*
      This method is going to help us verify the user signup process.we are going to send the RiderDTO,otp and the OTP
      token to s=tge server and upon verification by server the RiderDTO will be Stored at the Remote Database and
      locally as well.
    */
    @POST("user-signup")
    Call<Void>SignUpVerification(@Body SignUpDTO signUpDTO);
}

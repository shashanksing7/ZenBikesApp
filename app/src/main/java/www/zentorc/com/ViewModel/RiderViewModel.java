package www.zentorc.com.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import www.zentorc.com.DTO.SignUpDTO;
import www.zentorc.com.DTO.UserLoginDTO;
import www.zentorc.com.Entity.rider;
import www.zentorc.com.Repositories.RiderRepository;
import www.zentorc.com.RetrofitInterfaces.LoginUserInterface;
import www.zentorc.com.RetrofitInterfaces.LoginVerificationInterface;

/*
This will provide data to our view and handle user interaction from view.We also add our business logic
here before sending data to our views.
 */
public class RiderViewModel extends AndroidViewModel {

    /*
    creating instance of our RiderRepository.
     */
    private  RiderRepository riderRepository;
    /*
    The Application allows us to avoid passing Context that causes memory leaks.
     */
    public RiderViewModel(@NonNull Application application) {
        super(application);
        riderRepository=new RiderRepository(application);

    }

    /*
    creating a method to add data to the database
     */
    public void  insert(rider rider){
        riderRepository.insertrider(rider);
    }

    /*
    creating a method that would help login user by passing user entered value through
    a DTO and returns a hashmap of key pair values of status code,message,error body.
     */
    public void LoginUser(UserLoginDTO userLoginDTO,LoginUserInterface loginUserInterface){
        riderRepository.LoginUser(userLoginDTO,loginUserInterface);
    }

    /*
    This method is going to help us verify the user entered otp,and add the user to the local
    database if the otp is correct.
     */
    public  void  LoginUserVerification(String Verification_Header, int otp, LoginVerificationInterface loginVerificationInterface){
        riderRepository.Verify_UserLogin_OTP(Verification_Header, otp,loginVerificationInterface);
    }

     /*
    creating a method that would help lsignup user by passing user entered value through
    a DTO and returns a hashmap of key pair values of status code,message,error body.
     */
    public void  SignUpUser(UserLoginDTO userLoginDTO,LoginUserInterface loginUserInterface){
        riderRepository.RiderSignUp(userLoginDTO,loginUserInterface);
    }

    /*
    This method is going to help us verify the user signup process.we are going to send the RiderDTO,otp and the OTP
    token to s=tge server and upon verification by server the RiderDTO will be Stored at the Remote Database and
     locally as well.
     */
    public void SignUpVerification(String Verification_Header, int otp, SignUpDTO riderDTO,LoginUserInterface loginUserInterface){

        riderRepository.SignUpUserVerification(Verification_Header,otp,riderDTO,loginUserInterface);
    }
}

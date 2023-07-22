package www.zentorc.com.Repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import www.zentorc.com.DAO.riderDAO;
import www.zentorc.com.DTO.SignUpDTO;
import www.zentorc.com.Databases.RiderDataBase;
import www.zentorc.com.DTO.UserLoginDTO;
import www.zentorc.com.Entity.rider;
import www.zentorc.com.RetrofiEndPoint.RetrofitEndPoint;
import www.zentorc.com.RetrofitInterfaces.LoginUserInterface;
import www.zentorc.com.RetrofitInterfaces.LoginVerificationInterface;

/*
This class is going to act as repository and it is going to help us mediate between performing our
database operations locally and on remote databases.
 */
public class RiderRepository {
    /*
    Creating an instance of riderDAO interface that we will be getting
    using our database's abstract method to get the instance of rideDAO .
     */

    /*
    creating log tag
     */
    private static final String TAG = "mytag";


    /*
    Creating weak reference of riderDAO interface that we will be getting
    using our database's abstract method to get the instance of rideDAO .
     */

    private WeakReference<riderDAO> riderDAO;

    /*
    Creating instance of RiderDataBase  in the constructor of our Repository
     */

    public RiderRepository(Application application) {

        /*
        Getting the instance using the getinstance() method
         */
        RiderDataBase riderDataBase = RiderDataBase.getInstance(application);
        riderDAO = new WeakReference<>(riderDataBase.riderdao());
    }

    /*
    Enable lenient mode for the JSON parser by providing a custom Gson
     instance with lenient mode enabled.
     */
    Gson gson = new GsonBuilder().setLenient().create();

    /*
    Creating a Retrofit Object to perform our network operations using retrofit.
     */
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://13.233.61.173:8080/user/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    /*
    Now Creating instance of the RetrofitEndPoint Interface that we are going to use to call the
    methods defined there.
     */
    RetrofitEndPoint retrofitEndPoint = retrofit.create(RetrofitEndPoint.class);


    /*
    Now creating a method that will allow user to sign in (send request wih body to server) using retrofit
    and send back a hash map containing message,HTTP status code,error code etc.
     */
    public void LoginUser(UserLoginDTO userLoginDTO,LoginUserInterface loginUserInterface) {
        /*
          creating the instance of the Hashmap That we are going to return.
         */
        Map<String, String> LoginMap = new HashMap<>();;

        /*
           Here we wre going to perform our network request.
         */
        retrofitEndPoint.LoginUser(userLoginDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                /*
                Here we wre going to check if status of the Login operation using the Http status code,message and son on.
                 */

                /*
                Getting the Http status code
                 */
                int StatusCode = response.code();
                Log.d(TAG, "onResponse: "+StatusCode);

                /*
                 we will create a HashMAp that will Http status code,and error body everything else
                 null.
                 */
                if(StatusCode!=200){

                    /*
                    Getting the error body.
                     */
                    String ErrorBody=" ";
                    try {
                        ErrorBody=response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    LoginMap.put("Status", String.valueOf(StatusCode));
                    LoginMap.put("Verification",null);
                    LoginMap.put("message",null);
                    LoginMap.put("ErrorBody",ErrorBody);
                    loginUserInterface.onFailure(LoginMap);
                }
                /*
                Checking if the operation was successful,http status code is 200(ok) if successful we
                will create Hashmap with Verification header value,status code,everything else null;
                 */
                else {

                                        /*
                    Getting the Header from the response
                     */
                    String VerificationHeader = response.headers().get("Verification");

                    /*
                    creating a hash map ofr successful operations.
                     */
                    LoginMap.put("Status", String.valueOf(StatusCode));
                    LoginMap.put("Verification",VerificationHeader);
                    LoginMap.put("message",null);
                    LoginMap.put("ErrorBody",null);
                    loginUserInterface.onSuccess(LoginMap);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                 /*
                Here we will get the Exception that occurred while sending our request
                We will get the message and create the hash map.
                 */
                 String message = t.getMessage();
                 message = message.substring(0, 20) + "....";
                /*
                adding key value pairs to the hash map;
                 */
                LoginMap.put("Verification", null);
                LoginMap.put("StatusCode",null);
                LoginMap.put("message",message);
                LoginMap.put("errorbody", null);
                loginUserInterface.onFailure(LoginMap);
            }
        });

    }

    /*
    Creating a method to Verify the user Entered OTP and load the user from server and save data to local RoomDatabase
     if the user entered otp is correct.
     */
    public void Verify_UserLogin_OTP(String Verification_Header, int otp, LoginVerificationInterface loginVerificationInterface){

        /*
        now we are going to create a map that we will be passing to our logininetface to implement it's
        methods respectively to perform operations in our main ui.
         */
        Map<String,String> LoginVerificationMap=new HashMap<>();
        /*
        calling the call back method defined in our RetrofitEndPoint interface and performing our network operations
        in background thread and using the LoginUser Interface to send back the login map that is going to  contain
        result of network operations in form of key,value pair.
         */
        retrofitEndPoint.Verify_Login_User(Verification_Header,otp).enqueue(new Callback<rider>() {
            @Override
            public void onResponse(Call<rider> call, Response<rider> response) {

                if(response.code()==200){

                    /*
                        our network operation was successful and now perform further operation.
                     */
                    /*
                    Getting the JWT token from the Header.
                     */
                    String Authorization_Token=response.headers().get("Authorization");
                    /*
                    Getting the status code of request
                     */
                    String Status= String.valueOf(response.code());

                    /*
                    Getting the server returned user data.
                     */
                    rider rider=response.body();

                    /*
                     Adding the Values to the LoginVerificationMap
                     */
                    LoginVerificationMap.put("Authorization",Authorization_Token);
                    LoginVerificationMap.put("Sattus",Status);
                    LoginVerificationMap.put("ErrorBody",null);
                    LoginVerificationMap.put("message",null);

                    loginVerificationInterface.onSuccess(LoginVerificationMap,rider);
                }
                else {
                    /*
                    our network operation was not successful and server threw some error.
                    Get the Exception thrown by server and Add it to the LoginVerificationMap.
                     */

                    /*
                    Getting the status code of request
                     */
                    String Status= String.valueOf(response.code());
                    
                    /*
                    Getting the Exception thrown by server. 
                     */
                    String ErrorBody=" ";
                    try {
                        ErrorBody=response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LoginVerificationMap.put("Status",Status);
                    LoginVerificationMap.put("Authorization",null);
                    LoginVerificationMap.put("ErrorBody",ErrorBody);
                    LoginVerificationMap.put("message",null);
                    loginVerificationInterface.onFailure(LoginVerificationMap,null);
                }
            }
            @Override
            public void onFailure(Call<rider> call, Throwable t) {
                
                /*
                This indicates that pur network  operation was not executed and an exception was thrown. 
                 */
                 /*
                Here we will get the Exception that occurred while sending our request
                We will get the message and create the hash map.
                 */
                String message = t.getMessage();
                message = message.substring(0, 20) + "....";
                /*
                adding key value pairs to the hash map;
                 */
                LoginVerificationMap.put("Authorization", null);
                LoginVerificationMap.put("Status",null);
                LoginVerificationMap.put("message",message);
                LoginVerificationMap.put("errorbody", null);
                loginVerificationInterface.onFailure(LoginVerificationMap,null);
            }
        });

    }
    /*
    This method is going to help to send the user entered phonenumber to the server for
    verification (if user already exists) and will throw exception if user already exists
    or it will return the OTPToken to the user that will be used to verify the user with 2FA.
     */
    public void RiderSignUp(UserLoginDTO userLoginDTO,LoginUserInterface loginUserInterface){
        /*
          creating the instance of the Hashmap That we are going to return.
         */
        Map<String, String> LoginMap = new HashMap<>();;
        /*
           Here we wre going to perform our network request.
         */
        retrofitEndPoint.LoginUser(userLoginDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                /*
                Here we wre going to check if status of the Login operation using the Http status code,message and son on.
                 */
                /*
                Getting the Http status code
                 */
                int StatusCode = response.code();
                Log.d(TAG, "onResponse: "+StatusCode);

                /*
                 we will create a HashMAp that will Http status code,and error body everything else
                 null.
                 */
                if(StatusCode!=200){
                    /*
                    Getting the error body.
                     */
                    String ErrorBody=" ";
                    try {
                        ErrorBody=response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LoginMap.put("Status", String.valueOf(StatusCode));
                    LoginMap.put("Verification",null);
                    LoginMap.put("message",null);
                    LoginMap.put("ErrorBody",ErrorBody);
                    loginUserInterface.onFailure(LoginMap);
                }
                /*
                Checking if the operation was successful,http status code is 200(ok) if successful we
                will create Hashmap with Verification header value,status code,everything else null;
                 */
                else {
                    /*
                    Getting the Header from the response
                     */
                    String VerificationHeader = response.headers().get("Verification");

                    /*
                    creating a hash map ofr successful operations.
                     */
                    LoginMap.put("Status", String.valueOf(StatusCode));
                    LoginMap.put("Verification",VerificationHeader);
                    LoginMap.put("message",null);
                    LoginMap.put("ErrorBody",null);
                    loginUserInterface.onSuccess(LoginMap);


                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                 /*
                Here we will get the Exception that occurred while sending our request
                We will get the message and create the hash map.
                 */
                String message = t.getMessage();
                message = message.substring(0, 20) + "....";
                /*
                adding key value pairs to the hash map;
                 */
                LoginMap.put("Verification", null);
                LoginMap.put("StatusCode",null);
                LoginMap.put("message",message);
                LoginMap.put("errorbody", null);
                loginUserInterface.onFailure(LoginMap);
            }
        });


    }

        /*
    This method is going to help us verify the user signup process.we are going to send the RiderDTO,otp and the OTP
    token to s=tge server and upon verification by server the RiderDTO will be Stored at the Remote Database and
     locally as well and will return result as a HashMap.
     */
    public void SignUpUserVerification(String Verification_Header, int otp, SignUpDTO riderDTO,LoginUserInterface loginUserInterface){

        /*
          creating the instance of the Hashmap That we are going to return.
         */
        Map<String, String> LoginMap = new HashMap<>();;
        /*
           Here we wre going to perform our network request.
         */
        retrofitEndPoint.SignUpVerification(riderDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                               /*
                Here we wre going to check if status of the Login operation using the Http status code,message and son on.
                 */
                /*
                Getting the Http status code
                 */
                int StatusCode = response.code();
                Log.d(TAG, "onResponse: "+StatusCode);

                /*
                 we will create a HashMAp that will Http status code,and error body everything else
                 null.
                 */
                if(StatusCode!=200){
                    /*
                    Getting the error body.
                     */
                    String ErrorBody=" ";
                    try {
                        ErrorBody=response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LoginMap.put("Status", String.valueOf(StatusCode));
                    LoginMap.put("Verification",null);
                    LoginMap.put("message",null);
                    LoginMap.put("ErrorBody",ErrorBody);
                    loginUserInterface.onFailure(LoginMap);
                }
                /*
                Checking if the operation was successful,http status code is 200(ok) if successful we
                will create Hashmap with Verification header value,status code,everything else null;
                 */
                else {
                    /*
                    Getting the Header from the response
                     */
                    String VerificationHeader = response.headers().get("Verification");

                    /*
                    creating a hash map ofr successful operations.
                     */
                    LoginMap.put("Status", String.valueOf(StatusCode));
                    LoginMap.put("Verification",VerificationHeader);
                    LoginMap.put("message",null);
                    LoginMap.put("ErrorBody",null);
                    loginUserInterface.onSuccess(LoginMap);


                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                /*
                Here we will get the Exception that occurred while sending our request
                We will get the message and create the hash map.
                 */
                String message = t.getMessage();
                message = message.substring(0, 20) + "....";
                /*
                adding key value pairs to the hash map;
                 */
                LoginMap.put("Verification", null);
                LoginMap.put("StatusCode",null);
                LoginMap.put("message",message);
                LoginMap.put("errorbody", null);
                loginUserInterface.onFailure(LoginMap);

            }
        });
    }


    /*
    creating a trial method to insert data into rider table.
     */

    public void  insertrider(rider rider){

        new InsertAsyncTask(riderDAO.get()).execute(rider);

    }

    /*
    Executing the task in separate back ground thread to avoid blocking the
    UI thread.
     */
    private  static  class InsertAsyncTask extends AsyncTask<rider,Void,Void>{

        private riderDAO riderDAO;
        /*
        Assigning the value of instance of our DAO class to the variable
        of riderDAO type.
         */
        public InsertAsyncTask(riderDAO riderDAO){
            this.riderDAO=riderDAO;
        }

    /*
    Here we will perform our operations that will be executed
    in background.
     */
        @Override
        protected Void doInBackground(rider... riders) {

            riderDAO.AddRider(riders[0]);
            return null;
        }
    }
}

package www.zentorc.com.VeifyOTP;

import static android.app.Activity.RESULT_OK;


import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.zentorc.com.DTO.SignUpDTO;
import www.zentorc.com.Entity.rider;
import www.zentorc.com.R;
import www.zentorc.com.RetrofitInterfaces.LoginUserInterface;
import www.zentorc.com.RetrofitInterfaces.LoginVerificationInterface;
import www.zentorc.com.ViewModel.RiderViewModel;


public class VerifyOTP extends Fragment {

    /*
    Creating instance of the View of the fragment.
     */
    View VerifyOTPview;

    /*
    creating the variable of type View for the custom Toast layout .
     */
    private View Custom_Toast;
    /*
    Creating a variable of Type Dialog for our custom progress dialog.
     */
    private android.app.Dialog dialog;

    /*
    creating the Variable of TextView for text of our Custom Toast Layout
     */
    private TextView ToastText;
    private TextView ToastButtonText;

    /*
    Creating the request code for starting our OTP reading activity using the
    StartActivityForResult() method.
     */
    private static final int REQ_USER_CONSENT = 200;
    /*
    Creating the instance of our Broadcast Receiver that  is going to listen for SMS intent.
     */
   private VerifyOTPBroadcastReceiver VerifyOTPBroadcastReceiver;

    /*
    Creating instance of the Field that is going to allow us to enter otp
     */
    private PinView etOTP;
    /*
    Creating variable of Type Textview for our Custom Progress Dialog.
     */
    private  TextView Dialog_Text;

    /*
    Creating the instance of the view model
     */
    private RiderViewModel riderViewModel;

    /*
    Tag Variable for Logs.
     */
    private static final String TAG = "mytag";
    /*
    Creating a String Type Variable to store the OTP token
     */
    private  String Verification_Header;

    /*
    creating the instances of the Confirm button.
     */
    private Button VerifyOTPButton;

    /*
    Creating Confirm Button click flag variable.
    */
    private int Butoon_click_flag=0;

    /*
    Creating a String Type Variable to store Method(Login/SignUP)
     */
    private String Method;

    /*
     Creating a SignUpDTO Type Variable to store user Entered data in signup form.
     */
    private SignUpDTO signUpDTO;


    public VerifyOTP() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        /*
        Initializing instances of the view model in  our on create method.
         */
        riderViewModel=new ViewModelProvider(this).get(RiderViewModel.class);

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // Inflate the layout for this fragment

        VerifyOTPview = inflater.inflate(R.layout.fragment_verify_o_t_p, container, false);
        /*
        This helps to move our view when soft keyboard appears
         */
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        /*
        creating the  id assigning the ids to the object to the respective  views objects .
         */
        etOTP = VerifyOTPview.findViewById(R.id.verifyotppin);
        VerifyOTPButton=VerifyOTPview.findViewById(R.id.verifyotpconfirm);
        startSmartUserConsent();

        /*
        Getting the Arguments Received from the sender fragments.
         */
        Verification_Header=VerifyOTPArgs.fromBundle(getArguments()).getVerification();
        Method=VerifyOTPArgs.fromBundle(getArguments()).getMethod();
        signUpDTO=VerifyOTPArgs.fromBundle(getArguments()).getSingnUpDTO();
        int otp=Integer.parseInt(etOTP.getText().toString());


       /*
        Inflating our Custom toast's layout.
         */
        inflater=getLayoutInflater();

        Custom_Toast=inflater.inflate(R.layout.custom_toast_layout,null);


        /*
        creating the button click listener and the calling the appropriate method  and passing the
        appropriate parameters to perform the network request.
         */
        VerifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Butoon_click_flag==0){
                    Butoon_click_flag=1;
                    if(Method!="SignUp"){
                        /*
                        calling this method when the Method sent through safe args is Login and not
                        signup.
                         */
                        UserLoginVerification(Verification_Header,otp);
                    }
                    else{

                        /*
                        calling this method when the Method sent through safe args is signup and not
                        login.
                         */
                        SignUpUserVerification(Verification_Header,otp,signUpDTO);
                    }
                }
            }
        });

        /*
        Here we will automatically call the   UserLoginVerification() method when the six digit
        ui entered in the oin view.For this we use the addTextChangedListener(),inside the onTextChanged()
        we will call UserLoginVerification() method.
         */
        etOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*
                 Do nothing.
                 */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /*
                Check if the length of the text is six(6) if yes then call the UserLoginVerification() method
                and pass the valid arguments.
                 */
                if (s.length() == 6) {
                    /*
                    Calling  UserLoginVerification() method here and passing  the valid arguments.
                     */
                    UserLoginVerification(Verification_Header,otp);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*
                 Do nothing.
                 */
            }
        });



        return VerifyOTPview;
    }

    /*
    This method is going to be executed when the user clicks on the confirm button of the .
    This method will execute the  network request using the retrofit  in background thread,and we will get the
    data in a HashMap by implementing a interface methods.we will perform our UI updating operations in this method.
     */
    public void UserLoginVerification(String Verification_Header,int otp){

                /*
        Calling our Custom progress Dialog.
         */
        if(!requireActivity().isFinishing()&&isAdded()){
            ShowProgressDialog("Verifying...");
        }

        /*
        Checking if the length of otp is 6 or not if yes ,then perform further operation
        else show toast regarding the error.
         */
        if(String.valueOf(otp).length()==6){

                    /*
        Calling the LoginUserVerification() method and from RiderViewModel.This will take the parameters and rerun us
        a map object through the implementation of a LoginUserInterface.
         */

            riderViewModel.LoginUserVerification(Verification_Header, otp, new LoginVerificationInterface() {
                @Override
                public void onSuccess(Map<String, String> resultMap, rider rider) {
                /*
                  Hiding our Custom Progress Dialog.
                */
                    HideProgressDialog();
                /*
                This will be executed when our network request is
                 */
                    if(Integer.parseInt(resultMap.get("Status"))==200){
                    /*
                    Adding server returned data  to the database
                     */
                        riderViewModel.insert(rider);

                    /*
                    Getting the JWT token and saving it in saved preferences variable.
                     */
                        String JWTToken=resultMap.get("Authorization");
                        Navigation.findNavController(VerifyOTPview).navigate(R.id.action_verifyOTP_to_homeFragment);

                    }
                }

                @Override
                public void onFailure(Map<String, String> resultMap, rider rider) {

                /*
                 Hiding our Custom Progress Dialog.
                */
                    HideProgressDialog();
                    /*
                Here we rae going to get the HashMap when our network operation has failed
                 and our Http status code is other other than  200.
                 */

                /*
                Checking if the exception was thrown by the server or the android system.
                 */
                    if(resultMap.get("Status")!=null){

                    /*
                    Exception thrown by server.
                     */
                        CreateCustomToast(resultMap.get("ErrorBody"),null,Color.WHITE,Color.WHITE,Color.RED );
                    }
                    else{

                    /*
                    Exception thrown by android system.
                     */
                        CreateCustomToast(resultMap.get("message"),null,Color.WHITE,Color.WHITE,Color.RED);
                    }

                    Butoon_click_flag=0;

                }
            });

        }
        else{

            /*
               Hiding our Custom Progress Dialog.
             */
            HideProgressDialog();

            /*
            creating custom toast regarding the length of otp
             */
            CreateCustomToast("Enter 6 digit OTP",null, Color.WHITE,Color.WHITE,Color.RED );
            Butoon_click_flag=0;

        }

    }
    /*
    This method is going to help us verify the user signup process.we are going to send the RiderDTO,otp and the OTP
    token to s=tge server and upon verification by server the RiderDTO will be Stored at the Remote Database and
     locally as well.
     */
    private void SignUpUserVerification(String Verification_Header,int otp,SignUpDTO riderDTO){


                /*
        Calling our Custom progress Dialog.
         */
        if(!requireActivity().isFinishing()&&isAdded()){
            ShowProgressDialog("Verifying...");
        }

        /*
        Checking if the length of otp is 6 or not if yes ,then perform further operation
        else show toast regarding the error.
         */
        if(String.valueOf(otp).length()==6) {



            /*
        Calling the SignUpVerification() method and from RiderViewModel.This will take the parameters and rerun us
        a map object through the implementation of a LoginUserInterface.
         */

            riderViewModel.SignUpVerification(Verification_Header, otp, riderDTO, new LoginUserInterface() {
                @Override
                public void onSuccess(Map<String, String> resultMap) {

                    /*
                  Hiding our Custom Progress Dialog.
                */
                    HideProgressDialog();

                    /*
                This will be executed when our network request is
                 */
                    if(Integer.parseInt(resultMap.get("Status"))==200){
                    /*
                    Adding server returned data  to the database
                     */
                        rider rider=new rider(riderDTO.getPhonenumber(),riderDTO.getRider_name(),riderDTO.getEmail(),riderDTO.getGender(),riderDTO.getSecret_keyString());
                       riderViewModel.insert(rider);

                    /*
                    Getting the JWT token and saving it in saved preferences variable.
                     */
                        String JWTToken=resultMap.get("Authorization");
                    }

                }

                @Override
                public void onFailure(Map<String, String> resultMap) {


                /*
                 Hiding our Custom Progress Dialog.
                */
                    HideProgressDialog();
                    /*
                Here we rae going to get the HashMap when our network operation has failed
                 and our Http status code is other other than  200.
                 */

                /*
                Checking if the exception was thrown by the server or the android system.
                 */
                    if(resultMap.get("Status")!=null){

                    /*
                    Exception thrown by server.
                     */
                        CreateCustomToast(resultMap.get("ErrorBody"),null,Color.WHITE,Color.WHITE,Color.RED );
                    }
                    else{

                    /*
                    Exception thrown by android system.
                     */
                        CreateCustomToast(resultMap.get("message"),null,Color.WHITE,Color.WHITE,Color.RED);
                    }

                    Butoon_click_flag=0;

                }
            });

        }
        else
        {
            /*
               Hiding our Custom Progress Dialog.
             */
            HideProgressDialog();

            /*
            creating custom toast regarding the length of otp
             */
            CreateCustomToast("Enter 6 digit OTP",null, Color.WHITE,Color.WHITE,Color.RED );
            Butoon_click_flag=0;
        }


    }

    /*
    this method creates the consent flow,this flow prompts the user to grant the permission to the
    app to read otp from message.once the permission is granted this automatically
    listens for incoming sms message that match the app's registered hash string.
     */
    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(requireActivity());
        client.startSmsUserConsent(null);

    }

    /*
    This methods return the result of the operation performed by the StartActivityForResult()
    method.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*
        Checking if the method is returning result fo the desired operation.
         */
        if (requestCode == REQ_USER_CONSENT) {

            /*
            checking the result of the operation if the operation is successful
            get otp from the message.
             */

            if ((resultCode == RESULT_OK) && (data != null)) {

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    Retrieving otp from the  message
     */

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()) {

            etOTP.setText(matcher.group(0));

        }


    }

    /*
    this methods registers the broadcast receiver and start the onreceive() method.
    and listens for intent and extracts the status object through the on receive method defined in the
    VerifyOTPBroadcastReceiver class.and invoke s the StartActivityForResultMethod()
     */
    private void registerBroadcastReceiver() {

        VerifyOTPBroadcastReceiver = new VerifyOTPBroadcastReceiver();

        VerifyOTPBroadcastReceiver.smsBroadcastReceiverListener = new VerifyOTPBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                /*
                Performs the action specified in the intent and returns the result of the
                operation in onActivityresult() method.
                 */

                startActivityForResult(intent, REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };


        /*
        Registering the intent filter to listen for intents to retrieved otp
         */
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        requireActivity().registerReceiver(VerifyOTPBroadcastReceiver, intentFilter);

    }

    /*
    Registering the broadcast receiver.
     */
    @Override
    public void onStart() {
        registerBroadcastReceiver();
        super.onStart();
    }

    /*
    De registering the broadcast receiver on onStop() to stop memory leak.
     */
    @Override
    public void onStop() {
        requireActivity().unregisterReceiver(VerifyOTPBroadcastReceiver);
        super.onStop();
    }


    /*
This method is going to take Two string parameters ,one is going to be the toast text,
another is going to be the Button text
 */
    private void CreateCustomToast(String Toast_Text,String Toast_Button_Text,int Text_color,int Button_color,int Layout_BackGround_Color){

        /*
        Assigning the ids to the instances we crated.
         */
        ToastText=Custom_Toast.findViewById(R.id.custom_toast_text);
        ToastButtonText=Custom_Toast.findViewById(R.id.custom_toast_text_button);

        /*
        Setting the user sent text in the  text view and the button .
         */
        ToastText.setText(Toast_Text);
        ToastButtonText.setText(Toast_Button_Text);

        /*
        Setting color of text and button.
         */
        ToastText.setTextColor(Text_color);
        ToastButtonText.setTextColor(Button_color);

        /*
        Setting color of layout.
         */
        Custom_Toast.setBackgroundColor(Layout_BackGround_Color);

        /*
        Creating a Toast and assigning custom toast layout to the view.
         */
        Toast toast=new Toast(getContext().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(Custom_Toast);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

    }

    /*
    This method is going to show the custom progress Dialog,when we will be performing the network equest
    in background.
     */
    private void  ShowProgressDialog(String Dialog){

        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_progress_dialog);

        /*
        Setting the text of the dialog.
         */
        Dialog_Text=dialog.findViewById(R.id.custom_progress_dialog_layout_dialog_text);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_bg);
        dialog.setCancelable(false);
        Dialog_Text.setText(Dialog);
        /*
        Progress Dialog appears here.
         */
        dialog.show();

    }

    /*
    This method is going to help us hide our custom progress dialog,after our network request in
    background thread is over.
     */
    private void HideProgressDialog(){
        dialog.dismiss();
    }
}










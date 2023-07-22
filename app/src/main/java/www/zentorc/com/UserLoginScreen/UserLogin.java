package www.zentorc.com.UserLoginScreen;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

import www.zentorc.com.DTO.SignUpDTO;
import www.zentorc.com.DTO.UserLoginDTO;
import www.zentorc.com.R;
import www.zentorc.com.RetrofitInterfaces.LoginUserInterface;
import www.zentorc.com.ViewModel.RiderViewModel;

public class UserLogin extends Fragment {

    /*
    Creating the View object that represent the view of the fragment.
     */
    private View LoginScreenview;

    /*
    creating View of the layout that we are going to use
    in our custom toast.
     */
    private View Custom_Toast;

    /*
    creating a log tag.
     */
    private static final String TAG = "mytag";
    /*
    Creating a variable of Type Dialog for our custom progress dialog.
     */
    private android.app.Dialog dialog;

    /*
    Creating variable of Type Textview for our Custom Progress Dialog.
    */
    private  TextView Dialog_Text;
    /*
    creating the instance of TextInputLayout
     */
    private TextInputLayout LoginTextLayout;
        /*
    creating the instance of Edittext
     */
    private EditText LoginText;

    /*
    creating instance of Button
     */
    private Button LoginButton;

    /*
    Creating Confrim BUtton click flag variable.
     */
    private int Butoon_click_flag=0;

    /*
    creating a instance of Type SignUpDTO to to store data
    sent by SignUpScreen fragment.
     */
    private SignUpDTO RiderDTO;

    /*
    Creating instance sof the Textview of our custom toast layout.
     */
   private  TextView ToastText;
    private TextView ToastButtonText;

    /*
    creating the instance of RiderViewModel
     */
    private RiderViewModel riderViewModel;

    /*
    creating the objects of the TextView that will take us to the SignUpScreen.
     */
    private TextView TakeToSignUp;

    public UserLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*
        Initializing instances of the view model in  our on create method.
         */

        riderViewModel=new ViewModelProvider(this).get(RiderViewModel.class);
        super.onCreate(savedInstanceState);

        // Hide the action bar

        /*
        This will resize our view whe soft keyboard appears.
         */
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LoginScreenview=inflater.inflate(R.layout.fragment_user_login, container, false);
        /*
        creating the  id assigning the ids to the object to the respective  views objects .
         */
        LoginText=LoginScreenview.findViewById(R.id.userlogintext);
        LoginTextLayout=LoginScreenview.findViewById(R.id.userloginphonetextlayout);
        LoginButton=LoginScreenview.findViewById(R.id.loginconfirm);
        TakeToSignUp=LoginScreenview.findViewById(R.id.loginsignuptext);

       /*
        Inflating our Custom toast's layout.
         */
         inflater=getLayoutInflater();

        Custom_Toast=inflater.inflate(R.layout.custom_toast_layout,null);

        /*
        Getting the User Entered data sent from SignUpScreen after validation to through Safe Args.
         */
        RiderDTO=UserLoginArgs.fromBundle(getArguments()).getSignUpUserData();
        String method=UserLoginArgs.fromBundle(getArguments()).getMethod();

        /*
          Checking If Map<String,String> object is null or not if not nul,we will call a method  in onclicklistener() of the
           confirm button by passing the MAp<String,String>
          to that method that will perform further operation.
         */
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Below condition will stop triggering the button click actions until
                result of network request is thrown.
                 */
                if(Butoon_click_flag==0){
                    Butoon_click_flag=1;
                    /*
                    Checking if we have request to login or help user signup process.
                     */
                    if(method!="UserSignup"){
                        /*
                        will execute if we have request to login
                         */
                        CheckLoginMap();
                    }
                    else {
                        /*
                        will execute if we have to help user signup process.
                         */
                        PerformUserSignUp();
                    }

                }
            }
        });
        /*
        Hearing the click on TakeToSignUp textview to take the user to signup screen when they click on it.This
        will call the TakeToSignUpScreen() method that will navigate user to Signup screen.
         */
        TakeToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeToSignUpScreen();
            }
        });
       return LoginScreenview;
    }

    /*
    This method is going  to take LoginMap object as the and check the status code:
    if status code=200,take the OTPToken form the header and pass it to the
    Two factor authentication fragment.
    if status code!=200,take the error code and display it in a custom Toast.
     */
    private void CheckLoginMap(){

        /*
        Calling our Custom progress Dialog.
         */
        if(!requireActivity().isFinishing()&&isAdded()){
            ShowProgressDialog("Verifying...");
        }
        /*
        Getting the userInput From Edittext.
         */
        String username="+91"+LoginText.getText().toString();
        /*
        Checking if the user has entered the text length is 10 or less than 10:
        if less than 10,then perform further operation.
        else show  custom Toast to user.
         */
        if(LoginText.getText().length()==10){
            /*
            Perform further operation.
            check if status code is 200,if yes then take user to next fragment with required Header data.
            else get error body/message and display toast.
             */

        /*
        calling the LoginUserMethod() from our RiderViewModel and passing required parameters
        that will be used to perform network request and get back the result of the request.
        we will also implement the methods of LoginUserInterface to get back the result
         */

            riderViewModel.LoginUser(CreateEntity(username), new LoginUserInterface() {
                @Override
                public void onSuccess(Map<String, String> resultMap) {

                    /*
                    Hiding our Custom Progress Dialog.
                     */
                    HideProgressDialog();
                /*
                Here we rae going to get the HashMap when our network operation is successfully
                executed and our Http status code is 200.
                 */
                /*
                checking if the Http status code of the network request is successful
                 */
                    if (Integer.parseInt(resultMap.get("Status"))==200) {

                    /*
                    Navigating to the VerifyOtp fragment and sending the required data to
                    VerifyOTP fragment.
                     */
                        /*Getting the Verification token from the Header and passing it iwth the
                        Method.
                         */

                        www.zentorc.com.UserLoginScreen.UserLoginDirections.ActionUserLoginToVerifyOTP actionUserLoginToVerifyOTP=
                                UserLoginDirections.actionUserLoginToVerifyOTP(resultMap.get("Verification"),"Login",null);


                        Navigation.findNavController(LoginScreenview).navigate(actionUserLoginToVerifyOTP, new NavOptions.Builder()
                                .setPopUpTo(R.id.userLogin, true)
                                .build());
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
        else{
            /*
             Hiding our Custom Progress Dialog.
             */
            HideProgressDialog();

            /*
            Show Custom Toast Asking the user to enter 10 digit number.
             */
            CreateCustomToast("Please Enter a valid number",null,Color.WHITE,Color.WHITE,Color.RED);
            Butoon_click_flag=0;
        }

    }
    /*
    creating a method that will take user entered  phone number and craete a object of our userlogindto and
    return it.
     */
    private UserLoginDTO CreateEntity(String username){

        /*
        creating the UserLoginDTO object.
         */
        UserLoginDTO userLoginDTO=new UserLoginDTO(username);
        return  userLoginDTO;
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
    This method is going to get the User Entered Phone number and Then Add it to the RiderDTO and
    send the data to the Method in VerifyOTP fragment  that is going to perform further verification.
     */
    private void PerformUserSignUp(){

        /*
        Calling our Custom progress Dialog.
         */
        if(!requireActivity().isFinishing()&&isAdded()){
            ShowProgressDialog("Verifying...");
        }

        /*
        Getting the userInput From Edittext.
         */
        String username="+91"+LoginText.getText().toString();
        /*
        Now we are going to append it to the RIderDTO's phonenumber key after checking if it's null or not.
         */
        if(LoginText.getText().length()==10){

            /*
            Perform further operation.
            check if status code is 200,if yes then take user to next fragment with required Header data.
            else get error body/message and display toast.
             */

            /*
             calling the SignUpUser() from our RiderViewModel and passing required parameters
             that will be used to perform network request and get back the result of the request.
             we will also implement the methods of LoginUserInterface to get back the result
            */

            riderViewModel.SignUpUser(CreateEntity(username), new LoginUserInterface() {
                @Override
                public void onSuccess(Map<String, String> resultMap) {

                    /*
                    Hiding our Custom Progress Dialog.
                     */
                    HideProgressDialog();
                    /*
                    Here we rae going to get the HashMap when our network operation is successfully
                    executed and our Http status code is 200.
                    */

                    /*
                    checking if the Http status code of the network request is successful
                    */
                    if (Integer.parseInt(resultMap.get("Status"))==200) {

                        HideProgressDialog();

                         /*
                            Navigating to the VerifyOtp fragment and sending the required data to
                            VerifyOTP fragment.
                         */
                        /*Getting the Verification token from the Header and passing it iwth the
                        Method.
                         */

                        /*
                        Adding the Phone number to the RiderDTO.
                         */
                        RiderDTO.setPhonenumber(username);

                        www.zentorc.com.UserLoginScreen.UserLoginDirections.ActionUserLoginToVerifyOTP actionUserLoginToVerifyOTP=
                                UserLoginDirections.actionUserLoginToVerifyOTP(resultMap.get("Verification"),"SignUp",RiderDTO);


                        Navigation.findNavController(LoginScreenview).navigate(actionUserLoginToVerifyOTP, new NavOptions.Builder()
                                .setPopUpTo(R.id.userLogin, true)
                                .build());
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
            User has not entered phonenumber.
             */
            CreateCustomToast("Please Enter a valid number",null,Color.WHITE,Color.WHITE,Color.RED);
            Butoon_click_flag=0;
        }

    }

    /*
    This method is going to help us hide our custom progress dialog,after our network request in
    background thread is over.
     */
    private void HideProgressDialog(){
        dialog.dismiss();

    }

    /*
    This method will take to SignUpScreen,if user is not registered.
     */
    private void TakeToSignUpScreen(){

        Navigation.findNavController(LoginScreenview).navigate(R.id.action_userLogin_to_signUpScreen,null,new NavOptions.Builder()
                .setPopUpTo(R.id.userLogin,true)
                .build());

    }

}
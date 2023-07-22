package www.zentorc.com.SignUpScreen;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.zentorc.com.DTO.SignUpDTO;
import www.zentorc.com.R;


public class SignUpScreen extends Fragment {


    /*
    Creating the Variable of type View that is going to hold the View of our
    fragment
     */
    private View SignupScreenView;

    /*
    creating a variable of type AutocompleteTextview.
     */
    private AutoCompleteTextView GenderField;

    /*
    Creating a Variable of type ArrayAdapter that is going to represent the
    Adapter of our Gender Drop Down Field.
     */
    private ArrayAdapter<String> genderadapter;
    /*
    Creating a variable of type Edittext for getting our use entered full name in our SignUpScreen signup form.
     */
    private EditText RiderFullName;
    /*
    Creating a variable of type Edittext for getting our use entered email in our SignUpScreen signup form.
     */
    private  EditText RiderEmail;

    /*
    Creating Confrim BUtton click flag variable.
     */
    private int Butoon_click_flag=0;

    /*
    Creating a variable of type String Text for getting our use entered Gender in our SignUpScreen signup form.
     */
    private String RiderGender = "";

    /*
    Creating a variable of type Edittext Text for getting our use entered SecurityKey in our SignUpScreen signup form.
     */
    private EditText SecurityKey;

    /*
    Creating a variable of type Edittext Text for getting our use entered SecurityKey in our SignUpScreen signup form.
    */
    private Button ContinuButton;

    /*
       creating View of the layout that we are going to use
       in our custom toast.
    */
    private View Custom_Toast;

    /*
    Creating instance sof the Textview of our custom toast layout.
    */
    private TextView ToastText;
    private TextView ToastButtonText;



    public SignUpScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        /*
        This will resize our view whe soft keyboard appears.
         */
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SignupScreenView=inflater.inflate(R.layout.fragment_sign_up_screen, container, false);

        /*
        Binding the Variables to our id's.
         */
        GenderField=SignupScreenView.findViewById(R.id.SignupScreenGender);
        RiderFullName=SignupScreenView.findViewById(R.id.UserName);
        SecurityKey=SignupScreenView.findViewById(R.id.SecurityKey);
        RiderEmail=SignupScreenView.findViewById(R.id.RiderEmail);
        ContinuButton=SignupScreenView.findViewById(R.id.loginconfirm);

        /*
        Inflating our Custom toast's layout.
         */
        inflater=getLayoutInflater();
        Custom_Toast=inflater.inflate(R.layout.custom_toast_layout,null);

        /*
        Creating a String Array that is going to represent the Item of Drop down,For our
        Gender Field.It includes,Male,Female and Others.
         */
        String[] Gender_list= {"Male","Female","Others"};
        /*
        initializing the Adapter for our Gender Drop Down.
         */
        genderadapter=new ArrayAdapter<String>(getContext(),R.layout.gender_list,Gender_list);
        /*
        Setting the Adapter.
         */
        GenderField.setAdapter(genderadapter);
        /*
        Listening to continue Button click performing respective network operation.
         */
        ContinuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Butoon_click_flag==0) {
                    Butoon_click_flag = 1;
                    ValiDateUser();
                }
            }
        });
        /*
        This below call we are going tot listen for user selected gender and are poignant to save it after
        verifying it.
         */

        GenderField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("mytag", "onItemClick: ");
                RiderGender =parent.getItemAtPosition(position).toString();
            }
        });


        return SignupScreenView;
    }

    /*
    This method is going to validate the User Entered Data and if the user entered data
    is valid we will create a SignUpDTO object with phonenumber==null and we will pass that to the
    UserLogin fragment where we will ask user for phone number and validate it.If everything is fine
    we will get back otp token from server and OTP in users mobile and upon two factor validation we will
    register the user and save data to remote database and local sql lite database and load it in our app.
     */
    private  void ValiDateUser(){
        /*
        Here we wre going to get all the user entered data from the signup screen.
         */
        String Ridername=RiderFullName.getText().toString();
        String SecutiyKey=SecurityKey.getText().toString();
        String Rider_Email=RiderEmail.getText().toString();
        /*
        Checking if the use has entered the valid email or not,if yes proceed to validate password
        else Show toast asking to enter Valid email.
         */
        if(isValidEmail(Rider_Email)){
            /*
            Email is valid yes proceed to validate password.
             */
            if(isPasswordValid(SecutiyKey)){
                /*
                password is valid yes proceed to validate RiderGender.
             */
                if(isValidGender(RiderGender)){

                    /*
                    password is valid yes proceed to validate Rider FullName.
                 */

                    if (Ridername!=null){

                         /*
                         Full name  is valid yes proceed to create a SignUpDTO object with
                         phonenumber==null and we will pass that to the
                         UserLogin fragment
                        */

                        SignUpDTO ZenTorcRider=new SignUpDTO(null,Ridername,Rider_Email,RiderGender,SecutiyKey);
                        /*
                        sending data to UserLogin fragment for further processing.
                         */
                        SignUpScreenDirections.ActionSignUpScreenToUserLogin actionSignUpScreenToUserLogin=
                                SignUpScreenDirections.actionSignUpScreenToUserLogin(ZenTorcRider,"UserSignup");

                        /*
                        Navigating user to UserLogin Screen and sending Data for further processing.
                         */
                        Navigation.findNavController(SignupScreenView).navigate(actionSignUpScreenToUserLogin,new NavOptions.Builder()
                                .setPopUpTo(R.id.RidersignUpScreen,true)
                                .build());


                    }
                    else {
                        /*
                     Show toast asking to enter Valid Full name.
                    */
                        Butoon_click_flag=0;
                        CreateCustomToast("Please Enter a valid Full Name",null,Color.WHITE,Color.WHITE,Color.RED);

                    }

                }
                else {
                    /*
                Show toast asking to enter Valid Gender.
                */
                    Butoon_click_flag=0;
                    CreateCustomToast("Please Select a valid Gender",null,Color.WHITE,Color.WHITE,Color.RED);
                }

            }

            else{
                /*
                Show toast asking to enter Valid password.
             */
                Butoon_click_flag=0;
                CreateCustomToast("Please Enter a valid Security Key",null,Color.WHITE,Color.WHITE,Color.RED);

            }

        }
        else{
            /*
                Show toast asking to enter Valid email.
             */

            Butoon_click_flag=0;
            CreateCustomToast("Please Enter a valid Email",null, Color.WHITE,Color.WHITE,Color.RED);
        }

    }

    /*
    The isValidEmail() function takes an email address string as input and returns a boolean value
    indicating whether the email is valid or not.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    /*
    The isPassword8CharsLong() function takes a password string as input and returns a boolean value
    indicating whether the password is exactly 8 characters long or not.
     */

    public static boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= 8;
    }

    /*
    The isValidGender() function takes a gender string as input and returns a boolean
    value indicating whether the gender is valid or not.
     */

    public static boolean isValidGender(String gender) {
        if (gender == null) {
            return false;
        }
        String[] validGenders = {"male", "female", "others"};
        for (String validGender : validGenders) {
            if (gender.equalsIgnoreCase(validGender)) {
                return true;
            }
        }
        return false;
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





}
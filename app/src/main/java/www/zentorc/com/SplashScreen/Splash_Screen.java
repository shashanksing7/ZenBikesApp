
package www.zentorc.com.SplashScreen;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.zentorc.com.R;


public class Splash_Screen extends Fragment {


    /*
    Determine Hardware Availability of GPS in User's Smart Phone,
    If hardware is Available then Proceed
    to Check For permission,if not redirect user to educational UI.

     */



    //Declaring a variable of type View
    private View SplashScreenView;

    //this variable is used in the CheckLocPermission() method
    private boolean PermissionAlreadyGranted=false;


     //Creating a RequestCode Variable
    private final int RequestCode=5000;


    private static final String TAG = "hellotag";




    public Splash_Screen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       SplashScreenView = inflater.inflate(R.layout.fragment_splash__screen, container, false);


//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


               /*
        Checking if device has required hardware or not
         */
        if(getActivity().getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)){

            //Call methods to check for permissions and request permission
            ReqLocPermission();

        }
        else {

            //Redirect user to Hardware not found UI
            Navigation.findNavController(SplashScreenView).navigate(R.id.action_splash_Screen_to_hardware_Not_Found_UI);

        }

        return SplashScreenView;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }



   /*  Calling OnRequestPermissionResult() method to get user response to our request to permission
   */
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if(requestCode==RequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permission Granted,Redirect User to Home Fragment
                Log.d(TAG, "onRequestPermissionsResult: granted");
                Navigation.findNavController(SplashScreenView).navigate(R.id.action_splash_Screen_to_onBoardingScreen);



            }
            else {

                ReqLocPermission();
                /*
                permission Denied,Redirect Users to Educational UI
                and checking if view hierarchy has been created or not
                 */

                if (SplashScreenView!=null){
                    /*

                    using this method we will remove our current fragment form
                    the backstack and go to new fargmnet
                     */
                   Navigation.findNavController(SplashScreenView).navigate(R.id.educational_UI,null,
                            new NavOptions.Builder()
                                    .setPopUpTo(R.id.splash_Screen,true)
                                    .build());
//                    Navigation.findNavController(SplashScreenView).navigate(R.id.action_splash_Screen_to_educational_UI);
                }

            }

        }

    }

    /*Now based on the boolean value returned by the CheckLocPermission() method
    we will create a method that will request for user permission/redirect user to home fragment
    if permission is already granted
     */

    private void ReqLocPermission(){

        if(CheckLocPermission()){

            /*This means Location Permission Has already been granted
            redirect user to the home fragment
             */

            Navigation.findNavController(SplashScreenView).navigate(R.id.action_splash_Screen_to_onBoardingScreen);

        }
        else {

            /* Request For Location Permission */

           requestPermissions(new String[]{ACCESS_FINE_LOCATION},RequestCode);

        }
    }
        /*
    Creating Method CheckLocPermission() to check if user has already  granted permission,
    if so noo need to ask for permission proceed to next UI,else request permission.
     */

    private boolean CheckLocPermission(){


        if(ActivityCompat.checkSelfPermission(getContext(),ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){

            PermissionAlreadyGranted=true;

        }
        else{

            /*
            checking if the users denied permission to the request or
            the request is being asked first time
            this will return TRue if user has denied permission but has not ticked "Don't ask again"
           ,it will return False if it's first ever request for app or the  user has
           denied permission and has  ticked "Don't ask again"
             */
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){

                /*
                 user has denied permission but has not ticked "Don't ask again"
                 Request
                 */
                PermissionAlreadyGranted=false;

            }
            else {

                /*

                it will return False if it's first ever request for app or the  user has
                  denied permission and has  ticked "Don't ask again"
                 */

                PermissionAlreadyGranted=false;

            }


        }

        return PermissionAlreadyGranted;
    }




}
//package www.zentorc.com;
//
//import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//
//import androidx.core.app.ActivityCompat;
//
//import java.util.logging.LogRecord;
//
//public class PermissionThread extends Thread{
//
//
//    /*
//    Determine Hardware Availability of GPS in User's Smart Phone,
//    If hardware is Available then Proceed
//    to Check For permission,if not redirect user to educational UI.
//     */
//
//
//    //this variable is used in the CheckLocPermission() method
//    private boolean PermissionAlreadyGranted=false;
//
//    /*this variable has context of Application make
//    sure to set this to null before killing the thread*/
//     private  static Context ApplicationContext;
//
//     /*
//     Creating a RequestCode Variable
//      */
//
//    private final int RequestCode=5000;
//
//    /*
//    Creating a Variable taht holds refernce to fragment
//     */
//    private Activity SplashScreenInstance;
//
//
//    private static final String TAG = "hellotag";
//
//
//    /*
//
//     /*
//     Parameterized Constructor That Takes Activity context
//      */
//
//    public PermissionThread(Context ApplicationContext,Activity SplashScreenInstance) {
//
//        this.ApplicationContext=ApplicationContext;
//        this.SplashScreenInstance=SplashScreenInstance;
//
//        if(ApplicationContext.getApplicationContext().getPackageManager().
//                hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)){
//
//
//            //Call methods to check for permissions and request permission
//            ReqLocPermission();
//
//        }
//        else {
//
//            //Redirect user to Educational UI
//        }
//
//
//    }
//    /*
//    Calling OnRequestPermissionResult() method to get user response to our request to permission
//     */
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//
//                if(requestCode==RequestCode) {
//                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                        //Permission Granted,Redirect User to Home Fragment
//                        Log.d(TAG, "onRequestPermissionsResult: granted");
//                    }
//                    else {
//                        //permission Denied,Redirect Users to Educational UI
//                    }
//
//                }
//
//    }
//
//
//
//
//
//
//
//
//    /*Now based on the boolean value returned by the CheckLocPermission() method
//    we will create a method that will request for user permission/redirect user to home fragment
//    if permission is already granted
//     */
//
//    private void ReqLocPermission(){
//
//        if(CheckLocPermission()){
//
//            /*This means Location Permission Has already been granted
//            redirect user to the home fragment
//             */
//
//        }
//        else {
//
//            /* Request For Location Permission */
//
//            ActivityCompat.requestPermissions(SplashScreenInstance,new String[]{ACCESS_FINE_LOCATION},RequestCode);
//
//        }
//    }
//
//
//    /*
//    Creating Method CheckLocPermission() to check if user has already  granted permission,
//    if so noo need to ask for permission proceed to next UI,else request permission.
//     */
//
//    private boolean CheckLocPermission(){
//
//
//        if(ActivityCompat.checkSelfPermission(ApplicationContext,ACCESS_FINE_LOCATION)==
//                PackageManager.PERMISSION_GRANTED){
//
//            PermissionAlreadyGranted=true;
//
//        }
//        return PermissionAlreadyGranted;
//    }
//
//
//
//
//
//
//}

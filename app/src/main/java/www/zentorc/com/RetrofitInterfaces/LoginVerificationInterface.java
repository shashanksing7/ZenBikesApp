package www.zentorc.com.RetrofitInterfaces;

import java.util.Map;

import www.zentorc.com.Entity.rider;

public interface LoginVerificationInterface {

    /*
    This method is going to be executed when the Http status code will be 200
     */
    void onSuccess(Map<String, String> resultMap, rider rider);


    /*
    This method will be executed when there will be exception thrown by the server or
    android system,which will be handled accordingly.
     */

    void onFailure(Map<String, String> resultMap,rider rider);
}

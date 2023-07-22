package www.zentorc.com.RetrofitInterfaces;

import java.util.Map;

/*
This interface is going to help us run our network reques in the worker/background thread
and map the result if the network request to the Main thread/UI thread.
 */
public interface LoginUserInterface {

    /*
    This method is going to be executed when the Http status code will be 200
     */
    void onSuccess(Map<String, String> resultMap);


    /*
    This method will be executed when there will be exception thrown by the server or
    android system,which will be handled accordingly.
     */

    void onFailure(Map<String, String> resultMap);
}

package www.zentorc.com.SplashScreen;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.zentorc.com.R;


public class Educational_UI extends Fragment {
    /*
    creating variable for button
     */
    AppCompatButton AllowButton;


    public Educational_UI() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_educational__u_i, container, false);

        /*
        assigning the button in our layout
         */
        AllowButton=view.findViewById(R.id.educationaluibtn);

        AllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                    /*

                    using this method we will remove our current fragment form
                    the backstack and go to new fargmnet
                     */

                Navigation.findNavController(view).navigate(R.id.splash_Screen,null,
                        new NavOptions.Builder()
                                .setPopUpTo(R.id.educational_UI,true)
                                .build());
//                Navigation.findNavController(view).navigate(R.id.action_educational_UI_to_splash_Screen);

            }
        });

        return view;
    }
}
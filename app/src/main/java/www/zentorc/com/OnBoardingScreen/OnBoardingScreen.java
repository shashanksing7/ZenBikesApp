package www.zentorc.com.OnBoardingScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import www.zentorc.com.R;

public class OnBoardingScreen extends Fragment {

    /*
    creating object of Viewpager
     */
    ViewPager viewPager;


             /*
    creating an ArrayList that would hold the list of our images
    that we will display in our viewpager
     */

    ArrayList<Integer> arrayList = new ArrayList<>();

    /*
    Creating Object of our Adapter
     */
    MyAdapter myAdapter;


    /*
    Creating a Flag for Back Pressed Screen
     */
    private boolean BackPressFlag = false;


    /*
    Creating Weak Reference to context so that our fragment could be garbage collected
     */
    private WeakReference<Context> mContextReference;

    /*
    In the onAttach() method, we assign the value of getActivity() to
     mContextReference using the WeakReference constructor.
     We can then use mContextReference.get() to access the Context object in other methods.
     */
    private static final String TAG = "mytag";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContextReference = new WeakReference<>(context);
    }

    //Constructor
    public OnBoardingScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Hide the action bar
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.hide();

        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        creating variables for dot's
         */

        ImageView FirstDot, SecondDot, ThirdDot, FourthDot;

        /*
        creating variable for Next Button
         */
        AppCompatButton NextButton;

        /*
        Creating variable for Skip Button
         */
        TextView SkipButton;


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_boarding_screen, container, false);

        /*
        initialising viewpager object to it's respective id
         */
        viewPager = view.findViewById(R.id.onBoardingScreenviewpager);
        SkipButton = view.findViewById(R.id.onboardingscreenskip);
        NextButton = view.findViewById(R.id.onboardingscreennext);
        FirstDot = view.findViewById(R.id.first_screen);
        SecondDot = view.findViewById(R.id.second_screen);
        ThirdDot = view.findViewById(R.id.third_screen);
        FourthDot = view.findViewById(R.id.fourth_screen);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        /*
        adding image's id to our arraylsit
         */

        arrayList.add(R.drawable.firstscreenimg);
        arrayList.add(R.drawable.secondscreenimg);
        arrayList.add(R.drawable.thirdscreenimg);
        arrayList.add(R.drawable.fifthscreenimg);


        /*
        Adding a onPageChange listener to animate our dots and Perform required operations with our
        buttons
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    FirstDot.setScaleX(1.5f);
                    FirstDot.setScaleY(1.5f);
                } else if (position == 1) {
                    SecondDot.setScaleX(1.5f);
                    SecondDot.setScaleY(1.5f);
                } else if (position == 2) {
                    ThirdDot.setScaleX(1.5f);
                    ThirdDot.setScaleY(1.5f);
                } else if (position == (arrayList.size() - 1)) {
                    FourthDot.setScaleX(1.5f);
                    FourthDot.setScaleY(1.5f);
                    SkipButton.setVisibility(view.GONE);
                    NextButton.setText("Next");
                }
            }


            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /*
        Listening to our Next Button click and changing images
         */
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                  This condition helps to prevent Error
                 */

                if (viewPager.getCurrentItem() != 3) {
                    /*
                Increasing the current item in viewpager by one so that next
                screen replaces previous screen
                 */
                    viewPager.setCurrentItem(getitem(1), true);
                } else {
                    www.zentorc.com.OnBoardingScreen.OnBoardingScreenDirections.ActionOnBoardingScreenToUserLogin actionOnBoardingScreenToUserLogin=
                            OnBoardingScreenDirections.actionOnBoardingScreenToUserLogin(null,null);

                    Navigation.findNavController(view).navigate(actionOnBoardingScreenToUserLogin,
                            new NavOptions.Builder()
                                    .setPopUpTo(R.id.onBoardingScreenid, true)
                                    .build());
                }
            }
        });
        /*
        creating and initialising object of our adapter
         */
        myAdapter = new MyAdapter(mContextReference, arrayList);
        viewPager.setAdapter(myAdapter);
        return view;
    }


    private int getitem(int item) {

        return viewPager.getCurrentItem() + item;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arrayList = null;
        myAdapter.NullifyContext();
        viewPager.setAdapter(null);
        myAdapter = null;
        viewPager = null;
    }
}
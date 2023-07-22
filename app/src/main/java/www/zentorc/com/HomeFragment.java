package www.zentorc.com;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    /*
    Creating Variable of type View that is going tp hold the
    View Of our Fragment.
     */
    private View HomeFragmentView;

    public HomeFragment() {
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
        HomeFragmentView=inflater.inflate(R.layout.fragment_home, container, false);

        return HomeFragmentView;
    }
}
package www.zentorc.com.OnBoardingScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import www.zentorc.com.R;


/*
This class gives the adapter that we are going to use to set our image sin our onboarding screen
 */
public class MyAdapter extends PagerAdapter {

    /*
    These variable are required.
    make sure to null context before it's fragment/Activity is destroyed to prevent memory leaks
     */

        /*
    Creating Weak Reference to context so that our fragment could be garbage collected
     */
    private WeakReference<Context> mContextReference;
    ArrayList<Integer>arrayList;
    LayoutInflater layoutInflater;

    /*
    Creating the parameterized constructor that will help us initialise these values
     */

    public MyAdapter(WeakReference<Context> mContextReference, ArrayList<Integer> arrayList) {
        this.mContextReference = mContextReference;
        this.arrayList = arrayList;
        layoutInflater=LayoutInflater.from( mContextReference.get());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    @SuppressLint("MissingInflatedId")
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=layoutInflater.inflate(R.layout.onboardingsliderlayout,container,false);
        ImageView imageView=view.findViewById(R.id.firstimg);
        imageView.setImageResource(arrayList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);
    }

    void NullifyContext(){
        mContextReference=null;
        arrayList=null;


    }
}

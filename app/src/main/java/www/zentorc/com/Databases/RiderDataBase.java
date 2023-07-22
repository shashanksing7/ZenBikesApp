package www.zentorc.com.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import www.zentorc.com.DAO.riderDAO;
import www.zentorc.com.Entity.rider;

/*
This class is going to act as the database for our tables.
 */
@Database(entities = {rider.class}, version = 1)
public abstract class RiderDataBase extends RoomDatabase {

    /*
    Now creating the instance of the database so that we can us it to
    convert our class into singleton,i.e only one instance of the database class
    will be created and the database operations we perform are synchronized.
     */

    private static  RiderDataBase instsance;

    /*
    Now we will declare an abstract method that returns an instance of the
    riderDAO interface the instance is  used to perform database operations.
    This method is marked as abstract because Room will generate the implementation
     code for it at compile time.
     */

    public abstract riderDAO riderdao();

    /*
    Now lets create a method that will return us the singleton object of the RiderDatabase.
    we make this method synchronized meaning that only one thread can access it at a time.
     */

    public static  synchronized RiderDataBase getInstance(Context context){

        /*
        Checking if the instance of the database is null.
         */
        if(instsance==null){

            instsance= Room.databaseBuilder(context.getApplicationContext(),RiderDataBase.class,"RiderDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  instsance;
    }
}

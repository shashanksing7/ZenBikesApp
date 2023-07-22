package www.zentorc.com.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import www.zentorc.com.Entity.rider;

/*
This DAO interface is going to help us to perform operations in our database.
The methods we define here are going to help us perform the operations.
 */
@Dao
public interface riderDAO {

    /*
    This method is going to help us to insert the rider details in our rider table
    by passing the rider entity that we are going to get from server after
    successful registration of user.
     */

    @Insert
    void AddRider(rider rider);

    /*
    The below method is going to delete the user details from the rider table,when the user logs out from app.
     */

    @Query("DELETE FROM rider")
    void DeleteRider();

    /*
    This method below will allow our user to update their name
     */
    @Query("UPDATE Rider SET rider_name= :newName WHERE phonenumber = :username")
    void updateRiderName(int username, String newName);

    /*
    This method will return us the rider object
     */
    @Query("SELECT * FROM rider")
    LiveData<rider> getRider();
}

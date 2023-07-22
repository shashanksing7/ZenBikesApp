package www.zentorc.com.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/*
This Entity cis going to represent rider table in which we are going to add details about the user.
 */
@Entity
public class rider {
    /*
    The below properties are going to represent the column of our table.
     */
    @PrimaryKey
    @NotNull
    private String phonenumber;
    @ColumnInfo
    private String rider_name;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String gender;
    @ColumnInfo
    private String secret_keyString;

    /*
    Creating getter and setter methods.
     */

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSecret_keyString() {
        return secret_keyString;
    }

    public void setSecret_keyString(String secret_keyString) {
        this.secret_keyString = secret_keyString;
    }

    /*
    creating parameterized and non parameterized constructor
     */

    public rider() {
    }

    public rider(String phonenumber, String rider_name, String email, String gender, String secret_keyString) {
        this.phonenumber = phonenumber;
        this.rider_name = rider_name;
        this.email = email;
        this.gender = gender;
        this.secret_keyString = secret_keyString;
    }
}

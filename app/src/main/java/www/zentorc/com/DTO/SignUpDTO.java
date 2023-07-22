package www.zentorc.com.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/*
This class is going us to help us to map our user entered details during sign up to an
json object that we will send to the server.we implement parcelable so that an object of this
class can be passed using safe args .
 */

public class SignUpDTO implements Parcelable {
    /*
    creating the properties that we will represent the fields of our
    sign up form
     */

    private String phonenumber;
    private String rider_name;
    private String email;
    private String gender;
    private String secret_keyString;

    /*
    Creating getter and setter methods.
     */

    protected SignUpDTO(Parcel in) {
        phonenumber = in.readString();
        rider_name = in.readString();
        email = in.readString();
        gender = in.readString();
        secret_keyString = in.readString();
    }

    public static final Creator<SignUpDTO> CREATOR = new Creator<SignUpDTO>() {
        @Override
        public SignUpDTO createFromParcel(Parcel in) {
            return new SignUpDTO(in);
        }

        @Override
        public SignUpDTO[] newArray(int size) {
            return new SignUpDTO[size];
        }
    };

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

    public SignUpDTO(String phonenumber, String rider_name, String email, String gender, String secret_keyString) {
        this.phonenumber = phonenumber;
        this.rider_name = rider_name;
        this.email = email;
        this.gender = gender;
        this.secret_keyString = secret_keyString;
    }

    public SignUpDTO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(phonenumber);
        dest.writeString(rider_name);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(secret_keyString);
    }
}

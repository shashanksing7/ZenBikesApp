package www.zentorc.com.DTO;
/*
This class is going to Represent the DTO that will help our user to login to server.
All the details entered by the user during the Login process will be mapped to the properties
of this class.
 */
public class UserLoginDTO {

    /*
    Creating properties
     */
    private String phonenumber;

    /*
     * creating Getter and Setter methods
     */

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    /*
     * creating Parameterized and non parameterized constructor
     */

    public UserLoginDTO(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public UserLoginDTO() {
    }
}

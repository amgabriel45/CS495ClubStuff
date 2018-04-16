package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ApiAuthDto {

    @Expose
    @SerializedName("token")
    public String token;
    @Expose
    @SerializedName("user")
    public UserDto user;

}
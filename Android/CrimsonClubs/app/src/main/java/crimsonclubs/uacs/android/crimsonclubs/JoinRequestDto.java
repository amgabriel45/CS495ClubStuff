package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinRequestDto {

    @Expose
    @SerializedName("userId")
    public int userId;
    @Expose
    @SerializedName("first")
    public String first;
    @Expose
    @SerializedName("last")
    public String last;


}

package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MemberDto {

    @Expose
    @SerializedName("id")
    public int id;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("first")
    public String first;
    @Expose
    @SerializedName("last")
    public String last;
    @Expose
    @SerializedName("isGroupAdmin")
    public boolean isGroupAdmin;

}

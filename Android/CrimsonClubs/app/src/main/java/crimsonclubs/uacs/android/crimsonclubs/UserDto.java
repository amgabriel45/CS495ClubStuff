package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserDto {

    @Expose
    @SerializedName("id")
    public int id;
    @Expose
    @SerializedName("first")
    public String first;
    @Expose
    @SerializedName("last")
    public String last;
    @Expose
    @SerializedName("isOrganizationAdmin")
    public boolean isOrganizationAdmin;
    @Expose
    @SerializedName("organizationId")
    public int organizationId;

}
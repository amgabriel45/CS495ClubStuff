package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDto {

    @Expose
    @SerializedName("id")
    public int id;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("description")
    public String description;
    @Expose
    @SerializedName("OrganizationId")
    public int orgId;
    @Expose
    @SerializedName("clubcount")
    public int clubCount;

}

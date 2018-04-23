package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddClubDto {

    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("description")
    public String description;
    @Expose
    @SerializedName("isRequestToJoin")
    public boolean isRequestToJoin;
    @Expose
    @SerializedName("groupId")
    public int groupId;

}

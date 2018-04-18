package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatGroupDto {

    @Expose
    @SerializedName("groupId")
    public int groupId;
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
    @SerializedName("abbreviation")
    public String abbrev;
    @Expose
    @SerializedName("type")
    public StatType type;
}

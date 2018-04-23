package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public class EditEventDto {

    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("description")
    public String description;
    @Expose
    @SerializedName("start")
    public String start;
    @Expose
    @SerializedName("finish")
    public String finish;
    @Expose
    @SerializedName("isGroupEvent")
    public boolean isGroupEvent;
    @Expose
    @SerializedName("clubId")
    public int clubId;
    @Expose
    @SerializedName("clubIds")
    public Collection<Integer> clubIds;
}

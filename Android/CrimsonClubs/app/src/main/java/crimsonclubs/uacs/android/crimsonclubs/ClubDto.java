package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ClubDto {

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
    @SerializedName("isRequestToJoin")
    public boolean isRequestToJoin;
    @Expose
    @SerializedName("groupId")
    public int groupId;
    @Expose
    @SerializedName("groupName")
    public String groupName;
    @Expose
    @SerializedName("memberCount")
    public int memberCount;
    @Expose
    @SerializedName("isAccepted")
    public boolean isAccepted;
    @Expose
    @SerializedName("hasRequested")
    public boolean hasRequested;
    @Expose
    @SerializedName("isAllowedToJoin")
    public boolean isAllowedToJoin;
    @Expose
    @SerializedName("members")
    public ArrayList<MemberDto> members;
    @Expose
    @SerializedName("events")
    public ArrayList<EventDto> events;
}
package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

public class EventDto {

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
    @SerializedName("clubName")
    public String clubName;
    @Expose
    @SerializedName("clubs")
    public ArrayList<ClubDto> clubs;
    @Expose
    @SerializedName("clubStats")
    public ArrayList<StatClubDto> clubStats;
    @Expose
    @SerializedName("groupStats")
    public ArrayList<StatGroupDto> groupStats;
    @Expose
    @SerializedName("usersClubStats")
    public ArrayList<UserStatsDto> usersClubStats;
    @Expose
    @SerializedName("usersGroupStats")
    public ArrayList<UserStatsDto> usersGroupStats;
}

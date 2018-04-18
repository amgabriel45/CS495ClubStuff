package crimsonclubs.uacs.android.crimsonclubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class UserStatsDto {

    @Expose
    @SerializedName("userId")
    public int userId;
    @Expose
    @SerializedName("first")
    public String first;
    @Expose
    @SerializedName("last")
    public String last;
    @Expose
    @SerializedName("stats")
    public Map<Integer,Integer> stats;
}

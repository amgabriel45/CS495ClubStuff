package crimsonclubs.uacs.android.crimsonclubs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class InternalModeSQLiteHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "config.db";
    private static final int DATABASE_VERSION = 1;

    public InternalModeSQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.setForcedUpgrade();
    }

    public Cursor getItemsAtLevel(int parentId) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Cursor c;

        if(parentId <= 0) {
            c = db.rawQuery("SELECT id,(select name from sites where sites.id = site_id) || \": \" || description as description, name, case when sort is null then 0 else sort end as sort, \"group\" as type FROM groups where parent_id = 0 or parent_id is null union all SELECT id,(select name from sites where sites.id = site_id) || \": \" || description as description, name, case when sort is null then 0 else sort end as sort, \"poi\" as type FROM poi_config where group_id = 0 or group_id is null order by sort",
                    null);
        }
        else{
            c = db.rawQuery("SELECT id,(select name from sites where sites.id = site_id) || \": \" || description as description, name, case when sort is null then 0 else sort end as sort, \"group\" as type FROM groups where parent_id = ? union all SELECT id,(select name from sites where sites.id = site_id) || \": \" || description as description, name, case when sort is null then 0 else sort end as sort, \"poi\" as type FROM poi_config where group_id = ? and \"view_mode\" > '0' order by sort",
                            new String[] {Integer.toString(parentId), Integer.toString(parentId)});
        }


        c.moveToFirst();
        return c;

    }

    public Cursor getPoiConfig(int id){

        SQLiteDatabase db = getReadableDatabase();
        Cursor c;

        c = db.rawQuery("select id, flags,case when sort is null then 0 else sort end as sort,case when group_id is null then 0 else group_id end as group_id,chart_type,name,description, view_mode from poi_config WHERE \"id\" = ? and view_mode > '0'",
                   new String[] {Integer.toString(id)});



        c.moveToFirst();
        return c;

    }

    public Cursor getPoiDebug(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;

        c = db.rawQuery("select d.id, d.name, case when d.sort is null then 0 else d.sort end as sort,server_type,server_path,server_query,high_range,low_range,data_type, units,on_text,off_text,color,on_text_color,off_text_color,update_frequency,format from data_points d, poi_config_relations r, poi_config p WHERE d.id = r.datapoint_id and r.poi_config_id = ? and p.id = ? and p.view_mode > '0'",
                        new String[] {Integer.toString(id), Integer.toString(id)});

        c.moveToFirst();
        return c;
    }

    public Cursor getInfo(int id,@Nullable Integer categoryId){

        SQLiteDatabase db = getReadableDatabase();
        Cursor c;

        if(categoryId == null) {
            c = db.rawQuery("Select distinct c.id as id, '0' as sort,'0' as OpenMode, i.Audit as Audit, \"category\" as type, c.name as key, c.name as value, c.description as description from info i, info_relations r, info_category c, poi_config p WHERE i.id = r.info_id and r.poi_id = ? and p.id = ? and p.view_mode > '0' and i.verified = 1 and category_id =c.id union ALL Select i.id, case when i.sort is null then 0 else i.sort end as sort,i.OpenMode as OpenMode, i.Audit as Audit, type,key, value, i.description as description from info i, info_relations r, poi_config p  WHERE i.id = r.info_id and r.poi_id = ? and p.id = ? and p.view_mode > '0' and i.verified = 1 and category_id is NULL order by sort",
                    new String[]{Integer.toString(id), Integer.toString(id), Integer.toString(id), Integer.toString(id)});
        }
        else{
            c = db.rawQuery("Select id, case when sort is null then 0 else sort end as sort, OpenMode,Audit,type, key, value, description from info i, info_relations r WHERE i.id = r.info_id and r.poi_id = ? and category_id = ?",
                    new String[] {Integer.toString(id) , Integer.toString(categoryId)});
        }

        c.moveToFirst();
        return c;
    }

    public JSONArray getJson(Cursor cursor){
        JSONArray resultSet  = new JSONArray();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                             Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString() );
        return resultSet;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "data_points");
        db.execSQL("DROP TABLE IF EXISTS " + "groups");
        db.execSQL("DROP TABLE IF EXISTS " + "info");
        db.execSQL("DROP TABLE IF EXISTS " + "info_category");
        db.execSQL("DROP TABLE IF EXISTS " + "info_relations");
        db.execSQL("DROP TABLE IF EXISTS " + "poi_config");
        db.execSQL("DROP TABLE IF EXISTS " + "poi_config_relations");
        db.execSQL("DROP TABLE IF EXISTS " + "sites");


        this.onCreate(db);
    }

}
package com.tcv.peliculas.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.tcv.peliculas.R;
import com.tcv.peliculas.model.LoginCredentials;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alumno on 27/6/2018.
 */

public class PersistHelper {

    public static String encodeToBase64(Bitmap image){
        Bitmap profilePic =  image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profilePic.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    };
    public static Bitmap decodeToBase64(String input){
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }
    public static void persistirImagenPorBitMap(SharedPreferences preferences,Bitmap imagen)
    {
        SharedPreferences myPreferences = preferences;
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString("imagePreference", PersistHelper.encodeToBase64(imagen));
        editor.commit();
    }

    public static void persistirImagenByString(SharedPreferences preferences,String imagen)
    {
        SharedPreferences myPreferences = preferences;
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString("imagePreference", imagen);
        editor.commit();
    }

/*
    public static String getPreferencesKeyByValue(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> prefsMap = preferences.getAll();
        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            Log.v("SharedPreferences", entry.getKey() + ":" +
                    entry.getValue().toString());
        }
        return preferences.getString(key, null);
    }

*/
public static HashSet<String> getPreferencesCollectionByKey(String internalKey, String sharedKey, Context context)
{
    SharedPreferences sharedPreferences = context.getSharedPreferences(sharedKey,Context.MODE_PRIVATE);
    HashSet<String> response = (HashSet<String>) sharedPreferences.getStringSet(internalKey,null);
    return response;
}

    public static void setPreferencesKeyByValue(String internalKey, String value, String sharedKey, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedKey,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(internalKey,value);
        editor.commit();
    }

    public static void setPreferencesKeyByValues(String internalKey, HashSet<String> values, String sharedKey, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedKey,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(internalKey,values);
        editor.commit();
    }
    public static String getPreferencesKeyByValue(String internalKey, Context context, String sharedKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedKey,Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(internalKey, null);
        return value;
    }

}

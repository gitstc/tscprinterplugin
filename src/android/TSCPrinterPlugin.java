package com.stc.tscprinter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.os.Environment;

public class TSCPrinterPlugin extends CordovaPlugin {
    static final String TAG = "TSCPrinterPlugin";
    PrinterPlugin printerPlugin;

    @Override
    public boolean execute(String action, final JSONArray data, final CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "execute " + action);

        if(printerPlugin == null){
            printerPlugin = new PrinterPlugin(cordova.getActivity().getApplicationContext());
            Log.d(TAG, "Created new instance");
        }

        if (action.equals("PrintImage")){
            final String imageData = data.optString(0);
            final String printerAddress = data.optString(1);

            final int x = data.optInt(2);
            final int y = data.optInt(3);
            final int width = data.optInt(4);
            final int height = data.optInt(5);

            Log.d(TAG, "x: " + x + ", y: " + y + ", w: " + width + ", h: " + height);

            if(imageData == ""){
                callbackContext.error("No data found!");
                return true;
            }
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(printerPlugin.printBitmap(printerAddress,imageData,x,y,width,height)){
                            callbackContext.success();
                        }
                        else{
                            callbackContext.error("Could not print image");
                        }
                    }
                    catch (Exception ex){
                        Log.d(TAG,"Print Image Exception: " + ex.getMessage());
                        callbackContext.error(ex.getMessage());
                    }
                }
            }).run();

            return true;
        } else {
                return false;
        }
    }
}
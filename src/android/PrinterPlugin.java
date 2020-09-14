package com.stc.tscprinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.tscdll.TSCActivity;

public class PrinterPlugin {
    static final String TAG = "TSCPrinterPlugin";

    Context context;
    TSCActivity BTActivity;

    public PrinterPlugin(Context context) {
        this.context = context;
        this.BTActivity = new TSCActivity();

        Log.d(TAG, "Printer Plugin Initialized");
    }

    public boolean printBitmap(String macAddress, String base64, int x, int y, int w, int h){
        Log.d(TAG, "Print Bitmap -> x: " + x + ", y: " + y + ", w: " + w + ".0, h: " + h + ".0");

        if(base64 == null || base64.equals("")){
            Log.d(TAG, "Could not process image!");
            return false;
        }

        base64 = base64.split(",")[1];

        Bitmap bitmap = getBitmap(base64);
        if(bitmap == null) {
            Log.d(TAG, "Could not process image!");
            return false;
        }

        return printBitmap(macAddress, bitmap, x, y, w, h);
    }

    public boolean printBitmap(String macAddress, Bitmap bitmap, int x, int y, int w, int h){
        Log.d(TAG, "Print Bitmap -> x: " + x + ", y: " + y + ", w: " + w + ", h: " + h);

        if(bitmap == null) {
            Log.d(TAG, "Could not process image!");
            return false;
        }

        double ratio = (double) 72 / (double) (bitmap.getWidth());
        int width = 72;
        int height = (int) (bitmap.getHeight() * ratio);
        Log.d(TAG, "Width: " + width);
        Log.d(TAG, "Height: " + height);
        Log.d(TAG, "Ratio: " + ratio);
        //width = 72;
        //height = (int)(height * ratio);

        Log.d(TAG, "SIZE " + width + "mm, " + height + "mm\r\n");

        try{
            BTActivity.openport_without_security(macAddress);
            BTActivity.clearbuffer();
            BTActivity.sendcommand("SIZE " + width + "mm, " + height + "mm\r\n");
            BTActivity.sendcommand("GAP 0 mm, 0 mm\r\n");
            BTActivity.sendcommand("SPEED 2\r\n");
            BTActivity.sendcommand("DENSITY 10\r\n");
            BTActivity.sendcommand("DIRECTION 1\r\n");
            BTActivity.sendcommand("SET TEAR ON\r\n");
            BTActivity.sendcommand("SET COUNTER @1 1\r\n");
            //BTActivity.setup(0, 0, 2, 10, 1, 0, 0);
            //BTActivity.sendbitmap(x, y, bitmap);

            ratio = 600 / (double) bitmap.getWidth();
            height = (int) (bitmap.getHeight() * ratio);

            Log.d(TAG, "Resizing to 600x" + height);

            BTActivity.sendbitmap_resize(x, y, bitmap, 600, height);
            BTActivity.printlabel(1, 1);
            BTActivity.closeport(15000);

            return true;
        } catch (Exception ex){
            Log.d(TAG, "PrintImage Exception: " + ex.getMessage());
        }

        return false;
    }

    private Bitmap getBitmap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bmp;
    }

}

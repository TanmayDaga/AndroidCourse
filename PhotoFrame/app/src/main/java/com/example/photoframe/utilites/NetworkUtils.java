package com.example.photoframe.utilites;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class NetworkUtils{
    private static final String TAG = NetworkUtils.class.getSimpleName();


    /*

    *
    Required urls
    https://stackoverflow.com/questions/39394213/recyclerview-with-gridlayoutmanager-inside-recyclerview-with-linearlayoutmanager
    https://stackoverflow.com/questions/57565080/images-in-recycler-view-with-grid-layout-stretches
    https://stackoverflow.com/questions/21919624/get-image-content-from-httpresponse-in-android
    */
    private static final String URL = "https://source.unsplash.com/random";
    private static Bitmap[] returnArray;
    private static Byte[] readFromStream(InputStream inputStream) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int BufferSize = 1024;
        byte[] buffer = new byte[BufferSize];
        int len = 0;
        try {
            while ((len =  inputStream.read(buffer)) != -1){
                baos.write(buffer,0,len);
            }
            baos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
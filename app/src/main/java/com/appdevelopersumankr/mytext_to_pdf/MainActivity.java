package com.appdevelopersumankr.mytext_to_pdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btnCreate;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        takepermissions( );


        try {
            createpdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
    }

    private void createpdf() throws FileNotFoundException {
        String pdfpath= Environment.getExternalStoragePublicDirectory ( Environment.DIRECTORY_DOWNLOADS).toString ();
        File file=new File ( pdfpath,"mypdf.pdf");
        OutputStream outputStream=new FileOutputStream ( file);

        PdfWriter writer=new PdfWriter ( file);
        PdfDocument pdfDocument=new PdfDocument ( writer);
        Document document=new Document ( pdfDocument);

        Paragraph paragraph=new Paragraph ("hello suman ");
        document.add ( paragraph);
        document.close ();
        Toast.makeText ( this, "PDF Created  -->", Toast.LENGTH_LONG ).show ();
    }





//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
//    android:maxSdkVersion="30"></uses-permission>
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE">
//    </uses-permission>
//    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"></uses-permission>


    //////////////////////////////////////////////////////////////////////////////
//     <application
//
//    android:requestLegacyExternalStorage="true"
//
//    </application>


    ////////////////////////////////////////////////////////
    public void takepermissions(){
        if (ispermissiongranted ()){
            Toast.makeText ( this, "permission already granted ", Toast.LENGTH_SHORT ).show ();
        }else {
            takepermission ();
        }

    }
    public boolean ispermissiongranted(){
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
            //for android 10   11
            return Environment.isExternalStorageManager ();
        }
        else {
            //below 10 and 11
            int readexternalstoragepermission= ContextCompat.checkSelfPermission ( this, Manifest.permission.READ_EXTERNAL_STORAGE );
            return readexternalstoragepermission== PackageManager.PERMISSION_GRANTED;
        }
    }
    public void takepermission(){
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
            try {
                Intent intent=new Intent ( Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION );
                intent.addCategory ( "android.intent.category.DEFAULT");
                intent.setData ( Uri.parse ( String.format ( "package:%s",getApplicationContext ().getPackageName () ) ) );
                startActivityForResult ( intent,100 );
            } catch (Exception e) {
                Intent intent=new Intent (  );
               intent.setAction ( Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult ( intent,100 );
            }
        }
        else {
            ActivityCompat.requestPermissions ( this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101 );

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (resultCode==RESULT_OK){
            if (requestCode==100){
                if (Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
                    if (Environment.isExternalStorageManager ()){
                        Toast.makeText ( this, "permission granted ", Toast.LENGTH_SHORT ).show ();
                    }else {
                        takepermission ();
                    }
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );
            if (grantResults.length>0){
                if (requestCode==101){
                    boolean readexternalstorage=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (readexternalstorage){
                        Toast.makeText ( this, "read per in android 10 or below", Toast.LENGTH_SHORT ).show ();
                    }
                    else {
                        takepermission ();
                    }
                }

            }
    }
}
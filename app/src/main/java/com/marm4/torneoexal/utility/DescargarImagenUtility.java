package com.marm4.torneoexal.utility;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;


import com.marm4.torneoexal.listener.DescargaExito;

import java.io.File;

public class DescargarImagenUtility {

    public static void getEscudo(File directory, Context context, String fileName, String urlImagen, Boolean retorno, DescargaExito listener){
        File file = new File(directory, fileName);
        Log.i("DownloaderUtility", "New DownloaderUtility create");
        if(urlImagen == null || urlImagen.isEmpty()){
            listener.descargaExito(null, retorno);
            return;
        }

        else if(file.exists()){
            Log.i("DownloaderUtility", "File exist");
            listener.descargaExito(Uri.fromFile(file), retorno);
        }
        else{
            Log.i("DownloaderUtility", "File no exist");
            downloadEscudo(context, fileName, file, urlImagen, retorno, listener);
        }

    }

    private static void downloadEscudo(Context context, String fileName, File file, String urlImagen, Boolean retorno, DescargaExito listener){
        BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.i("DownloaderUtility", "Success on downloading pp");
                    listener.descargaExito(Uri.fromFile(file), retorno);
                }
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);

        Log.i("DownloaderUtility", "Getting picture");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlImagen))
                .setTitle("Escudo")
                .setDescription("Descargando")
                .setDestinationInExternalFilesDir(context, "Torneo-Exal", fileName);
        downloadManager.enqueue(request);
    }
}

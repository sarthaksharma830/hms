package com.example.sarthak.hms;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import com.example.sarthak.hms.models.ComplaintPicture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {
    public static int getRandomColor() {
        int colors[] = new int[]{R.color.lightBlue, R.color.pink, R.color.yellow, R.color.orange, R.color.green, R.color.red, R.color.violet, R.color.lightRed, R.color.lightGreen};
        Random random = new Random();
        int max = colors.length - 1;
        int min = 0;
        return colors[random.nextInt((max - min) + 1) + min];
    }

    public static List<ComplaintPicture> uriListToPicturesList(Context context, List<Uri> pictures) throws IOException {
        List<ComplaintPicture> complaintPictures = new ArrayList<>();

        for (Uri pic : pictures) {
            InputStream inputStream = null;
            String ext = "";
            if (pic.toString().startsWith("content")) {
                inputStream = context.getContentResolver().openInputStream(pic);
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                ext = mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(pic));
            } else {
                File f = new File(pic.toString());
                int i = f.getName().lastIndexOf('.');
                if (i > 0) {
                    ext = f.getName().substring(i+1);
                }
                inputStream = new FileInputStream(f);
            }

            if (inputStream != null) {
                byte[] bytes = toByteArray(inputStream);
                String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                ComplaintPicture complaintPicture = new ComplaintPicture();
                complaintPicture.setFileName("xyz." + ext);
                complaintPicture.setData(base64);
                complaintPictures.add(complaintPicture);
            }
        }

        return complaintPictures;
    }

    private static byte[] toByteArray(@NonNull InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        byte[] bytes = buffer.toByteArray();
        buffer.flush();
        inputStream.close();
        buffer.close();
        return bytes;
    }
}

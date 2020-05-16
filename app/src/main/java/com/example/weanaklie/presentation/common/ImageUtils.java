package com.example.weanaklie.presentation.common;

import android.app.Activity;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by goda on 29/01/2018.
 */

public class ImageUtils {
    public static Uri fileUri;
    public static Uri fileCompressToUploadSection;

    public static Uri fileUriSavedFixed;
    static final String IMAGE_DIRECTORY_NAME = "Saved_Friendy_Images";
    static File file;
    static String filePath;
    public static File myDir;
    public static File Dir_cache;

    //save image in external or internal storage
    public static Uri saveImageToStorage(Bitmap finalBitmap, Activity activity, int quality) {
        ContextWrapper wrapper;
        String root;
//        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        // Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
      /*  if (isSDSupportedDevice && isSDPresent) {// yes SD-card is present
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            myDir = new File(root, IMAGE_DIRECTORY_NAME);
        } else {
            // save it internal storage
            wrapper = new ContextWrapper(activity);
            myDir = wrapper.getDir(IMAGE_DIRECTORY_NAME, MODE_PRIVATE);
        }*/

        // save it internal storage
        wrapper = new ContextWrapper(activity);
        // myDir = wrapper.getFilesDir();
        myDir = wrapper.getDir(IMAGE_DIRECTORY_NAME, MODE_PRIVATE);
        // Create a media file name
        // Create the storage directory if it does not exist
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(new Date());
        String fname = generateRandomIntIntRange(1, 9999999) + timeStamp + ".jpg";
        file = new File(myDir, fname);
        if (file.exists()) file.delete();
        //  String fname2 = "Imagsde-" + timeStamp + ".jpg";
        //  file2 = new File(myDir, fname2);
        // if (file2.exists()) file2.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            //  FileOutputStream out2 = new FileOutputStream(file2);
            fileUri = Uri.fromFile(file);
            filePath = file.getAbsolutePath();
            //finalBitmap = new Compressor(activity).compressToBitmap(file);

            finalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            // Bitmap fj=new Compressor(activity).compressToBitmap(file);
        /*    BitmapFactory.Options Options = new BitmapFactory.Options();
            Options.inSampleSize = 1;
            Options.inJustDecodeBounds = false;
            finalBitmap = BitmapFactory.decodeFile(filePath, Options);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
*/
            //  fj.compress(Bitmap.CompressFormat.JPEG, 100, out2);
            // file = new Compressor(activity).compressToFile(file);
            finalBitmap.recycle();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return fileUri;

    }





    //retrieve location for image as url
    public static Uri getUrl() {
        return fileUri;
    }

    public static Uri getfileToUploadSection() {
        return fileCompressToUploadSection;
    }

    public static File getFile() {
        return file;
    }

    public static String getPathFile() {
        return filePath;
    }

    public static void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getFileName(Uri uri, Activity context) {

        String scheme = uri.getScheme();
        String fileName = "";
        if (scheme.equals("file")) {
            fileName = uri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Images.Media.TITLE};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileName;
    }

    public static Uri saveImageWithoutComp(byte[] finalBitmap, Activity activity) {
        ContextWrapper wrapper;
        File myDir;
        // save it internal storage
        wrapper = new ContextWrapper(activity);
        // myDir = wrapper.getFilesDir();
        myDir = wrapper.getDir(IMAGE_DIRECTORY_NAME, MODE_PRIVATE);
        // Create a media file name
        // Create the storage directory if it does not exist
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fname = "Image-" + timeStamp + ".jpg";
        file = new File(myDir, fname);
        if (file.exists()) file.delete();
        //  String fname2 = "Imagsde-" + timeStamp + ".jpg";
        //  file2 = new File(myDir, fname2);
        // if (file2.exists()) file2.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            //  FileOutputStream out2 = new FileOutputStream(file2);
            fileUri = Uri.fromFile(file);
            filePath = file.getAbsolutePath();
            out.write(finalBitmap);
            //finalBitmap = new Compressor(activity).compressToBitmap(file);

            // finalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            // Bitmap fj=new Compressor(activity).compressToBitmap(file);

            //  fj.compress(Bitmap.CompressFormat.JPEG, 100, out2);
            // file = new Compressor(activity).compressToFile(file);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return fileUri;

    }
    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}

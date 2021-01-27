package com.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class GetSampledImage extends AsyncTask<String, Void, File> {
    private OnImageSampledListener listener;

    /**
     * Sample an image file in the same thread that it is called.
     */
    @Nullable
    public static File sampleImageSync(@NonNull String imagePath, @NonNull String imageDirectory, int requiredImageSize) {
        try {
            if (!imagePath.trim().isEmpty()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath, options);
                options.inSampleSize = calculateInSampleSize(options, requiredImageSize, requiredImageSize);
                options.inJustDecodeBounds = false;
                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath, options);

                imageBitmap = rotateImageIfRequired(imageBitmap, imagePath);

                if (imageBitmap != null) {
                    return getImageFile(imageBitmap, imageDirectory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options        An options object with out* params already populated (run through a decode*
     *                       method with inJustDecodeBounds==true
     * @param requiredWidth  The requested width of the resulting bitmap
     * @param requiredHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int requiredWidth, int requiredHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) requiredHeight);
            final int widthRatio = Math.round((float) width / (float) requiredWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = Math.min(heightRatio, widthRatio);

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = requiredWidth * requiredHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Bitmap img, String selectedImage) throws IOException {
        ExifInterface ei = new ExifInterface(selectedImage);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private static File getImageFile(Bitmap bitmap, String imageDirectory) {
        try {
            File mediaStorageDir = new File(imageDirectory);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            File imageFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + UUID.randomUUID().toString() + ".jpg");

            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setListener(@NonNull OnImageSampledListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        listener = null;
    }

    public void sampleImage(@NonNull String imagePath, @NonNull String imageDirectory, int requiredImageSize) {
        execute(imagePath, imageDirectory, String.valueOf(requiredImageSize));
    }

    /**
     * Params:
     * 0 - imagePath (String)
     * 1 - imageDirectory (String)
     * 2 - requiredImageSize (String)
     */
    @Override
    protected File doInBackground(String... params) {
        final String imagePath = params[0];
        final String imageDirectory = params[1];
        final int requiredImageSize = Integer.parseInt(params[2]);

        return sampleImageSync(imagePath, imageDirectory, requiredImageSize);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (listener != null && file != null) {
            listener.onImageSampled(file);
        }
    }

    public interface OnImageSampledListener {
        void onImageSampled(@NonNull File imageFile);
    }
}
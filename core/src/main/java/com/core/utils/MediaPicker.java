package com.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.core.R;
import com.core.extensions.ContextExtensionsKt;
import com.core.ui.custom.AppAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

public class MediaPicker {
    private static final String TYPE_IMAGE = "image/*";
    private static final String[] MIME_TYPES_IMAGE = {"image/jpeg", "image/jpg", "image/png"};

    @NonNull
    private final String cacheDirectory;
    private File mediaFile;
    /**
     * Activity object that will be used while calling startActivityForResult(). Activity then will
     * receive the callbacks to its own onActivityResult() and is responsible of calling the
     * onActivityResult() of the MediaPicker for handling result and being notified.
     */
    private Activity context;
    /**
     * Fragment object that will be used while calling startActivityForResult(). Fragment then will
     * receive the callbacks to its own onActivityResult() and is responsible of calling the
     * onActivityResult() of the MediaPicker for handling result and being notified.
     */
    private Fragment fragment;
    private AlertDialog pickerDialog;
    private ImagePickerListener imagePickerListener;
    private VideoPickerListener videoPickerListener;
    private boolean allowVideo = false;

    public MediaPicker(@NonNull Activity activity) {
        this.context = activity;
        cacheDirectory = FileUtils.getAppCacheDirectoryPath(activity);
    }

    public MediaPicker(@NonNull Fragment fragment) {
        this.fragment = fragment;
        this.context = fragment.requireActivity();
        cacheDirectory = FileUtils.getAppCacheDirectoryPath(context);
    }

    public void setImagePickerListener(@NonNull ImagePickerListener imagePickerListener) {
        this.imagePickerListener = imagePickerListener;
    }

    public void setVideoPickerListener(VideoPickerListener videoPickerListener) {
        this.videoPickerListener = videoPickerListener;
    }

    public void setAllowVideo(boolean allowVideo) {
        this.allowVideo = allowVideo;
    }

    /**
     * Removes all listeners and references
     */
    public void clear() {
        dismiss();
        this.imagePickerListener = null;
        this.context = null;
        this.fragment = null;
        this.mediaFile = null;
        this.pickerDialog = null;
    }

    private void setupPickerDialog() {
        String[] pickerItems;

        if (allowVideo) {
            pickerItems = new String[]{context.getString(R.string.media_picker_dialog_capture_image),
                    context.getString(R.string.media_picker_dialog_gallery_image),
                    context.getString(R.string.media_picker_dialog_capture_video),
                    context.getString(R.string.media_picker_dialog_gallery_video)};
        } else {
            pickerItems = new String[]{context.getString(R.string.media_picker_dialog_capture_image),
                    context.getString(R.string.media_picker_dialog_gallery_image)};
        }

        AlertDialog.Builder builder = new AppAlertDialogBuilder(context, null, R.style.AppTheme_MaterialAlertDialog);
        builder.setItems(pickerItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    openImageCamera();
                    break;

                case 1:
                    openGallery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, AppConstants.REQUEST_CODE_GALLERY_IMAGE);
                    break;

                case 2:
                    if (allowVideo) {
                        openVideoCamera();
                    }
                    break;

                case 3:
                    if (allowVideo) {
                        openGallery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, AppConstants.REQUEST_CODE_GALLERY_VIDEO);
                    }
                    break;
            }
            dialog.dismiss();
        });
        pickerDialog = builder.create();
    }

    public void show() {
        if (FileUtils.isLowStorage(context)) {
            ContextExtensionsKt.longToast(context, R.string.media_picker_message_please_free_up_space);
            return;
        }

        if (pickerDialog == null) {
            setupPickerDialog();
        }

        if (pickerDialog != null) {
            pickerDialog.show();
        }
    }

    public void dismiss() {
        if (pickerDialog != null && pickerDialog.isShowing()) {
            pickerDialog.dismiss();
        }
    }

    /**
     * Handles the result of events that the Activity or Fragment receives on its own
     * onActivityResult(). This method must be called inside the onActivityResult()
     * of the container Activity or Fragment.
     */
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case AppConstants.REQUEST_CODE_GALLERY_IMAGE:
                if (data != null && data.getData() != null) {
                    String imagePath = getPathFromGallery(context, data.getData());
                    if (imagePath != null) {
                        mediaFile = new File(imagePath);
                        Timber.i("Media size : %s", Formatter.formatShortFileSize(context, mediaFile.length()));
                        imagePickerListener.onImageSelectedFromPicker(mediaFile);
                    }
                }
                break;

            case AppConstants.REQUEST_CODE_GALLERY_VIDEO:
                if (data != null && data.getData() != null && !FileUtils.isGooglePhotosUri(data.getData())) {
                    String videoPath = FileUtils.getPath(context, data.getData());
                    if (videoPath != null) {
                        mediaFile = new File(videoPath);
                        Timber.i("Media size : %s", Formatter.formatShortFileSize(context, mediaFile.length()));
                        videoPickerListener.onVideoSelectedFromPicker(mediaFile);
                    }
                }
                break;

            case AppConstants.REQUEST_CODE_CAMERA_IMAGE:
                if (mediaFile != null) {
                    Timber.i("Media size : %s", Formatter.formatShortFileSize(context, mediaFile.length()));
                    imagePickerListener.onImageSelectedFromPicker(mediaFile);
                    revokeUriPermission();
                }
                break;

            case AppConstants.REQUEST_CODE_CAMERA_VIDEO:
                if (mediaFile != null) {
                    Timber.i("Media size : %s", Formatter.formatShortFileSize(context, mediaFile.length()));
                    videoPickerListener.onVideoSelectedFromPicker(mediaFile);
                    revokeUriPermission();
                }
                break;
        }
    }

    /**
     * Save the image to device external cache
     */
    private void openImageCamera() {
        checkListener();
        startCameraImageIntent(cacheDirectory);
    }

    /**
     * Save the image to a custom directory
     */
    private void openImageCamera(@NonNull final String imageDirectory) {
        checkListener();
        startCameraImageIntent(imageDirectory);
    }

    private void startCameraImageIntent(@NonNull final String videoDirectory) {
        try {
            String fileName = "IMG_" + UUID.randomUUID().toString();
            String fileSuffix = ".jpg";
            mediaFile = createFile(videoDirectory, fileName, fileSuffix);

            if (fragment == null)
                context.startActivityForResult(getCameraIntent(mediaFile, MediaStore.ACTION_IMAGE_CAPTURE),
                        AppConstants.REQUEST_CODE_CAMERA_IMAGE);
            else
                fragment.startActivityForResult(getCameraIntent(mediaFile, MediaStore.ACTION_IMAGE_CAPTURE),
                        AppConstants.REQUEST_CODE_CAMERA_IMAGE);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Save the video to device external cache
     */
    private void openVideoCamera() {
        checkListener();
        startCameraVideoIntent(cacheDirectory);
    }

    private void startCameraVideoIntent(@NonNull final String imageDirectory) {
        try {
            String fileName = "VID_" + UUID.randomUUID().toString();
            String fileSuffix = ".mp4";
            mediaFile = createFile(imageDirectory, fileName, fileSuffix);

            if (fragment == null)
                context.startActivityForResult(getCameraIntent(mediaFile, MediaStore.ACTION_VIDEO_CAPTURE),
                        AppConstants.REQUEST_CODE_CAMERA_VIDEO);
            else
                fragment.startActivityForResult(getCameraIntent(mediaFile, MediaStore.ACTION_VIDEO_CAPTURE),
                        AppConstants.REQUEST_CODE_CAMERA_VIDEO);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Returns the camera intent using FileProvider to avoid the FileUriExposedException in Android N and above
     *
     * @param file File for which we need the intent
     */
    private Intent getCameraIntent(File file, @NonNull String action) {
        Intent cameraIntent = new Intent(action);

        // Put the uri of the image file as intent extra
        Uri imageUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider",
                file);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        // Get a list of all the camera apps
        List<ResolveInfo> resolvedIntentActivities =
                context.getPackageManager()
                        .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // Grant uri read/write permissions to the camera apps
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            context.grantUriPermission(packageName, imageUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        return cameraIntent;
    }

    private void openGallery(@NonNull final Uri uri, final int requestCode) {
        checkListener();
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        try {
            intent.setType(TYPE_IMAGE);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, MIME_TYPES_IMAGE);
            if (fragment == null)
                context.startActivityForResult(intent, requestCode);
            else
                fragment.startActivityForResult(intent, requestCode);
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    @Nullable
    public File getMediaFile() {
        return mediaFile;
    }

    @Nullable
    private String getPathFromGallery(@NonNull final Context context, @NonNull final Uri imageUri) {
        String imagePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }

    @Nullable
    private File createFile(@NonNull final String directory, @NonNull final String fileName,
                            @NonNull final String fileSuffix) throws IOException {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File storageDir = new File(directory);
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
            file = File.createTempFile(fileName, fileSuffix, storageDir);
        }
        return file;
    }

    /**
     * Revoke access permission for the content URI to the specified package otherwise the permission won't be
     * revoked until the device restarts.
     */
    private void revokeUriPermission() {
        context.revokeUriPermission(FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider", mediaFile),
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    private void checkListener() {
        if (imagePickerListener == null) {
            throw new RuntimeException("ImagePickerListener must be set before calling openImageCamera() or openImageGallery()");
        }

        if (allowVideo && videoPickerListener == null) {
            throw new RuntimeException("VideoPickerListener must be set before calling openVideoCamera() or openVideoGallery()");
        }
    }

    public interface ImagePickerListener {
        void onImageSelectedFromPicker(@NonNull File imageFile);
    }

    public interface VideoPickerListener {
        void onVideoSelectedFromPicker(@NonNull File videoFile);
    }
}
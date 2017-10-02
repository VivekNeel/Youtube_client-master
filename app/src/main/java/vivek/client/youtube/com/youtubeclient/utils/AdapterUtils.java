package vivek.client.youtube.com.youtubeclient.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import vivek.client.youtube.com.youtubeclient.R;

/**
 * Created by vivekneel on 12/19/16.
 */

public class AdapterUtils {

    private static SweetAlertDialog sweetAlertDialog;

    public static void setImage(String imageUrl, Context context, ImageView imageView) {
        Picasso.with(context).load(imageUrl).fit().into(imageView);
    }


    public static void manageAlertDialog(Context context, boolean shouldShow) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        if (shouldShow) {
            sweetAlertDialog.getProgressHelper().setBarColor(R.color.colorAccent);
            sweetAlertDialog.setTitleText("Loading");
            sweetAlertDialog.show();
        } else {
            sweetAlertDialog.hide();
        }
    }

    public static void openUrlInBrowser(@NonNull String url, @NonNull Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}

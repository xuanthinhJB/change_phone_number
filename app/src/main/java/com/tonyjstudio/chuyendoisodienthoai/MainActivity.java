package com.tonyjstudio.chuyendoisodienthoai;

import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tonyjstudio.chuyendoisodienthoai.model.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog mDialog;
    private TextView mAmountTextView;
    private ProgressBar mStatusProgressBar;
    private final Map<String, String> mPhoneMap = new HashMap<String, String>()
    {{
        // viettel
        put("0162", "032");
        put("0163", "033");
        put("0164", "034");
        put("0165", "035");
        put("0166", "036");
        put("0167", "037");
        put("0169", "039");

        // mobifone
        put("0120", "070");
        put("0121", "079");
        put("0122", "077");
        put("0126", "076");
        put("0128", "078");

        // vinaphone
        put("0123", "083");
        put("0124", "084");
        put("0125", "085");
        put("0127", "081");
        put("0129", "082");

        // vietnamobile
        put("0186", "056");
        put("0188", "058");

        // gmobile
        put("0199", "059");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.layout_start_transfer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_start_transfer) {
            startTransfer();
        }
    }

    /**
     * function to start transfer
     */
    private void startTransfer() {
        showProgressTransferring();
    }

    /**
     * function to show a progress bar
     */
    private void showProgressTransferring() {
        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.MyDialog);
            mDialog.setContentView(R.layout.progress_dialog);
        }

        mDialog.show();

        doTransfer();
    }

    /**
     * function to do the transfer task
     */
    private void doTransfer() {

        if (mDialog.isShowing()) {

            for (Map.Entry<String, String> entry : mPhoneMap.entrySet()) {

                ArrayList<ContentProviderOperation> operationArrayList = new ArrayList<>();
                String where =
                        //ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " ? AND "
                                + String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE).substring(0, entry.getKey().length()) + " = ? ";

                String[] params = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, entry.getKey()};

                operationArrayList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, params)
                        .withValue(ContactsContract.CommonDataKinds.Phone.DATA, entry.getValue())
                        .build());

                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationArrayList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * function to get list contacts
     */
    private ArrayList<Contact> getListContact() {
        ArrayList<ContentProviderOperation> operationArrayList = new ArrayList<>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
    }
}

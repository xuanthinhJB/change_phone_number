package com.tonyjstudio.chuyendoisodienthoai;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private final Map<String, String> mPhoneMap = new HashMap<String, String>() {{
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

        long id = getRawContactIdByName("tony", "phan");

        ContentResolver contentResolver = getContentResolver();
        updatePhoneNumber(contentResolver, id, ContactsContract.CommonDataKinds.Phone.TYPE_HOME, "0968252764");
        //doTransfer();
    }

    /**
     * function to do the transfer task
     */
//    private void doTransfer() {
//
//        if (mDialog.isShowing()) {
//
//            for (Map.Entry<String, String> entry : mPhoneMap.entrySet()) {
//
//                ArrayList<ContentProviderOperation> operationArrayList = new ArrayList<>();
//                String where =
//                        //ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
//                        ContactsContract.Data.MIMETYPE + " ? AND "
//                                + String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE).substring(0, entry.getKey().length()) + " = ? ";
//
//                String[] params = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, entry.getKey()};
//
//                operationArrayList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                        .withSelection(where, params)
//                        .withValue(ContactsContract.CommonDataKinds.Phone.DATA, entry.getValue())
//                        .build());
//
//                try {
//                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationArrayList);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }

    /* Get raw contact id by contact given name and family name.
     *  Return raw contact id.
     * */
    private long getRawContactIdByName(String givenName, String familyName) {
        ContentResolver contentResolver = getContentResolver();

        // Query raw_contacts table by display name field ( given_name family_name ) to get raw contact id.

        // Create query column array.
        String queryColumnArr[] = {ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};

        // Create where condition clause.
        String displayName = givenName + " " + familyName;
        String whereClause = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = '" + displayName + "'";

        // Query raw contact id through RawContacts uri.
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;

        // Return the query cursor.
        Cursor cursor = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS},
                    1);
        } else {
            try {
                cursor = contentResolver.query(rawContactUri, null, whereClause, null, null);
            } catch (Exception ex) {
                Log.e("ERRRRRRRROR", ex.toString());
            }

        }

        long rawContactId = 0;

        if (cursor != null) {
            // Get contact count that has same display name, generally it should be one.
            int queryResultCount = cursor.getCount();
            // This check is used to avoid cursor index out of bounds exception. android.database.CursorIndexOutOfBoundsException
            if (queryResultCount > 0) {
                // Move to the first row in the result cursor.
                cursor.moveToFirst();
                // Get raw_contact_id.
                String s = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));
                Log.d("tag", s);
                rawContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            }
            cursor.close();
        }

        return rawContactId;
    }


    /*
     * Update contact phone number by contact name.
     * Return update contact number, commonly there should has one contact be updated.
     */
    private int updateContactPhoneByName(String givenName, String familyName) {
        int ret = 0;

        ContentResolver contentResolver = getContentResolver();

        // Get raw contact id by display name.
        long rawContactId = getRawContactIdByName(givenName, familyName);

        // Update data table phone number use contact raw contact id.
        if (rawContactId > -1) {
            // Update mobile phone number.
            updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, "66666666666666");

            // Update work mobile phone number.
            updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, "8888888888888888");

            // Update home phone number.
            updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_HOME, "99999999999999999");

            ret = 1;
        } else {
            ret = 0;
        }

        return ret;
    }

    /* Update phone number with raw contact id and phone type.*/
    private void updatePhoneNumber(ContentResolver contentResolver, long rawContactId, int phoneType, String newPhoneNumber) {
        // Create content values object.
        ContentValues contentValues = new ContentValues();

        // Put new phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber);

        // Create query condition, query with the raw contact id.
        StringBuffer whereClauseBuf = new StringBuffer();

        // Specify the update contact id.
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);

        // Specify the row data mimetype to phone mimetype( vnd.android.cursor.item/phone_v2 )
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.Data.MIMETYPE);
        whereClauseBuf.append(" = '");
        String mimetype = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
        whereClauseBuf.append(mimetype);
        whereClauseBuf.append("'");

        // Specify phone type.
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.TYPE);
        whereClauseBuf.append(" = ");
        whereClauseBuf.append(phoneType);

        // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        // Get update data count.
        int updateCount = contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null);
    }
}

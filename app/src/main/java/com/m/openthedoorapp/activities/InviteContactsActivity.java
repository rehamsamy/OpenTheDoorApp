package com.m.openthedoorapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.provider.ContactsContract;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.Adapter.ContactsAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ContactsModel;

import java.util.ArrayList;
import java.util.List;

public class InviteContactsActivity extends AppCompatActivity {

    @BindView(R.id.no_contacts_txt_id)
    TextView no_contacts;
    @BindView(R.id.contacts_list_id)
    ListView contacts_list;
    ContactsAdapter adapter;

    List<ContactsModel> list = new ArrayList<>();
    private String contacts_permission[];
    private final int conatcts_request_code = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_contacts);
        ButterKnife.bind(this);
        //Storage Permission
        contacts_permission = new String[]{Manifest.permission.READ_CONTACTS};
        try {
            if (ActivityCompat.checkSelfPermission(InviteContactsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(InviteContactsActivity.this, contacts_permission, conatcts_request_code);
            } else {
                loadContacts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle permission result...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case conatcts_request_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    loadContacts();
                else
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick(R.id.inviteFriend_back_txtV_id)
    void goBack() {
        finish();
    }

    private void loadContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);
        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(InviteContactsActivity.this, android.R.layout.simple_list_item_2, cursor, from, to);
        contacts_list.setAdapter(simpleCursorAdapter);
        contacts_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}

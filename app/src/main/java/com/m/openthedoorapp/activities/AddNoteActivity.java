package com.m.openthedoorapp.activities;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.AddNoteModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {

    @BindView(R.id.note_ed_id)
    EditText note_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    ServiceItemData itemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("itemData")) {
            itemData = getIntent().getParcelableExtra("itemData");
        }
    }

    @OnClick(R.id.addNotes_back_txtV_id)
    void goBACK() {
        finish();
    }

    @OnClick(R.id.save_note_button)
    void saevNote() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(note_ed, getString(R.string.required))) {

                final ProgressDialog dialog = dialogUtil.showProgressDialog(AddNoteActivity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<AddNoteModel> call = serviceInterface.save_Note(itemData.getProvider_id(), FindServiceActivity.userModel.getId(), note_ed.getText().toString(), FindServiceActivity.userModel.getToken());
                call.enqueue(new Callback<AddNoteModel>() {
                    @Override
                    public void onResponse(Call<AddNoteModel> call, Response<AddNoteModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            Toast.makeText(AddNoteActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                            note_ed.setText("");
                        } else {
                            Toast.makeText(AddNoteActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddNoteModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        }
    }
}

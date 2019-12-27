package com.m.openthedoorapp.activities;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ReportAproblemModel;
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

public class ReportAproblemActivity extends AppCompatActivity {

    @BindView(R.id.problem_ed_id)
    EditText problem_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    ServiceItemData itemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_aproblem);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("itemData")) {
            itemData = getIntent().getParcelableExtra("itemData");
        }
    }

    @OnClick(R.id.reportProplem_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.sendReport_button)
    void sendProblem() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(problem_ed, getString(R.string.required))) {

                final ProgressDialog dialog = dialogUtil.showProgressDialog(ReportAproblemActivity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ReportAproblemModel> call = serviceInterface.send_Problem(itemData.getProvider_id(), FindServiceActivity.userModel.getId(), problem_ed.getText().toString(), FindServiceActivity.userModel.getToken());
                call.enqueue(new Callback<ReportAproblemModel>() {
                    @Override
                    public void onResponse(Call<ReportAproblemModel> call, Response<ReportAproblemModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            Toast.makeText(ReportAproblemActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                            problem_ed.setText("");
                        } else {
                            Toast.makeText(ReportAproblemActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportAproblemModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        }
    }
}

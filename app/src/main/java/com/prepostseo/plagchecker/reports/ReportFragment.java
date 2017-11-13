package com.prepostseo.plagchecker.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prepostseo.plagchecker.R;
import com.prepostseo.plagchecker.activity.PublicActivity;
import com.prepostseo.plagchecker.activity.ResultActivity;
import com.prepostseo.plagchecker.api.ApiClient;
import com.prepostseo.plagchecker.checker.response.PlagiarismResponse;
import com.prepostseo.plagchecker.checker.response.PlagiarismSource;
import com.prepostseo.plagchecker.checker.restInterface.PlagiarismService;
import com.prepostseo.plagchecker.reports.response.ReportsResponse;
import com.prepostseo.plagchecker.reports.response.ReportsResultResponse;
import com.prepostseo.plagchecker.reports.restinterface.ReportsService;

import org.apache.poi.sl.usermodel.Line;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ReportFragment extends Fragment {

    public Activity activity;
    private String key = "";
    private ProgressDialog pdGettingReports;
    private ProgressDialog pdGettingResult;
    private LayoutInflater mInflater;
    private View mRootView;
    public ReportFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = inflater;
        mRootView = inflater.inflate(R.layout.fragment_report, container, false);
        return mRootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        callGetReportsService();
    }
    public void getKey(){
        SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", MODE_PRIVATE);
        key = shared.getString("api_key", "");
    }
    void initialize()
    {
        activity = getActivity();
        pdGettingReports = new ProgressDialog(getActivity());
        pdGettingReports.setMessage("Getting report, please wait");
        pdGettingReports.setCanceledOnTouchOutside(false);

        pdGettingResult = new ProgressDialog(getActivity());
        pdGettingResult.setMessage("Getting requested result, please wait");
        pdGettingResult.setCanceledOnTouchOutside(false);
        getKey();
    }
    public void callGetReportsService(){
        ReportsService reportService = ApiClient.getClient().create(ReportsService.class);
        RequestBody keyParam = RequestBody.create(MediaType.parse("text/plain"), key);

        Call<ReportsResponse> call =  reportService.getReportList(keyParam);
        pdGettingReports.show();

        //inflateDialogBoxViewLayout();
        call.enqueue(new Callback<ReportsResponse>() {
            @Override
            public void onResponse(Call<ReportsResponse> call, Response<ReportsResponse> response) {
                pdGettingReports.hide();
                if(response != null){
                    updateResult(response);
                }
            }

            @Override
            public void onFailure(Call<ReportsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pdGettingReports.hide();
            }
        });
    }
    public void callGetResultService(Integer id){
        ReportsService reportService = ApiClient.getClient().create(ReportsService.class);
        RequestBody keyParam = RequestBody.create(MediaType.parse("text/plain"), key);
        RequestBody idParam = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(id));

        Call<ReportsResultResponse> call =  reportService.getReportDetails(keyParam,idParam);
        pdGettingResult.show();

        //inflateDialogBoxViewLayout();
        call.enqueue(new Callback<ReportsResultResponse>() {
            @Override
            public void onResponse(Call<ReportsResultResponse> call, Response<ReportsResultResponse> response) {
                pdGettingResult.hide();
                if(response != null){
                    showResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReportsResultResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pdGettingResult.hide();
            }
        });
    }
    void showResult(ReportsResultResponse response) {
        if(response != null) {
            if(response.getResponse() == 1 ) {
                String jsonFormattedString = response.getDetails().replaceAll("\\\\", "");
                //JSON from String to Object
                Gson gson = new GsonBuilder().create();
                PlagiarismResponse plagiarismResponse= gson.fromJson(jsonFormattedString, PlagiarismResponse.class);
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("response", plagiarismResponse);
                getActivity().startActivity(intent);
            }else
            {
                Toast.makeText(getActivity(), response.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
    void updateResult(Response<ReportsResponse> response)
    {
        if(response.body()!=null) {

            if(response.body().getResponse() == 1) {

                addSourcesItems(response.body().getReports(), mInflater, mRootView);
            }else {

                Toast.makeText(getActivity(), response.body().getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
    void addSourcesItems(List<ReportsResponse.Report> sourceItems, LayoutInflater inflater, View rootView)
    {
        if(!sourceItems.isEmpty())
        {
            for (ReportsResponse.Report item:sourceItems)
            {
                if(item!=null)
                {
                    LinearLayout itemLayout = inflateReportItemLayout(inflater);
                    itemLayout.setTag(item);
                    TextView dateTextView=(TextView) itemLayout.findViewById(R.id.date);
                    dateTextView.setText(item.getDate());
                    TextView timeTextView=(TextView) itemLayout.findViewById(R.id.date_time);
                    timeTextView.setText(item.getTime());
                    TextView titleTextView=(TextView) itemLayout.findViewById(R.id.title);
                    titleTextView.setText(item.getTitle());
                    TextView percentageTextView=(TextView) itemLayout.findViewById(R.id.percentage);
                    percentageTextView.setText(item.getUniquePercent()+"%");
                    setColorOfPercentage(percentageTextView,item.getUniquePercent());
                    itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          ReportsResponse.Report item =(ReportsResponse.Report)v.getTag();
                          callGetResultService(item.getId());
                        }
                    });
                    ((LinearLayout)rootView.findViewById(R.id.table_layout)).addView(itemLayout);
                }
            }
        }
    }

    void setColorOfPercentage(TextView textView, int percentage)
    {
        if(percentage >= 60)
        {
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }else
        {
            textView.setTextColor(getResources().getColor(R.color.resultplag));
        }
    }
    static LinearLayout inflateReportItemLayout(LayoutInflater inflater)
    {
        return (LinearLayout) inflater.inflate(R.layout.report_cell, null);
    }
}

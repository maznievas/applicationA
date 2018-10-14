package com.example.andrey.applicationa.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.andrey.applicationa.Const;
import com.example.andrey.applicationa.R;
import com.example.andrey.applicationa.database.ReferenceData;
import com.example.andrey.applicationa.util.ItemSelectedListener;
import com.example.andrey.applicationa.util.ListItemDecoration;
import com.example.andrey.applicationa.util.adapters.RecyclerViewAdapter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends MvpAppCompatFragment implements MainView,
        ItemSelectedListener {

    private Unbinder unbinder;
    private RecyclerViewAdapter recyclerViewAdapter;
    private final String TAG = "mLog";
    private ProgressDialog progressDialog;

    @InjectPresenter
    MainPresenter mainPresenter;

    @BindView(android.R.id.tabhost)
    TabHost tabHost;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.receiver_action))) {
                Log.d(TAG, "intent received");

                String urlExtra = intent.getStringExtra(getString(R.string.url_extra));
                int status = intent.getIntExtra(getString(R.string.status_extra), 3);
                long lastOpenedExtra = intent.getLongExtra(getString(R.string.last_opened_extra), 0);

                getContext().unregisterReceiver(mReceiver);

                ReferenceData referenceData = new ReferenceData();
                referenceData.setUrl(urlExtra);
                referenceData.setStatus(status);
                referenceData.setLastOpened(new Date(lastOpenedExtra));

                mainPresenter.saveReference(referenceData);
            }
        }
    };

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec(getString(R.string.test_tab_spec_tag ));
        tabSpec.setIndicator(getString(R.string.test));
        tabSpec.setContent(R.id.testTab);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(getString(R.string.history_tab_spec_tag));
        tabSpec.setIndicator(getString(R.string.history));
        tabSpec.setContent(R.id.historyTab);
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals(getString(R.string.test_tab_spec_tag )))
                    ((MainActivity)getActivity()).setMenuVisible(false);
                else {
                    ((MainActivity) getActivity()).setMenuVisible(true);
                   //mainPresenter.
                }
            }
        });

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerViewAdapter.setItemSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ListItemDecoration((int) getResources().getDimension(R.dimen.space_betwen_items)));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        mainPresenter.clear();
        super.onDestroy();
    }

    @OnClick(R.id.buttonOk)
    public void onOkButtonClicked(){
        Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(getString(R.string.application_b_package));
        if (launchIntent != null) {
            getContext().registerReceiver(mReceiver, new IntentFilter(getString(R.string.receiver_action)));
            launchIntent.putExtra(getString(R.string.url_extra), editText.getText().toString());
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }
        else
            showMessage(R.string.you_dont_have_additional_application_for);
    }

    public void showMessage(int resourceId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setMessage(resourceId)
                .setPositiveButton(R.string.ok, (dialog, wich) -> dialog.dismiss())
                .show();
    }

    @Override
    public void selectedReference(ReferenceData referenceData) {

    }

    @Override
    public void setReferencesList(List<ReferenceData> referenceDataList) {
        recyclerViewAdapter.setReferencesList(referenceDataList);
    }

    @Override
    public void updateReferencesList(List<ReferenceData> referenceDataList) {
        recyclerViewAdapter.updateReferencesList(referenceDataList);
    }

    @Override
    public void showLoadingState(boolean flag) {
        if(flag)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    public void sortByDate(){
        recyclerViewAdapter.sortByDate();
    }

    public void sortByStatus(){
        recyclerViewAdapter.sortByStatus();
    }
}

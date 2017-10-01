package app.numFacts.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import app.numFacts.R;
import app.numFacts.databinding.FragmentAdvanceBinding;

/**
 * Created by Deepak on 9/24/2017.
 */

public class AdvancedFragment extends Fragment implements View.OnClickListener {
    ProgressDialog pd;
    //Non ui Vars
    FragmentAdvanceBinding mBinding;
    Context contextd;
    StringRequest request;
    RequestQueue queue;
    String[] category = {"trivia", "year", "date", "math"};
    String cate = "", digitDate = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_advance, container, false);
        contextd = container.getContext();
        View rootView = mBinding.getRoot();
        queue = Volley.newRequestQueue(contextd);
        getTabs();
        pd = new ProgressDialog(contextd);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        mBinding.categoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cate = category[mBinding.categoryTabs.getSelectedTabPosition()];
                mBinding.factTxt.setText("");
                mBinding.getNumber.setHint(mBinding.categoryTabs.getSelectedTabPosition() == 1 ? "Enter Year" : "Enter Number");
                mBinding.getNumber.setVisibility(mBinding.categoryTabs.getSelectedTabPosition() == 2 ? View.GONE : View.VISIBLE);
                mBinding.dateUI.setVisibility(mBinding.categoryTabs.getSelectedTabPosition() == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mBinding.getFact.setOnClickListener(this);
        return rootView;
    }

    void getTabs() {
        for (int i = 0; i < category.length; i++) {
            mBinding.categoryTabs.addTab(mBinding.categoryTabs.newTab().setText(category[i].toUpperCase()));
        }
        mBinding.categoryTabs.getTabAt(0).select();
        cate = category[0];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getFact:
                if (validate()) {
                    digitDate = mBinding.categoryTabs.getSelectedTabPosition() == 2 ?
                            ((Integer.parseInt(mBinding.getMonth.getText().toString().trim()) < 10 ?
                                    ("0" + mBinding.getMonth.getText().toString().trim())
                                    : mBinding.getMonth.getText().toString().trim())
                                    + "/" +
                                    (Integer.parseInt(mBinding.getDate.getText().toString().trim()) < 10 ?
                                            ("0" + mBinding.getDate.getText().toString().trim()) :
                                            mBinding.getDate.getText().toString().trim())) :
                            mBinding.getNumber.getText().toString();
                    pd.setMessage("Fetching Random Fact for " + cate.toUpperCase() + ".");
                    pd.show();
                    requestForFact();
                }
                break;
        }
    }

    boolean validate() {
        boolean b = false;
        if (mBinding.categoryTabs.getSelectedTabPosition() == 2) {
            if (mBinding.getMonth.getText().toString().trim().equals("")) {
                b = false;
                mBinding.getMonth.setError("Required Field..!");
            } else if (Integer.parseInt(mBinding.getMonth.getText().toString().trim()) > 12 ||
                    Integer.parseInt(mBinding.getMonth.getText().toString().trim()) == 0) {
                b = false;
                mBinding.getMonth.setError("Invalid Month..!");
            } else if (mBinding.getDate.getText().toString().trim().equals("")) {
                b = false;
                mBinding.getDate.setError("Required Field..!");
            } else {
                b = true;
            }
        } else if (mBinding.categoryTabs.getSelectedTabPosition() == 1) {
            if (mBinding.getNumber.getText().toString().trim().equals("")) {
                b = false;
                mBinding.getNumber.setError("Required Field..!");
            } else if (mBinding.getNumber.getText().toString().trim().length() != 4) {
                b = false;
                mBinding.getNumber.setError("Invalid Year..!");
            } else
                b = true;
        } else {
            if (mBinding.getNumber.getText().toString().trim().equals("")) {
                b = false;
                mBinding.getNumber.setError("Required Field..!");
            } else
                b = true;
        }
        return b;
    }

    void requestForFact() {
        request = new StringRequest(Request.Method.GET, ("http://numbersapi.com/" + digitDate + "/" + cate), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mBinding.factTxt.setText(response);
                queue.stop();
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mBinding.factTxt.setText("");
                queue.stop();
                pd.dismiss();
            }
        });
        queue.add(request);
        queue.start();
    }
}

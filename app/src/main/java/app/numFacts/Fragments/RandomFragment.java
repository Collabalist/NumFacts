package app.numFacts.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import app.numFacts.databinding.FragmentRandomBinding;

/**
 * Created by Deepak on 9/24/2017.
 */

public class RandomFragment extends Fragment implements View.OnClickListener {
    ProgressDialog pd;
    //Non ui Vars
    FragmentRandomBinding mbinding;
    Context contextd;
    StringRequest request;
    RequestQueue queue;
    String[] category = {"trivia", "year", "date", "math"};
    String cate = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextd = container.getContext();
        mbinding = DataBindingUtil.inflate(inflater, R.layout.fragment_random, container, false);
        View rootView = mbinding.getRoot();
        queue = Volley.newRequestQueue(contextd);
        getTabs();
        pd = new ProgressDialog(contextd);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        mbinding.categoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cate = category[mbinding.categoryTabs.getSelectedTabPosition()];
                mbinding.factTxt.setText("");
                Log.d("cateGory", cate);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mbinding.randomBtn.setOnClickListener(this);
        return rootView;
    }

    void getTabs() {
        for (int i = 0; i < category.length; i++) {
            mbinding.categoryTabs.addTab(mbinding.categoryTabs.newTab().setText(category[i].toUpperCase()));
        }
        mbinding.categoryTabs.getTabAt(0).select();
        cate = category[0];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.randomBtn:
                pd.setMessage("Fetching Random Fact for " + cate.toUpperCase() + ".");
                pd.show();
                requestForFact();
                break;
        }
    }

    void requestForFact() {
        request = new StringRequest(Request.Method.GET, ("http://numbersapi.com/random/" + cate), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mbinding.factTxt.setText(response);
                queue.stop();
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mbinding.factTxt.setText("");
                queue.stop();
                pd.dismiss();
            }
        });
        queue.add(request);
        queue.start();
    }
}

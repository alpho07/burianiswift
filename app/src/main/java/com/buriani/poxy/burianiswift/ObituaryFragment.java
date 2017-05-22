package com.buriani.poxy.burianiswift;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buriani.poxy.burianiswift.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ObituaryFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ObituaryAdapter adapter;
    List<MyData> dataList;

    String Method = "mobileob/";
    Constant constant;
    ProgressDialog progressDialog;
    String cat= "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public ObituaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.obituaryfragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        preferences = getActivity().getSharedPreferences("ob.conf", Context.MODE_PRIVATE);
        cat = preferences.getString("cat", "");
        String title = preferences.getString("title", "");
        dataList = new ArrayList<>();
        loadData(0, Integer.parseInt(cat));
        constant = new Constant();
        gridLayoutManager = new GridLayoutManager(this.getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ObituaryAdapter(this.getActivity(), dataList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition()==dataList.size()-1){

                    loadData(dataList.get(dataList.size()-1).getId(),Integer.parseInt(cat));
                }
            }
        });


       return view;
    }

    private void loadData(final int id,final int cat) {
        Log.i("THEURL",Constant.Home+Method+id+"/"+cat);
        AsyncTask<Integer, Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.Home+Method+id+"/"+cat).build();
                try {
                    Response response = client.newCall(request).execute();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            MyData data = new MyData(
                                    obj.getInt("id"),
                                    obj.getString("description"),
                                    obj.getString("image_path"),
                                    obj.getString("dob"),
                                    obj.getString("dod"),
                                    obj.getString("region"),
                                    obj.getString("date_posted"),
                                    obj.getString("obtitle"),
                                    obj.getString("title")
                            );

                            dataList.add(data);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                adapter.notifyDataSetChanged();
              //  dismissLoader();
            }

            @Override
            protected void onPreExecute()  {
                super.onPreExecute();
               //showLoader();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

            }
        };
        task.execute(id,cat);
    }


    public void showLoader(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissLoader(){
        progressDialog.dismiss();
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}


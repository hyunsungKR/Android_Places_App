package com.hyunsungkr.placesapp;

import static com.hyunsungkr.placesapp.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hyunsungkr.placesapp.adapter.PlaceAdapter;
import com.hyunsungkr.placesapp.api.NetworkClient;
import com.hyunsungkr.placesapp.api.PlaceApi;
import com.hyunsungkr.placesapp.model.Place;
import com.hyunsungkr.placesapp.model.PlaceList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    ImageView imgSearch;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    PlaceAdapter adapter;
    ArrayList<Place> placeArrayList = new ArrayList<>();

    LocationManager locationManager;
    LocationListener locationListener;

    private String keyword;
    private String pagetoken;

    private double currentLat;
    private double currentLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCount) {
                    if (pagetoken != null) {
                        addNetworkData();
                    }
                }
            }
        });


        // API ????????? ????????? ??? ?????? ????????? ????????????
        // ????????? ???????????? ????????????, ??????????????????????????? ???????????? ???????????? ????????????.

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // ???????????? ???????????? ?????????.
        // ????????? ?????? ????????? ???????????? ?????? ??????!
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                // ?????? ?????? ?????? ????????? ???????????? ????????? ?????? ????????? ??????
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                Log.i("MyLocation",""+currentLng+ " "+currentLat);



            }
        };

        if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return;

        }
        // ?????????????????? GPS ?????? ???????????? ????????? ???????????? ??????
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,-1,locationListener);





        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();
                if(keyword.isEmpty()){
                    Toast.makeText(MainActivity.this, "?????????????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(currentLat==0){
                    Toast.makeText(MainActivity.this, "????????? ?????? ?????????, ?????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }

                // ??????????????? API ???????????? ????????? ???????????? ????????? ????????????.
                getNetworkData();

            }
        });







    }

    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlaceList(keyword, currentLat+","+currentLng, 1500, "ko", "", MAPS_API_KEY);

        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    pagetoken = response.body().getNext_page_token();
                    placeArrayList.addAll( response.body().getResults() );

                    adapter.notifyDataSetChanged();


                }else{

                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},100);
                return;
            }
            // ?????????????????? GPS ?????? ???????????? ????????? ???????????? ??????
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,-1,locationListener);

        }

    }

    private void getNetworkData() {
        placeArrayList.clear();
        pagetoken = "";

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlaceList(keyword, currentLat+","+currentLng, 1500, "ko", "", MAPS_API_KEY);

        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){

                    pagetoken = response.body().getNext_page_token();
                    placeArrayList.addAll( response.body().getResults() );

                    adapter = new PlaceAdapter(MainActivity.this, placeArrayList);
                    adapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                        @Override
                        public void onCardViewClick(int index) {
                            Place place = placeArrayList.get(index);

                            Intent intent = new Intent(MainActivity.this,MapActivity.class);
                            intent.putExtra("place",place);

                            startActivity(intent);
                        }
                    });

                    recyclerView.setAdapter(adapter);

                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
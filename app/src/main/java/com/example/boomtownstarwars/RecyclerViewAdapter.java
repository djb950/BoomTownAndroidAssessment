package com.example.boomtownstarwars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    // Properties
    String baseUrl = "https://swapi.dev/api/";
    int pageCount = 1;
    Context context;
    Response response;
    List<Person> people = new ArrayList<>();

    public RecyclerViewAdapter(Context ct) {
        context = ct;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (people.isEmpty()) {
            fetchData();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.combined_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        if (!people.isEmpty()) {

            // populate views
            holder.name.setText(people.get(position).getName());
            holder.birthYear.setText("Birth: " + people.get(position).getBirth_year());
            holder.height.setText("Height: " + people.get(position).getHeight());
            holder.mass.setText("Mass: " + people.get(position).getMass());
            holder.gender.setText("Gender: " + people.get(position).getGender());
            holder.hairColor.setText("Hair color: " + people.get(position).getHair_color());
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(view.getContext(), "Eye color: " + people.get(position).getEye_color(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (people.isEmpty()) {
            return 1;
        } else {
            return people.size();
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView name, birthYear, height, mass, gender, hairColor;
        ProgressBar progressBar;
        ConstraintLayout mainLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialize views
            progressBar = itemView.findViewById(R.id.progressBar);
            name = itemView.findViewById(R.id.name_text);
            birthYear = itemView.findViewById(R.id.birth_year_text);
            height = itemView.findViewById(R.id.height_text);
            mass = itemView.findViewById(R.id.mass_text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            gender = itemView.findViewById(R.id.gender_text);
            hairColor = itemView.findViewById(R.id.hair_color_text);
        }
    }

    // Private Methods
    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        StarWarsApi swapi = retrofit.create(StarWarsApi.class);
        Call<Response> call = swapi.getAllPeople(pageCount);
        pageCount++;
        call.enqueue(new Callback<Response>() {

            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> res) {
                // if request is successful, but fail to get data
                if (!res.isSuccessful()) {
                    return;
                }

                // successfully fetched data
                response = res.body();
                people.addAll(response.getPeople());
                notifyDataSetChanged();
                if (response.getNext() != null) {
                    fetchData();
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t) {
            }
        });
    }
}

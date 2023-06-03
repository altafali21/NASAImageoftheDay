package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView imageListView;
    private List<String> imageUrls;
    private ImageAdapter adapter;
    private DatePicker datePicker;
    private Button searchButton;
    private Button randomButton;
    private Button potdButton;
    private ProgressBar progressBar;
    private EditText editText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageListView = findViewById(R.id.imageListView);
        datePicker = findViewById(R.id.datePicker);
        searchButton = findViewById(R.id.searchButton);
        randomButton = findViewById(R.id.randomButton);
        potdButton = findViewById(R.id.potdButton);
        progressBar = findViewById(R.id.progress_bar);
        editText = findViewById(R.id.edit_text);

        imageUrls = new ArrayList<>();
        adapter = new ImageAdapter(this, imageUrls);
        imageListView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomImage();
            }
        });

        potdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureOfTheDay();
            }
        });

        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imageUrl = imageUrls.get(position);
                // Implement logic to show detailed information about the selected image
                // You can display a dialog, start a new activity, or use a fragment to show the details
            }
        });
    }

    private void performSearch() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is zero-based
        int year = datePicker.getYear();
        String selectedDate = year + "-" + month + "-" + day;

        URL url = NetworkUtils.buildUrl(selectedDate);
        if (url != null) {
            new ImageSearchTask().execute(url);
        } else {
            Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRandomImage() {
        URL url = NetworkUtils.buildUrl("random");
        if (url != null) {
            new ImageSearchTask().execute(url);
        } else {
            Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPictureOfTheDay() {
        URL url = NetworkUtils.buildUrl("today");
        if (url != null) {
            new ImageSearchTask().execute(url);
        } else {
            Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageLocally(String imageUrl) {
        // Implement logic to store the image locally
        // You can use SharedPreferences, a database, or any other storage mechanism
    }

    private class ImageSearchTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResponse = null;
            try {
                jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            progressBar.setVisibility(View.GONE);
            String imageUrl = NetworkUtils.parseImageUrlFromJson(jsonResponse);
            if (imageUrl != null) {
                imageUrls.add(imageUrl);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Failed to parse image URL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ImageAdapter extends ArrayAdapter<String> {

        public ImageAdapter(AppCompatActivity context, List<String> urls) {
            super(context, 0, urls);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_list_item, parent, false);
            }

            String imageUrl = getItem(position);
            TextView urlTextView = convertView.findViewById(R.id.urlTextView);
            Button saveButton = convertView.findViewById(R.id.saveButton);

            urlTextView.setText(imageUrl);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveImageLocally(imageUrl);
                    Toast.makeText(MainActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation item selection here
        // You can perform actions based on the selected item
        // For example, you can start a new activity or update the UI

        // Remember to close the drawer after handling the selection
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}

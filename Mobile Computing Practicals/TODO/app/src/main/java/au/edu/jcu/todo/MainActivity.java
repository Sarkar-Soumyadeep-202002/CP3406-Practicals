package au.edu.jcu.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ListView todoList;
    private ArrayAdapter<String> adapter;
    private SharedPreferences dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = findViewById(R.id.todo_list);
        adapter = new ArrayAdapter<>(this, R.layout.item);
        todoList.setAdapter(adapter);

        adapter.addAll("buy milk", "wash car", "call mum");

        dataSource = getSharedPreferences("todo items", MODE_PRIVATE);

        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView itemView = (TextView) view;
                adapter.remove(itemView.getText().toString());
            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_item) {

            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.clear();
        Set<String> newItems = dataSource.getStringSet("items", null);
        if(newItems != null)
            adapter.addAll(newItems);
    }

    @Override
    protected void onStop(){
        super.onStop();

        Set<String> currentItems = new HashSet<>();
        for(int i = 0; i < adapter.getCount(); ++i)
            currentItems.add(adapter.getItem(i));

        dataSource.edit().putStringSet("items", currentItems).apply();
    }

}

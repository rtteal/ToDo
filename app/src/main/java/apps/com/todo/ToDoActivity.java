package apps.com.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ToDoActivity extends Activity {
    private List<String> items;
    private ArrayAdapter<String> itemAdapter;
    private ListView lvItems;
    private File TODOFILE;
    public static final int REQUEST_CODE = 20;
    public static final String MESSAGE = "MESSAGE";
    public static final String POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // WARNING: throws NullPointerException if todo.txt does not exist
        TODOFILE = new File(getFilesDir(), "todo.txt");
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemAdapter);
        setupListViewListener();
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        etNewItem.setSelection(0);
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        items.remove(pos);
                        itemAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }

        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
                        i.putExtra(POSITION, pos);
                        i.putExtra(MESSAGE, items.get(pos));
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String message = data.getExtras().getString(MESSAGE);
            int pos = data.getIntExtra(POSITION, 0);
            items.set(pos, message);
            itemAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(TODOFILE, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readItems(){
        try {
            items = new ArrayList<String>(FileUtils.readLines(TODOFILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

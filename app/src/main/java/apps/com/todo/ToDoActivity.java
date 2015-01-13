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
import org.w3c.dom.Comment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ToDoActivity extends Activity {
    private List<Todo> items;
    private ArrayAdapter<Todo> itemAdapter;
    private ListView lvItems;
    public static final int REQUEST_CODE = 20;
    public static final String MESSAGE = "MESSAGE";
    public static final String POSITION = "POSITION";
    public static final String ID = "ID";
    private ToDoDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datasource = new ToDoDataSource(this);
        datasource.open();

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = datasource.getAllTodos();
        itemAdapter = new ArrayAdapter<Todo>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemAdapter);
        setupListViewListener();
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        etNewItem.setSelection(0);
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Todo todo = new Todo(itemText);
        datasource.createTodo(todo);
        itemAdapter.add(todo);
        etNewItem.setText("");
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        datasource.deleteTodo(items.get(pos));
                        items.remove(pos);
                        itemAdapter.notifyDataSetChanged();
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
                        i.putExtra(MESSAGE, items.get(pos).getTodo());
                        i.putExtra(ID, items.get(pos).getId());
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String message = data.getExtras().getString(MESSAGE);
            int pos = data.getIntExtra(POSITION, 0);
            long id = data.getLongExtra(ID, 0);
            Todo todo = new Todo(message);
            todo.setId(id);
            items.set(pos, todo);
            itemAdapter.notifyDataSetChanged();
            datasource.updateTodo(todo);
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

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // this is causing the database to close when the user navigates to the EditItemActivity...
        //datasource.close();
        super.onPause();
    }

}

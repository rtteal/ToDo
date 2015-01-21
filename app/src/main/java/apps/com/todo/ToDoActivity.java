package apps.com.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ToDoActivity extends FragmentActivity implements EditTodoDialog.EditTodoDialogListener {
    private List<Todo> items;
    private ToDoAdapter toDoAdapter;
    private ListView lvItems;
    private int posBeingEdited;
    private long idBeingEdited;
    private final String TAG = this.getClass().getSimpleName();
    private ToDoDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datasource = new ToDoDataSource(this);
        datasource.open();
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = datasource.getAllTodos();
        toDoAdapter = new ToDoAdapter(this, items);
        lvItems.setAdapter(toDoAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Todo todo = new Todo(itemText);
        SimpleDateFormat df = new SimpleDateFormat("M/dd/yyyy");
        Calendar c = Calendar.getInstance();
        todo.setDate(df.format(c.getTime()));
        datasource.createTodo(todo);
        toDoAdapter.add(todo);
        etNewItem.setText("");
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        datasource.deleteTodo(items.get(pos));
                        items.remove(pos);
                        toDoAdapter.notifyDataSetChanged();
                        return true;
                    }
                }

        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        ToDoActivity.this.posBeingEdited = pos;
                        ToDoActivity.this.idBeingEdited = items.get(pos).getId();
                        showEditDialog(items.get(pos).getTodo(), items.get(pos).getDate());
                    }
                }
        );
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

    private void showEditDialog(String todo, String date) {
        FragmentManager fm = getSupportFragmentManager();
        EditTodoDialog editNameDialog = EditTodoDialog.newInstance("Edit Todo", todo, date);
        editNameDialog.show(fm, "fragment_edit_todo");
    }

    @Override
    public void onFinishEditDialog(String inputText, String date) {
        Log.d(TAG, String.format("editing id: %s, position: %s, inputText: %s, date: %s",
                idBeingEdited, posBeingEdited, inputText, date));
        items.get(posBeingEdited).setTodo(inputText);
        items.get(posBeingEdited).setDate(date);
        toDoAdapter.notifyDataSetChanged();
        datasource.updateTodo(items.get(posBeingEdited));
    }
}

package apps.com.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rtteal on 1/12/2015.
 */
public class ToDoDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_TODO };
    public final String TAG = this.getClass().toString();

    public ToDoDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Todo createTodo(Todo todo) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_TODO, todo.getTodo());
        long insertId = database.insert(SQLiteHelper.TABLE_TODO, null,
                values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_TODO,
                allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Todo newTodo = cursorToTodo(cursor);
        cursor.close();
        return newTodo;
    }

    public void deleteTodo(Todo todo) {
        long id = todo.getId();
        Log.d(TAG, "Todo deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_TODO, SQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateTodo(Todo todo){
        long id = todo.getId();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_ID, todo.getTodo());
        Log.d(TAG, "Todo updated with id: " + id);
        database.update(SQLiteHelper.TABLE_TODO, values, SQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<Todo>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_TODO,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Todo todo = cursorToTodo(cursor);
            todos.add(todo);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return todos;
    }

    private Todo cursorToTodo(Cursor cursor) {
        Todo todo = new Todo();
        todo.setId(cursor.getLong(0));
        todo.setTodo(cursor.getString(1));
        return todo;
    }

}

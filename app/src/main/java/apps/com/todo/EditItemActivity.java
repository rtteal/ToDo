package apps.com.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditItemActivity extends Activity {
    private Button btSave;
    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        final String itemText = getIntent().getStringExtra(ToDoActivity.MESSAGE);
        final int pos = getIntent().getIntExtra(ToDoActivity.POSITION, 0);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(itemText);
        etEditItem.setSelection(etEditItem.getText().length());
        btSave = (Button) findViewById(R.id.btSave);
        setupOnClickListener(pos);
    }

    private void setupOnClickListener(final int pos){
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etEditItem.getText().toString();
                Intent data = new Intent();
                data.putExtra(ToDoActivity.MESSAGE, text);
                data.putExtra(ToDoActivity.POSITION, pos);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

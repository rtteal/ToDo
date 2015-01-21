package apps.com.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by rtteal on 1/18/2015.
 */
public class EditTodoDialog extends DialogFragment implements EditText.OnEditorActionListener, DatePickerDialog.OnDateSetListener  {
    private EditText etEditItem;
    private DatePicker dpDueDate;
    private final String TAG = this.getClass().getSimpleName();

    public interface EditTodoDialogListener {
        void onFinishEditDialog(String inputText, String date);
    }

    public static EditTodoDialog newInstance(String title, String todo, String date) {
        EditTodoDialog frag = new EditTodoDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("todo", todo);
        args.putString("date", date);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_todo, container);
        etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);

        String title = getArguments().getString("title", "Edit Todo");
        String todo = getArguments().getString("todo", "");
        String date = getArguments().getString("date", "1/1/2015");

        getDialog().setTitle(title);
        initializeDate(date);
        etEditItem.setText(todo);

        // Show soft keyboard automatically
        etEditItem.setSelection(etEditItem.getText().length());
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        etEditItem.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // get date from edit dialog
            int day = dpDueDate.getDayOfMonth();
            int month = dpDueDate.getMonth() + 1;
            int year = dpDueDate.getYear();
            String date = month + "/" + day + "/" + year;
            Log.d(TAG, date);

            EditTodoDialogListener activity = (EditTodoDialogListener) getActivity();
            activity.onFinishEditDialog(etEditItem.getText().toString(), date);

            this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, day + "/" + month + "/" + year);
    }

    private void initializeDate(String dateInString){
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(dateInString));
            dpDueDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}

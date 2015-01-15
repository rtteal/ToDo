package apps.com.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rtteal on 1/14/2015.
 */
public class ToDoAdapter extends ArrayAdapter<Todo> {
    // View lookup cache
    private static class ViewHolder {
        TextView todo;
    }

    public ToDoAdapter(Context context, List<Todo> todos) {
        super(context, R.layout.item_todo, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
            viewHolder.todo = (TextView) convertView.findViewById(R.id.tvTodo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.todo.setText(todo.getTodo());
        return convertView;
    }
}

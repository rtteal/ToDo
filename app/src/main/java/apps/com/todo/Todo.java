package apps.com.todo;

/**
 * Created by rtteal on 1/12/2015.
 */
public class Todo {
    private long id;
    private String todo;

    public Todo() {
    }

    public Todo(String todo) {
        this.todo = todo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    @Override
    public String toString() {
        return todo;
    }
}

package exercise.android.reemh.todo_items;

import androidx.annotation.Nullable;

import java.io.Serializable;

enum STATUS implements Serializable{
    DONE,
    IN_PROGRESS
}
public class TodoItem implements Serializable, Comparable<TodoItem> {

    private STATUS status = STATUS.IN_PROGRESS;
    private String description;
    private final Integer time_added;


    TodoItem(String desc, int time_added){
       setDescription(desc);
       setStatus(STATUS.IN_PROGRESS);
       this.time_added = time_added;
    }

    void setDescription(String desc){
        description = desc;
    }

    String getDescription(){
        return description;
    }

    STATUS getStatus(){
        return STATUS.IN_PROGRESS;
    }

    void setStatus(STATUS toSet){
        status = toSet;
    }

    void changeStatus(){

        if (status == STATUS.IN_PROGRESS){
            status = STATUS.DONE;
        }
        else{
            status = STATUS.IN_PROGRESS;
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TodoItem))
        {
            return false;
        }
        TodoItem other = (TodoItem) obj;
        return time_added.equals(other.time_added);
    }

    @Override
    public int compareTo(TodoItem other){

        if (this.status != other.status)
        {
            if (this.status == STATUS.IN_PROGRESS)
            {
                return -1;
            }
            return 1;
        }
        return other.time_added - this.time_added;
    }


  // TODO: edit this class as you want
}

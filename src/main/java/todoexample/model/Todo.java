package todoexample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Todo {
    private int id;
    private String name;
    private Date createdDate;
    private String deadline;
    private Status status;
    private int user_id;


    public Todo(String name, String deadline, Status status, int user_id) {
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.user_id = user_id;
    }
}

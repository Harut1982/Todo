package todoexample.manager;

import todoexample.db.DBConnectionProvider;
import todoexample.model.Status;
import todoexample.model.Todo;
import todoexample.model.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class UserManager {
    private Connection connection;

    public UserManager() {
        connection = DBConnectionProvider.getInstance().getConnection();
    }

    public void addUser(User user) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("Insert into user(name,surname,email,password) Values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            user.setId(id);
        }

    }

    public List<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT* FROM user");
        List<User> users = new LinkedList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            users.add(user);

        }
        return users;
    }

    public void deleteUserById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();


    }

    public User getUser(String email) throws SQLException {

        for (User user : getAllUsers()) {
            if (user.getEmail().equals(email)) {
                return user;
            }

        }
        return null;
    }

    public void addTodo(Todo todo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("Insert into todo(name,createdDate,deadline,status,user_id) Values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, todo.getName());
        preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        preparedStatement.setString(3, todo.getDeadline());
        preparedStatement.setString(4, todo.getStatus().name());
        preparedStatement.setInt(5, todo.getUser_id());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            todo.setId(id);
        }


    }

    public List<Todo> getAllTodo() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT* FROM todo");
        List<Todo> todo = new LinkedList<>();
        while (resultSet.next()) {
            Todo todos = new Todo();
            todos.setId(resultSet.getInt("id"));
            todos.setName(resultSet.getString("name"));
            todos.setCreatedDate(resultSet.getTimestamp("createdDate"));
            todos.setDeadline(resultSet.getTimestamp("deadline").toString());
            todos.setStatus(Status.valueOf(resultSet.getString("status")));
            todos.setUser_id(resultSet.getInt("user_id"));
            todo.add(todos);

        }
        return todo;
    }

    public void printTodoByUser(User user) throws SQLException {
        List<Todo> allTodo = getAllTodo();
        for (Todo todo : allTodo) {
            if (todo.getUser_id() == user.getId()) {
                System.out.println(todo);
            }
        }

    }

    public void printMyInProgressList(User user) throws SQLException {
        List<Todo> allTodo = getAllTodo();
        for (Todo todo : allTodo) {
            if (todo.getUser_id() == user.getId() && todo.getStatus() == Status.IN_PROGRESS) {
                System.out.println(todo);
            }
        }

    }

    public void printMyFinishedTodoList(User user) throws SQLException {
        List<Todo> allTodo = getAllTodo();
        for (Todo todo : allTodo) {
            if (todo.getUser_id() == user.getId() && todo.getStatus() == Status.FINISHED) {
                System.out.println(todo);
            }
        }

    }

    public void deleteTodoById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM todo WHERE id=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();


    }


    public void changeTodo(int id, Status status) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE todo SET status=? WHERE id=?");
        preparedStatement.setString(1, status.name());
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();


    }
}





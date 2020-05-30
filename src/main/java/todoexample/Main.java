package todoexample;

import todoexample.commands.Commands;
import todoexample.manager.UserManager;
import todoexample.model.Status;
import todoexample.model.Todo;
import todoexample.model.User;


import java.sql.SQLException;

import java.util.Arrays;

import java.util.List;
import java.util.Scanner;

public class Main implements Commands {
    private static Scanner scanner = new Scanner(System.in);
    public static UserManager userManager = new UserManager();
    private static User currentUser = null;

    public static void main(String[] args) throws SQLException {
//        List<User> allUsers = userManager.getAllUsers();
//        for (User user : allUsers) {
//            System.out.println(user);
//
//        }


        boolean isRun = true;
        while (isRun) {
            Commands.printMainCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case LOGIN:
                    loginUser();
                    break;
                case REGISTER:
                    registerUser();
                    break;
                default:
                    System.out.println("Wrong command!");


            }
        }

    }

    private static void registerUser() {
        System.out.println("Please input user data name,surname,email,password");
        try {
            String userDataStr = scanner.nextLine();
            String[] userDataArr = userDataStr.split(",");
            User userFromManager = userManager.getUser(userDataArr[2]);
            if (userFromManager == null) {
                User user = new User(userDataArr[0], userDataArr[1], userDataArr[2], userDataArr[3]);
                userManager.addUser(user);
                System.out.println("User was successfully added ");

            } else {
                System.out.println("User already exists!");
            }
        } catch (Exception e) {
            System.out.println("Wrong Data!");

        }

    }

    private static void loginUser() {

        System.out.println("Please input email,password");
        try {
            String loginStr = scanner.nextLine();
            String[] loginArr = loginStr.split(",");
            User user = userManager.getUser(loginArr[0]);
            if (user != null && user.getPassword().equals(loginArr[1])) {
                currentUser = user;
                loginSuccess();
            } else {
                System.out.println("Wrong email or password");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong Data!");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void loginSuccess() throws SQLException {
        System.out.println("Welcome " + currentUser.getName() + " !");
        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    break;
                case MY_LIST:
                    userManager.printTodoByUser(currentUser);
                    break;
                case MY_IN_PROGRESS_LIST:
                    userManager.printMyInProgressList(currentUser);
                    break;
                case MY_FINISHED_LIST:
                    userManager.printMyFinishedTodoList(currentUser);
                    break;
                case ADD_TODO:
                    addNewTodo();
                    break;
                case CHANGE_TODO_STATUS:
                    changeTodoStatus();
                    break;
                case DELETE_TODO_BY_ID:
                    deleteById();
                    break;

                default:
                    System.out.println("Wrong command!");
            }
        }

    }

    private static void changeTodoStatus() throws SQLException {
        userManager.printTodoByUser(currentUser);
        System.out.println("please input id and status from list:" + Arrays.toString(Status.values()));
        String todoArr = scanner.nextLine();
        String[] todoArrStr = todoArr.split(",");
        userManager.changeTodo(Integer.parseInt(todoArrStr[0]), Status.valueOf(todoArrStr[1].toUpperCase()));


    }


    private static void deleteById() throws SQLException {
        System.out.println("Please choose Id from list");
        userManager.printTodoByUser(currentUser);
        int id = Integer.parseInt(scanner.nextLine());
        userManager.deleteTodoById(id);
        System.out.println("Todo success deleted!");

    }

    private static void addNewTodo() {
        System.out.println("Please input todo data name,deadline,status");
        System.out.println("Please choose status name from list: " + Arrays.toString(Status.values()));
        try {
            String todoDataStr = scanner.nextLine();
            String[] todoDataArr = todoDataStr.split(",");
            Todo todo = new Todo(todoDataArr[0], todoDataArr[1], Status.valueOf(todoDataArr[2].toUpperCase()), currentUser.getId());
            userManager.addTodo(todo);
            System.out.println("Todo was successfully added ");
        } catch (Exception e) {
            System.out.println("Wrong Data!");
        }
    }


}
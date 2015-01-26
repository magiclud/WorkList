package controllers;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result xml(String login, String passwd) {

        int id = 0;
        String msg = "";

        if( !login.equals("login") ) {
            id = 1;
            msg = "Unknown user";
        }
        else if( !passwd.equals("password") ) {
            id = 2;
            msg = "Wrong password";
        }

        return ok(index.render(msg, id));
    }
}

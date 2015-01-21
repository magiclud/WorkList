package controllers;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    //public static Result index() {
    //    return ok(index.render("Your new application is ready."));
    //}

    public static Result xml(String login, String passwd) {

        String msg = "";

        if( !login.equals("login") ) {
            msg = "Unknown user";
        }
        else if( !passwd.equals("password") ) {
            msg = "Wrong password";
        }

        return ok(index.render(msg));
    }
}

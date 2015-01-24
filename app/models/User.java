package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;


/**
 * Created by Vladimir on 14.12.2014.
 */

    @Entity
    @Table(name = "user")
    public class User extends Model {

        @Id
        @Constraints.Required(message = "Required")
        @Formats.NonEmpty
        public String email;

        @Constraints.Required(message = "Required")
        public String password;

        @Constraints.Required(message = "Required")
        public transient String confirmPassword;

        public User(){
        }

        public void setEmail(String email){
            this.email = email;
        }

        public void setPassword(String password){
            this.password = password;
        }

        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }

        public static Model.Finder<String,User> find = new Finder<>(String.class, User.class);

        public static List<User> findAll(){
            return find.all();
        }

        public static User findByEmail(String email){
            return find.where().eq("email", email).findUnique();
        }

        public static User findByName(String name){
            return find.where().eq("name", name).findUnique();
        }

        public static User authenticate(String email, String password){
            return find.where()
                    .eq("email", email)
                    .eq("password", password)
                    .findUnique();
        }

        static public boolean emailAvailable(String email){
            if (findByEmail(email)!= null)
                return false;
            return true;
        }

        static public boolean nameAvailable(String name){
            if (findByName(name) != null)
                return false;
            return true;
        }

        //create new user
        @Override
        public void save(){
            super.save();
        }

        // update user
        public void update(){
            super.save();
        }

        public String validate(){
            if (!emailAvailable(email))
                return "Электронный адрес уже зарегистрирован"; //change to i18

            if (!password.equals(confirmPassword))
                return "Пароли не совпадают";

            return null;
        }



    }



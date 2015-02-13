package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;


/**
 * Created by Vladimir on 14.12.2014.
 */

    @Entity
    @Table(name = "user")
    public class User extends Model {

        @Id
        @Constraints.Required
        @Formats.NonEmpty
        public String   email;

        @Constraints.Required
        public String    password;

        @Constraints.Required
        public transient String confirmPassword;

        public String   applicationName;

        public UUID     appKey;// (User.appKey == ErrorReportModel.apiKey)

        public String   description;

        @Formats.NonEmpty
        public String    lang;


        public User(String email, String password, String applicationName, UUID appKey, String description, String lang){
            this.email = email;
            this.password = password;
            this.applicationName = applicationName;
            this.appKey = appKey;
            this.description = description;
            this.lang = lang;

        }

        public void setEmail(String email){
            this.email = email;
        }

        public void setPassword(String password){
            this.password = password;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }

        public void setAppKey(UUID appKey) {
            this.appKey = appKey;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setLang(String lang){
            this.lang = lang;
        }


        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public UUID getAppKey() {
            return appKey;
        }

        public String getDescription() {
            return description;
        }

        public String getLang(){
            return lang;
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

        public static User create(String email, String password, String applicationName, UUID appKey, String description, String lang){

            User user = new User(email, password, applicationName, appKey, description, lang);
            user.save();
            return user;
    }
        @Override
        public void save(){//create new user
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
            if(lang == null)
                return "Выберите язык интерфейса";

            return null;
        }

        public static UUID uuidGeneration(){
            UUID x = UUID.randomUUID();

            return x;
        }

        public static User findByKey(UUID appKey){
            return find.where().eq("appKey", appKey).findUnique();
        }


    }



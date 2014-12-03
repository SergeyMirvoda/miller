package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Vladimir on 20.11.2014.
 *
 * Лучше генерировать мутаторы(геттеры, сеттеры) сразу, Ebean'овский кодогенератор глючит и не всегда их генерирует
 * как следствие непонятные падения
 */
@Entity
@Table(name="reports")
public class ErrorReportModel extends Model{
/*
*  Модель для получения и хранения сообщений об ошибках
*
* */
    @Id
    public UUID id;
    public UUID getId(){
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static void setFind(Finder<UUID, ErrorReportModel> find) {
        ErrorReportModel.find = find;
    }

    public String getMessage() {
        return message;

    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getStack() {
        return stack;
    }

    public String getDate() {
        return date;
    }

    public String getBrowser() {
        return browser;
    }

    public String getAgent() {
        return agent;
    }

    public String getClient() {
        return client;
    }

    public String getVersion() {
        return version;
    }

    public static Finder<UUID, ErrorReportModel> getFind() {
        return find;
    }

    public String message;
    public String url;
    public String user;
    public String stack;
    public String date;
    public String browser;
    public String agent;
    public String client;
    public String version;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String apiKey;
    public static Finder<UUID,ErrorReportModel> find = new Finder<>(
            UUID.class, ErrorReportModel.class
    );


    public static void save(ErrorReportModel model) {
        Ebean.save(model);
    }
}

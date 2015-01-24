package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by Vladimir on 20.11.2014.
 *
 * Лучше генерировать мутаторы(геттеры, сеттеры) сразу, Ebean'овский кодогенератор глючит и не всегда их генерирует
 * как следствие непонятные падения
 */
@Entity
@Table(name="reports")
public class ErrorReportModel extends Model {
/*
*  Модель для получения и хранения сообщений об ошибках
*
* */

    //Пользователь
    @Id
    public UUID id;
    public String user;
    public String apiKey;

    //Ошибки
    public String message;
    public String stack;
    public String date;
    public Integer happens = 1;

    //Запрос
    public String url;
    public String host;


    //Среда
    public String browser;  //браузер
    public String browserVendor;  //Название-Производитель
    public String browserVersion;   //Версия браузера
    public String agent;    // UserAgent
    public String browserResolution;
    public String browserLanguage;
    public String browserCookie;
    public String browserJava;
    public String platform;
    public String platformVersion;
    public String screenResolution;


    //
    public String client;
    public String version;



    static ErrorReportModel errorReportModel = new ErrorReportModel();


    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHappens(Integer happens) {
        this.happens = happens;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setBrowserVendor(String browserVendor) {
        this.browserVendor = browserVendor;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setBrowserResolution(String browserResolution) {
        this.browserResolution = browserResolution;
    }

    public void setBrowserLanguage(String browserLanguage) {
        this.browserLanguage = browserLanguage;
    }

    public void setBrowserCookie(String browserCookie) {
        this.browserCookie = browserCookie;
    }

    public void setBrowserJava(String browserJava) {
        this.browserJava = browserJava;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
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


    //Get
    public UUID getId(){
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getApiKey() {
        return apiKey;
    }


    public String getMessage() {
        return message;
    }

    public String getStack() {
        return stack;
    }

    public String getDate() {
        return date;
    }

    public Integer getHappens() {
        return happens;
    }


    public String getUrl() {
        return url;
    }

    public String getHost() {
        return host;
    }

    public String getAgent() {
        return agent;
    }


    public String getBrowser() {
        return browser;
    }

    public String getBrowserVendor() {
        return browserVendor;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getBrowserResolution() {
        return browserResolution;
    }

    public String getBrowserLanguage() {
        return browserLanguage;
    }

    public String getBrowserCookie() {
        return browserCookie;
    }

    public String getBrowserJava() {
        return browserJava;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getScreenResolution() {
        return screenResolution;
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


    public static Finder<UUID,ErrorReportModel> find = new Finder<>(
            UUID.class, ErrorReportModel.class
    );

    public static void save(ErrorReportModel model) {

        Ebean.save(model);
    }

    public static void update(ErrorReportModel model){
        Ebean.update(model);
    }

    public static void remove(ErrorReportModel model){
        Ebean.delete(model);
    }

    public static ErrorReportModel findByStack(String stack){
        return find.where().eq(stack, stack).findUnique();
    }

    public static boolean stackExist(String stack){
        if (findByStack(stack)!= null)
            return false;
        return true;
    }





}

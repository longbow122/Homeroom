package me.longbow122.Homeroom.utils;

import com.mongodb.MongoSecurityException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import me.longbow122.Homeroom.Main;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {

    private final String username;
    private final String password;
    private final MongoDatabase db;


    public DBUtils(String username, String password) {
        this.username = username;
        this.password = password;
        db = connect();
    }

    public MongoDatabase getHomeroomDB() {
        return db;
    }

    /**
     * Method to verify the connection to a database. Requires no parameters as is able to make use of the attributes of this class
     * to facilitate a connection instead. Returns an integer depending on the success or failure of the operation. 0 if successful.
     * @return 0 if successful. 1 if timed out connection or failed connection. 2 if bad credentials passed through.
     */
    public int isConnected() {
        Bson document = new Document("connectionStatus", 1);
        try {
            Document connection = db.runCommand(document);
            List authUserRoles = ((Document) connection.get("authInfo")).get("authenticatedUserRoles", List.class);
            if (!(authUserRoles.isEmpty())) {
                return 0;
            }
        } catch (MongoTimeoutException e ) { //Connection timed out, no point trying to connect again!
            return 1;
        } catch (MongoSecurityException e) { //Bad credentials passed through
            return 2;
        }
        return 1;
    }

    /**
     * Method written that converts MongoDB's pre-generated roles into a numerical representation that can be used throughout the
     * program to get permissions from the user, which will dictate what each user can do within Homeroom. <p></p> "readWriteAnyDatabase" is a permission that should generally be granted to Admins. <p></p>
     * "readAnyDatabase" is a permission that should be granted to general Users only. <p></p>
     * 1 if the user is able to read, but NOT EDIT any database. <p></p>
     * 2 if the user is able to read and write to any database. <p></p>
     * 0 if there is no role found/designated.
     * @return {@link Integer} representing the level of permissions the user has.
     */
    public int getPermission() {
        System.out.println("Getting authentication roles");
        Bson document = new Document("connectionStatus", 1);
        Document connection = db.runCommand(document);
        Document authUserRoles = ((Document) connection.get("authInfo"));
        String role = ((ArrayList<Document>) authUserRoles.get("authenticatedUserRoles")).get(0).get("role").toString();
        switch(role) {
            case "readWriteAnyDatabase":
                return 2;
            case "readAnyDatabase":
                return 1;
        }
        return 0;
    }

    private MongoDatabase connect() {
        String connectionString;
        try {
            connectionString = new ConfigReader(Main.getConfigFile()).getConnectionStringFromConfig().replace("<username>", URLEncoder.encode(username, "UTF-8")).replace("<password>", URLEncoder.encode(password, "UTF-8")).replace("\"", "");
            MongoClient cl = MongoClients.create(connectionString);
            return cl.getDatabase("Homeroom");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }
}

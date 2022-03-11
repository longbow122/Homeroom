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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

    private final String username;
    private final String password;
    private final MongoDatabase db;


    public DBUtils(String username, String password) {
        this.username = username;
        this.password = password;
        db = connect();
        // TODO
        // LOGIN LOGIC HERE.
    }

    /**
     * Method to verify the connection to a database. Requires no parameters as is able to make use of the attributes of this class
     * to facilitate a connection instead. Returns an integer depending on the success or failure of the operation. 0 if successful.
     * @return 0 if successfull.
     * @return 1 if timed out connection or failed connection.
     * @return 2 if bad credentials passed through.
     */
    public int isConnected() {
        System.out.println("Testing connection");
        Bson document = new Document("connectionStatus", 1);
        try {
            Document connection = db.runCommand(document);
            System.out.println(connection);
            List authUserRoles = ((Document) connection.get("authInfo")).get("authenticatedUserRoles", List.class);
            Map<String, Object> config = new HashMap<>();
            if (!(authUserRoles.isEmpty())) {
                return 0;
            }
        } catch (MongoTimeoutException e) { //Connection timed out, no point trying to connect again!
            return 1;
        } catch (MongoSecurityException e) {
            return 2;
        }
        return 1;
    }

    private MongoDatabase connect() {
        String connectionString;
        try {
            connectionString = new ConfigReader(Main.getConfigFile()).getConnectionStringFromConfig().replace("<username>", URLEncoder.encode(username, "UTF-8")).replace("<password>", URLEncoder.encode(password, "UTF-8")).replace("\"", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        MongoClient cl = MongoClients.create(connectionString);
        return cl.getDatabase("Homeroom");
    }

/*
TODO
Write to the DB, and upload data
Read from the DB, and download data
Write connect and disconnect methods
 */


}

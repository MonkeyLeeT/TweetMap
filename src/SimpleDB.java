import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;


public class SimpleDB {
    static AmazonSimpleDB sdb;
    static AmazonS3 s3;
    static String bucket = "text-test-" + UUID.randomUUID();
    static AWSCredentials credentials = null;

    public static void init(PropertiesCredentials p) throws Exception {

        try {
            credentials = p;
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location, and is in valid format.",
                    e);
        }

        sdb = new AmazonSimpleDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sdb.setRegion(usWest2);

        s3 = new AmazonS3Client(credentials);
        s3.setRegion(usWest2);
    }

    static HashMap<String, String> map = new HashMap<String, String>();

    public static List<SelectResult> selectFromTimeRange(String table, String start, String end) throws Exception{
        List<SelectResult> list = new LinkedList<SelectResult>();

        String selectExpression = "select * from `" + table + "` where created_at > '"+start+"' and created_at < '"+end+"'";
        System.out.println(selectExpression);

        SelectRequest selectRequest = new SelectRequest(selectExpression).withConsistentRead(true);
        com.amazonaws.services.simpledb.model.SelectResult result = sdb.select(selectRequest);

        int count = 0;
        List<Item> items = result.getItems();
        String next = sdb.select(selectRequest).getNextToken();

        while (true) {
            count += items.size();

            for (Item item : items ) {
                SelectResult sr = new SelectResult(item.getName());

//	            System.out.println("  Item");
//	            System.out.println("    Name: " + item.getName());

                List<Attribute> attributes = item.getAttributes();
                for (Attribute attribute : attributes) {
//	                System.out.println("      Attribute");
//	                System.out.println("        Name:  " + attribute.getName());
//	                System.out.println("        Value: " + attribute.getValue());

                    switch (attribute.getName()) {
                        case "text":
                            sr.setText(attribute.getValue());
                            break;
                        case "coor1":
                            sr.setCoor1(attribute.getValue());
                            break;
                        case "coor2":
                            sr.setCoor2(attribute.getValue());
                            break;
                        case "created_at":
                            sr.setTime(attribute.getValue());
                            break;
                    }
                }

                list.add(sr);
            }

            if (next!=null) {
                selectRequest.setNextToken(next);
                result = sdb.select(selectRequest);
                items = result.getItems();
                next = sdb.select(selectRequest).getNextToken();
            } else {
                break;
            }
        }

        System.out.println("total number of items " + count);

        return list;
    }

    public static void main(String[] args) throws Exception {
        //init();

        // show tables with item count
//    	listDomains();

        // insert to four tables
//    	insertAll();

        // example usage
        System.out.println(selectFromTimeRange("movie", "0", "1414347967000").size());

    }
}
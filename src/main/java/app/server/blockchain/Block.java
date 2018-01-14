package app.server.blockchain;

import app.Utils;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.model.ContainerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anat ana on 07/01/2018.
 */


public class Block {

    private List<AbstractTransaction> transactions;
    private long timestamp;
    private String blockId;
    /*optional:
    * Set<AbstractTransaction> pool;
    * and information about previous transactions
    * */

    public Block(List<AbstractTransaction> transactions, String blockId) {
        this.transactions = transactions;
        timestamp = System.currentTimeMillis();
        this.blockId = blockId;
    }

    public long getTimeStamp() {
        return this.timestamp;
    }

    public List<? extends AbstractTransaction> getTransactions() {
        //return Arrays.asList(this.transactions);
        return this.transactions;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public AbstractTransaction getTransactionById(Block block, String id) {
        for (AbstractTransaction transaction : transactions) {
            if (transaction.getID().equals(id)) {
                return transaction;
            }
        }
        return null;
    }

    public void addTransactionToBlock(AbstractTransaction transaction) {
        this.transactions.add(transaction);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Block parseString(String s) {
        Gson gson = new Gson();
        Block resp = null;
        try {
            System.out.println("Parsing string...");
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(s);
            System.out.println("Getting transactions array...");
            JSONArray transArray = (JSONArray) jsonObject.get("transactions");
            System.out.println("Getting timestamp...");
            long timestamp = (Long) jsonObject.get("timestamp");
            System.out.println("Getting blockId...");
            String blockId = (String) jsonObject.get("blockId");
            System.out.println("After getting blockId...");
            List<AbstractTransaction> transactionList = new ArrayList<>();
            for (int i = 0; i < transArray.size(); i++) {
                System.out.println("Getting item from array...");
                Object objectString = transArray.get(i);
                System.out.println("objectString[" + i + "] : " + objectString);
                transactionList.add((gson.fromJson(objectString.toString(), Utils.getTransactionClassObjectFromString(objectString.toString()))));
            }
            resp = new Block(transactionList, blockId);
            resp.setTimestamp(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(77);
        }
        /*try {
            System.out.println("Calling fromJson... " + s);
            resp = gson.fromJson(s, Block.class);
            System.out.println("Parsed block = " + resp.toString());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }*/
        return resp;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String transactionsJson = gson.toJson(this.transactions, new TypeToken<ArrayList<ContainerModel>>() {
        }.getType());
        String timestampJson = gson.toJson(this.timestamp);
        String blockIdJson = gson.toJson(this.blockId);
        System.out.println(transactionsJson);
        System.out.println(timestampJson);
        System.out.println(blockIdJson);
        System.out.println(transactions.get(0).getClass().getName());
        return new Gson().toJson(this);
    }
}

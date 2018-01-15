package app.server.blockchain;

import app.Utils;
import app.server.servers.jersey.model.AbstractTransaction;
import com.google.gson.Gson;
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

    public List<? extends AbstractTransaction> getTransactions() {
        //return Arrays.asList(this.transactions);
        return this.transactions;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Block parseString(String s) {
        Gson gson = new Gson();
        Block resp = null;
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(s);
            JSONArray transArray = (JSONArray) jsonObject.get("transactions");
            long timestamp = (Long) jsonObject.get("timestamp");
            String blockId = (String) jsonObject.get("blockId");
            List<AbstractTransaction> transactionList = new ArrayList<>();
            for (int i = 0; i < transArray.size(); i++) {
                Object objectString = transArray.get(i);
                transactionList.add((gson.fromJson(objectString.toString(), Utils.getTransactionClassObjectFromString(objectString.toString()))));
            }
            resp = new Block(transactionList, blockId);
            resp.setTimestamp(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(77);
        }

        return resp;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

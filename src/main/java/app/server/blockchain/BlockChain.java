package app.server.blockchain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anat ana on 07/01/2018.
 */
public class BlockChain {
    private List<Block> blocks;

    public BlockChain() {
        this.blocks = new ArrayList<>();
    }


    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void addBlock(Block block) {
        System.out.println("Adding block to blockchain");
        blocks.add(block);
    }

}

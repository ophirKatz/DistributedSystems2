package app.server.blockchain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anat ana on 07/01/2018.
 */
public class BlockChain {
    private List<Block> blocks;

    private String creatorServerId;

    public BlockChain() {
        this.blocks = new ArrayList<>();
    }

    public void setCreatorServerId(String creatorServerId) {
        this.creatorServerId = creatorServerId;
    }


    public String getCreatorServerId() {
        return this.creatorServerId;
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void addBlock(Block block) {
        System.out.println("Adding block to blockchain");
        blocks.add(block);
    }

    public Block getBlockById(String id) {
        for (Block block : blocks) {
            if (block.getBlockId().equals(id)) {
                return block;
            }
        }
        return null;
    }

}

package blockchain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anat ana on 07/01/2018.
 */
public class BlockChain {
    private List<Block> blocks;
    private String creatorServerId;

    public BlockChain(String creatorServerId) {
        this.blocks = new ArrayList<>();
        this.creatorServerId = creatorServerId;
    }

    public BlockChain(List<Block> blocks, String creatorServerId) {
        this.blocks = blocks;
        this.creatorServerId = creatorServerId;
    }

    public String getCreatorServerId() {
        return this.creatorServerId;
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void addBlock(BlockChain blockChain, Block block) {
        blockChain.getBlocks().add(block);
    }

    public Block getBlockById(BlockChain blockChain, String id) {
        for (Block block : blockChain.getBlocks()) {
            if (block.getBlockId().equals(id)) {
                return block;
            }
        }
        return null;
    }

}

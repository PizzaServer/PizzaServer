package io.github.pizzaserver.api.event.type.block;

import io.github.pizzaserver.api.block.impl.BlockSign;

/**
 * Called when a player edits the text on a sign.
 */
public class SignChangeEvent extends BaseBlockEvent.Cancellable {

    protected String text;

    public SignChangeEvent(BlockSign block, String text) {
        super(block);
        this.text = text;
    }

    @Override
    public BlockSign getBlock() {
        return (BlockSign) super.getBlock();
    }

    public String getOldText() {
        return this.getBlock().getBlockEntity().getText();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

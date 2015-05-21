package org.intermine.bio.item.util;
import org.apache.log4j.Logger;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.xml.full.Item;

public class ItemHolder {

	protected static final Logger log = Logger.getLogger(TaskletStep.class);
	
	private Item item;
	private int itemId;
		
	public ItemHolder(Item item, int itemId) {
		this.item = item;
		this.itemId = itemId;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	@Override
	public String toString() {
		return "ItemHolder [item=" + item + ", itemId=" + itemId + "]";
	}
	
}

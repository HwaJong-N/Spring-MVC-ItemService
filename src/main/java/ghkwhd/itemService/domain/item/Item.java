package ghkwhd.itemService.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer proce, Integer quantity) {
        this.itemName = itemName;
        this.price = proce;
        this.quantity = quantity;
    }
}

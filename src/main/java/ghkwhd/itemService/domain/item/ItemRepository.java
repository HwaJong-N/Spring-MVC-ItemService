package ghkwhd.itemService.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
// @Repository가 붙어있으므로 컴포넌트 스캔의 대상이 된다
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); // 동시에 여러 개가 접근할 때는 HashMap 사용 X
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);

        // Item객체를 사용하는 것보다 DTO를 만들어서 수정하는 것이 좋음
        // DTO에는 id를 제외하고 남은 3가지 필드가 들어감
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }


}

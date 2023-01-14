package ghkwhd.itemService.web.basic;

import ghkwhd.itemService.domain.item.Item;
import ghkwhd.itemService.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    // ItemRepository가 자동 주입 (스프링 빈으로 등록되어 있기 때문에)
    // 생성자가 하나만 있는 경우, @Autowired 생략 가능
    // @RequiredArgsConstructor를 사용하면 아래 생성자 코드 생략 가능
        // final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다
    /*
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    */

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @PostConstruct
    // 테스트용 데이터 추가
    // 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
    
/*
    @PostMapping("/add")
    // input 태그의 name 속성으로 넘어옴
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {

        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);

        model.addAttribute(item);

        return "basic/item";
    }
*/

    @PostMapping("/add")
    // input 태그의 name 속성으로 넘어옴
    public String addItemV2(@ModelAttribute("item") Item item,
                            RedirectAttributes redirectAttributes) {

        /*
        @ModelAttribute가 Item 객체를 생성해 파라미터로 받은 값들을 넣어준다 ( 1. 요청 파라미터 처리 )
         Model에 @ModelAttribute로 지정한 객체를 자동으로 넣어준다 ("item")이라는 이름으로 ( 2. Model 추가 )
         이름을 생략하면 클래스명의 첫 글자를 소문자로 변경해서 model에 등록한다 (Item --> item)
         @ModelAttribute도 생략 가능
         */

        Item savedItem = itemRepository.save(item);

        /*
        return "basic/item";
         위처럼 코드를 작성하면 상품 등록 후, 상품 상세 페이지가 나왔을 때 새로고침하면
         POST 요청이 계속 반복해서 이루어져 중복으로 상품이 등록됨
         새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출
             PRG Post/Redirect/Get

         //return "redirect:/basic/items/" + item.getId();

         */

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        /*
        RedirectAttributes : 리다이렉트할 때 파라미터를 붙여서 보냄
        itemId는 아래 return 문에 들어가고, 들어가지 못한 status는
        쿼리 파라미터 형식으로 전달됨
         */
        
        // redirectAttribute를 사용했기 때문에 아래 처럼 return 가능
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        // {itemId} 는 @PathVariable Long itemId 의 값을 그대로 사용
        return "redirect:/basic/items/{itemId}";
    }

}

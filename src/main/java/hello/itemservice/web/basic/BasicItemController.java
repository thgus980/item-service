package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items=itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item=itemRepository.findById(itemId);
        model.addAttribute("item",item); //Thymeleaf 에서 item 쓸 수 있음
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() { // 단순히 뷰 템플릿만 호출
        return "basic/addForm";
    }

//    @PostMapping("/add") //위 메소드와 같은 url 이어도 메서드 타입으로 기능을 구분. add form 에서 포스트로 올 때 호출 됨.
    public String addItemV1(@RequestParam String itemName, // addForm.html 에서 Form 안에서 name= 에 해당하는 값
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item=new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
//        @ModelAttribute("item") 를 사용하여 아래 코드 모두 대체
//        Item item=new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item); // @ModelAttribute("item") 를 사용하여 해당 코드 대체. 자동 추가, 생략 가능. 파라미터로 들어온 클래스 Item 의 첫 글자를 소문자로 바꾼 item 을 @ModelAttribute("item") 처럼 작성

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute("item") Item item) { // V2 에서 파리미터 model 생략 가능, model.addAttribute("item",item); 생략 가능

        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) { // V3 에서 @ModelAttribute 생략 가능 (내가 맨든 객체니까)

        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item) { // V3 에서 @ModelAttribute 생략 가능 (내가 맨든 객체니까)

        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) { // V3 에서 @ModelAttribute 생략 가능 (내가 맨든 객체니까)
        Item savedItem=itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}"; // redirectAttributes.addAttribute("itemId", savedItem.getId()); 에서 itemId가 치환 되어 {}에 들어간다. 나머지 status 는 url 에 쿼리 파라미터로.
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item=itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute  Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; // PathVariable itemId 치환
    }

    // 테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));
    }


}

package hello.itemservice.domain.item;

import lombok.Data;

@Data // 실무에서는 쓰면 위험. 다양한 기능을 지원하기 떄문에 예측하지 못한 작업이 발생될 수도 있음
public class Item {

    private Long id;
    private String itemName;
    private Integer price; // null 이 들어갈 수 있도록 -> int 대신 integer 로
    private Integer quantity;

    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity){
        this.itemName=itemName;
        this.price=price;
        this.quantity=quantity;
    }

}

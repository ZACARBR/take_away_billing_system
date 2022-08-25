import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        List<Item> itemList = itemRepository.findAll();
        List<SalesPromotion> promotionList = salesPromotionRepository.findAll();
        // cast item list to map
        Map<String, Item> itemMap = itemList.stream().collect(Collectors.toMap(Item::getId, item -> item));
        Map<String, String> specifiedItemsMap = promotionList.get(1).getRelatedItems().stream().collect(Collectors.toMap(itemId -> itemId, itemId -> itemId));
        StringBuilder result = new StringBuilder();
        int totalPrice = 0, amountDiscount = 0, specifiedItemsDiscount = 0;
        result.append("============= Order details =============\n");
        try {
            for (String orderString : inputs) {
                String[] orderItemArray = orderString.split(" ");
                Item item = itemMap.get(orderItemArray[0]);
                int orderItemTotalPrice = (int) item.getPrice() * Integer.valueOf(orderItemArray[2]);
                totalPrice += orderItemTotalPrice;
                if (specifiedItemsMap.containsKey(item.getId())) {
                    specifiedItemsDiscount += (int) item.getPrice() / 2;
                }
                result.append(item.getName() + " x " + orderItemArray[2] + " = " + orderItemTotalPrice + " yuan\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.append("-----------------------------------\n");
        if (totalPrice > 0 && specifiedItemsDiscount > 0) {
            if (totalPrice >= 30) {
                amountDiscount = (totalPrice / 30) * 6;
            }
            if (specifiedItemsDiscount > amountDiscount) {
                result.append("Promotion used:\n" +
                        "Half price for certain dishes (Braised chicken，Cold noodles)，saving " + specifiedItemsDiscount + " yuan\n" +
                        "-----------------------------------\n");
                totalPrice -= specifiedItemsDiscount;
            } else {
                result.append("Promotion used:\n" +
                        "满30减6 yuan，saving 6 yuan\n" +
                        "-----------------------------------\n");
                totalPrice -= amountDiscount;
            }
        }
        result.append("Total：" + totalPrice + " yuan\n" +
                "===================================");
        return result.toString();
    }
}
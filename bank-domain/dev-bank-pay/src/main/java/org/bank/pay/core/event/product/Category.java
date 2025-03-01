package org.bank.pay.core.event.product;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Category {

    PRODUCT("PRODUCT", Arrays.asList(CategoryType.GENERAL, CategoryType.CASHABLE, CategoryType.DIGITAL_CONTENT, CategoryType.SUPPORT)),
    FOOD("FOOD", Arrays.asList(CategoryType.DELIVERY)),
    ETC("ETC", Arrays.asList(CategoryType.ETC));


    private final String categoryType;
    private final List<CategoryType> categories;

    public static Category of(CategoryType categoryType) {
        return Arrays.stream(Category.values())
                .filter(categories -> categories.getCategories().stream()
                            .anyMatch(category -> category.equals(categoryType)))
                .findAny()
                .orElse(ETC);
    }

    public static String category(CategoryType categoryType) {
        return of(categoryType).getCategoryType();
    }

    @Getter
    public enum CategoryType {

        GENERAL("GENERAL", "000", "일반 상품"),
        CHARGE("CASHABLE", "001", "온라인 캐시 충전"),
        CASHABLE("CASHABLE","002", "환금성 상품"),
        DIGITAL_CONTENT("DIGITAL_CONTENT", "003", "디지털 컨텐츠"),
        SUPPORT("SUPPORT", "004", "후원"),
        DELIVERY("FOOD", "005", "음식"),
        ETC("ETC", "006", "해당하는 categoryType 및 categoryId가 없는 경우 사용");

        private final String categoryId;
        private final String uid;
        private final String description;

        CategoryType(String categoryId, String uid, String description) {
            this.categoryId = categoryId;
            this.uid = uid;
            this.description = description;
        }
    }

    Category(String categoryType, List<CategoryType> categories) {
        this.categoryType = categoryType;
        this.categories = categories;
    }
}


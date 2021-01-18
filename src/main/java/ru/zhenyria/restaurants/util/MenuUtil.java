package ru.zhenyria.restaurants.util;

import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.to.MenuTo;

public class MenuUtil {
    private MenuUtil() {
    }

    public static MenuTo getToFromMenu(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().id(), menu.getDishes());
    }
}

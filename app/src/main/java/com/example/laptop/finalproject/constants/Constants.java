package com.example.laptop.finalproject.constants;

/**
 * Created by laptop on 09/05/2017.
 */

public class Constants {

    //Tab layout
    public static final CharSequence TAB_TITLES[]={"Overview","User Reviews","Daily Menu"};
    public static final int NUMBER_OF_TABS = 3;

    //API data
    public static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";
    public static final String API_KEY = "user-key: 8b1de1b1e12c75f0cdc90defe4e359ef";
    public static final String HEADER_ACCEPT = "Accept: application/json";
    public static final String USE_MY_LOCATION = "User Location";

    /**
     * American = 1
     * British = 33
     * Burger = 168
     *  Chinese = 25
     *  Desserts = 100
     *  Fast Food = 40
     *  Fish and Chips = 298
     *  Grill = 181
     *  Healthy Food = 143
     *  Indian = 148
     *  International = 154
     *  Italian = 55
     *  Kebab = 178
     *  Pizza = 82
     *  Sushi = 177
     */

    public static final String [] CUISINE_ID_LIST = {"0", "1", "33", "168", "25", "100", "40", "298", "181", "143",
            "148", "154", "55", "178", "82", "177"};

    /**
     *     "categories": {
     "id": 1,
     "name": "Delivery"
     }
     },
     {
     "categories": {
     "id": 2,
     "name": "Dine-out"
     }
     },
     {
     "categories": {
     "id": 3,
     "name": "Nightlife"
     }
     },
     {
     "categories": {
     "id": 4,
     "name": "Catching-up"
     }
     },
     {
     "categories": {
     "id": 5,
     "name": "Takeaway"
     }
     },
     {
     "categories": {
     "id": 6,
     "name": "Cafes"
     }
     },
     {
     "categories": {
     "id": 7,
     "name": "Daily Menus"
     }
     },
     {
     "categories": {
     "id": 8,
     "name": "Breakfast"
     }
     },
     {
     "categories": {
     "id": 9,
     "name": "Lunch"
     }
     },
     {
     "categories": {
     "id": 10,
     "name": "Dinner"
     }
     },
     {
     "categories": {
     "id": 11,
     "name": "Pubs & Bars"
     }
     },
     {
     "categories": {
     "id": 13,
     "name": "Pocket Friendly Delivery"
     }
     },
     {
     "categories": {
     "id": 14,
     "name": "Clubs & Lounges"
     }

     */
    public static final String [] CATEGORY_ID_LIST = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10", "11", "13", "14"};

    //English strings
    public static final String EN_POSTCODE_HINT = "Enter postcode";
    public static final String EN_OR = "OR";
    public static final String EN_USE_LOCATION = "Use my location";
    public static final String EN_CUISINE = "Cuisine: ";
    public static final String EN_CATEGORY = "Category: ";
    public static final String EN_PRICE = "Maximum price: ";
    public static final String EN_RATING = "Minimum rating: ";
    public static final String EN_BUTTON = "Find Restaurants";
    public static final String EN_MAIN_TOOLBAR_TITLE = "Restaurant Finder";
    public static final String EN_FILTERS = "Filters (Optional)";
    public static final String [] EN_CUISINE_LIST = {"Select a cuisine", "American", "British", "Burger",
            "Chinese", "Desserts", "Fast Food", "Fish and Chips", "Grill", "Healthy Food", "Indian",
            "International", "Italian", "Kebab", "Pizza", "Sushi"};
    public static final String [] EN_CATEGORY_LIST = {"Select a category", "Delivery", "Dine-out", "Nightlife", "Catching-up",
    "Takeaway", "Cafes", "Daily Menus", "Breakfast", "Lunch", "Dinner", "Pubs & Bars",
            "Pocket Friendly Delivery", "Clubs & Lounges"};
    public static final String [] EN_PRICE_LIST = {"Select maximum price", "£", "££", "£££", "££££"};
    public static final String [] EN_RATING_LIST = {"Select minimum rating", "1", "2", "3", "4", "5"};
    public static final String EN_TOAST_ONLY_ONE_INPUT = "Error: Only either enter a postcode or select 'Use my location'";
    public static final String EN_TOAST_INVALID_POSTCODE = "Error: Invalid Postcode";

    //Bulgarian strings
    public static final String BG_POSTCODE_HINT = "Въведете пощенски код";
    public static final String BG_OR = "ИЛИ";
    public static final String BG_USE_LOCATION = "Използвай моето местоположение";
    public static final String BG_CUISINE = "Вид на кухнята: ";
    public static final String BG_CATEGORY = "Категория: ";
    public static final String BG_PRICE = "Максимална цена: ";
    public static final String BG_RATING = "Минимален рейтинг: ";
    public static final String BG_BUTTON = "Намери Ресторанти";
    public static final String BG_MAIN_TOOLBAR_TITLE = "Търсач на Ресторанти";
    public static final String BG_FILTERS = "Филтри (Незадължителни)";
    public static final String [] BG_CUISINE_LIST = {"Изберете вид кухня", "Американска", "Британска", "Бургер",
            "Китайска", "Десерти", "Бързо хранене", "Риба с чипс", "Грил", "Здравословна храна", "Индийска",
            "Интернационална", "Италианска", "Кебап", "Пица", "Суши"};
    public static final String [] BG_CATEGORY_LIST = {"Изберете категория", "Доставка", "Ресторант", "Нощен", "За Среща",
            "За Вкъщи", "Кафенета", "Дневни Менюта", "Закуска", "Обяд", "Вечеря", "Кръчми и Барове",
            "Евтина Доставка", "Клубове и Салони"};
    public static final String [] BG_PRICE_LIST = {"Изберете максимална цена", "£", "££", "£££", "££££"};
    public static final String [] BG_RATING_LIST = {"Изберете минимален рейтинг", "1", "2", "3", "4", "5"};
    public static final String BG_TOAST_ONLY_ONE_INPUT = "Грешка: Или въведте пощенски код, или изберете 'Използвай моето местоположение'";
    public static final String BG_TOAST_INVALID_POSTCODE = "Грешка: Невалиден пощенски код";
}

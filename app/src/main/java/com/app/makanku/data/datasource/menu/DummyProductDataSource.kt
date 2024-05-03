package com.app.makanku.data.datasource.menu

// class DummyProductDataSource : ProductDataSource {
//    override fun getProducts(): List<Product> {
//        return listOf(
//            Product(
//                name = "Pancakes",
//                imgUrl = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/img_food.jpg?raw=true",
//                price = 50000.0,
//                desc = "Pancakes are a popular breakfast dish enjoyed worldwide. They are made from a simple batter consisting of flour, eggs, milk, and usually a leavening agent such as baking powder. The batter is poured onto a hot griddle or frying pan and cooked until golden brown on both sides.\n" +
//                "Pancakes have a soft, fluffy texture and a slightly sweet flavor, making them versatile for various toppings and accompaniments. Common toppings include butter, maple syrup, fresh fruit, whipped cream, chocolate chips, or nuts. In some regions, pancakes may also be served with savory toppings like bacon, eggs, or cheese.\n" +
//                        "These breakfast delights are celebrated for their simplicity, delicious taste, and ability to be customized to suit individual preferences. They are often enjoyed not only for breakfast but also as a comforting treat any time of the day. Pancakes are a staple in many cultures and are cherished for their nostalgic and comforting qualities." ,
//                location = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345",
//                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
//            ),
//            Product(
//                name = "Orage Juice",
//                imgUrl = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/img_drink.jpg?raw=true",
//                price = 20000.0,
//                desc = "Orange juice is a beloved beverage crafted by extracting the juice from oranges. Its vibrant color and tangy flavor make it a refreshing choice enjoyed by many. While some varieties may contain added sugars or preservatives, it's important to seek out pure juice options for the most authentic taste and nutritional benefits.\n" +
//                        "Rich in vitamin C and other essential nutrients, orange juice offers a refreshing way to boost your daily intake of vitamins and minerals. Whether enjoyed cold in the morning, mixed into smoothies, or used as a base for cocktails, its versatility makes it a staple in many households. However, it's wise to consume orange juice in moderation due to its naturally occurring sugars.\n" +
//                        "Overall, orange juice stands as a timeless classic in the world of beverages, cherished for its simplicity, refreshing taste, and nutritional value. Whether savored alone or incorporated into various culinary creations, it remains a beloved choice for those seeking a taste of sunshine in a glass.",
//                location = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345",
//                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
//            ),
//            Product(
//                name = "Pop Corn",
//                imgUrl = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/img_pop_corn.jpg?raw=true",
//                price = 40000.0,
//                desc = "Popcorn is a favorite snack worldwide, made from dried corn kernels that \"pop\" when heated. It's prepared by heating the kernels in oil or air-popping them, causing them to burst into fluffy pieces. This snack comes in various forms, including classic buttered popcorn, flavored varieties like cheese or caramel, and healthier options such as air-popped with minimal seasoning.\n" +
//                        "Popcorn is a popular choice for movie nights, sports events, and casual gatherings due to its delicious taste and satisfying crunch. It's also a relatively low-calorie snack when prepared without excessive butter or oil, making it a healthier alternative to many other snacks. Whether enjoyed plain or with creative toppings, popcorn is a timeless treat that brings joy to people of all ages.",
//                location = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345",
//                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
//            ),
//            Product(
//                name = "Fruit Bag",
//                imgUrl = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/bg_banner.jpg?raw=true",
//                price = 100000.0,
//                desc = "A fruit bag is a convenient and portable way to carry and enjoy fresh fruits. These bags are typically made from lightweight, breathable materials like cloth or mesh, allowing air circulation to keep the fruits fresh for longer periods. Fruit bags come in various sizes and designs, often featuring handles or drawstrings for easy carrying and closure. They are ideal for storing a variety of fruits such as apples, bananas, grapes, and oranges, making them a practical choice for picnics, lunches, or on-the-go snacking. Additionally, fruit bags are eco-friendly alternatives to single-use plastic bags, promoting sustainability and reducing waste. Overall, fruit bags offer a convenient solution for keeping fruits fresh and accessible while minimizing environmental impact.",
//                location = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345",
//                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
//            ),
//            Product(
//                name = "Burger",
//                imgUrl = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/img_burger.jpg?raw=true",
//                price = 60000.0,
//                desc = "A burger is a popular food item consisting of a cooked patty of ground meat, typically beef, chicken, turkey, or plant-based alternatives like veggie or bean patties, served between two halves of a bun. The patty is often seasoned and cooked on a grill, griddle, or skillet to enhance its flavor and texture. Burgers are customizable and may include various toppings such as lettuce, tomato, onion, cheese, pickles, sauces like ketchup or mustard, and condiments like mayonnaise or barbecue sauce. They are commonly served alongside side dishes like fries, onion rings, or a salad.\n" +
//                        "Burgers are a beloved comfort food enjoyed in various settings, including fast-food restaurants, casual diners, and upscale eateries. They are known for their satisfying taste, versatility, and convenience, making them a favorite choice for meals on the go or casual dining experiences. Whether classic or gourmet, burgers remain a timeless culinary delight appreciated by people of all ages around the world.",
//                location = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345",
//                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
//            ),
//            Product(
//                name = "Bread",
//                imgUrl = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/img_bread.jpg?raw=true",
//                price = 25000.0,
//                desc = "Bread is a staple food made from flour, water, yeast, and salt, which is typically baked. It comes in various forms, including loaves, rolls, baguettes, and flatbreads, and can be made from different grains such as wheat, rye, barley, or oats. Bread serves as a versatile base for sandwiches, toast, and accompaniments to meals. It is valued for its texture, flavor, and ability to complement a wide range of dishes.\n" +
//                        "The process of making bread involves mixing the ingredients, kneading the dough to develop gluten, allowing it to rise through fermentation, shaping it, and then baking it until golden brown. This process creates a soft interior with a crusty exterior, contributing to bread's appeal. Beyond its basic ingredients, bread can be enriched with additions like seeds, nuts, herbs, or sweeteners for added flavor and texture. As a fundamental component of many cuisines worldwide, bread holds cultural significance and is cherished for its role in bringing people together around the table.",
//                location = "Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tangerang, Banten 15345",
//                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
//            ),
//        )
//    }
// }

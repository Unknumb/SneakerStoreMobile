import com.example.appsneakerstore.model.Product

object MockData {
    val products = listOf(
        Product(
            id = 1,
            name = "Nike Air Max 95 Corteiz Gutta Green",
            price = 119990.0,
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQvUcbUNdg_8frDdK7PdSqNEfNSvk9wWDot3DtrWeCXynh3lCy1tPobG6HBZlFNTQlI7E&usqp=CAU",
            description = "Nike Air Max 95 edición Corteiz Gutta Green.",
            sizes = listOf(40, 41, 42, 43, 44, 45)
        ),
        Product(
            id = 2,
            name = "Jordan 4 Retro Black Cat",
            price = 139990.0,
            imageUrl = "https://dripchileno.cl/wp-content/uploads/2023/09/Diseno-sin-titulo-8.jpg",
            description = "Modelo clásico Jordan 4 Retro.",
            sizes = listOf(40, 41, 42, 43, 44, 45)
        ),
        Product(
            id = 3,
            name = "adidas Yeezy Desert BootOil",
            price = 189990.0,
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfd0hDelatoQPg_oZaKK3_0AZiketZiF19Ps0v5AyrfYDupxswBKlvoFb5Q-Ad0mUtWs0&usqp=CAU",
            description = "Yeezy Desert BootOil edición Adidas.",
            sizes = listOf(40, 41, 42, 43, 44, 45)
        ),
        // Nuevos productos con URLs actualizadas
        Product(
            id = 4,
            name = "Nike Air Max Plus Lisboa",
            price = 149990.0,
            imageUrl = "https://www.summersnkrs.com/cdn/shop/files/Nike-Air-Max-Plus-Lisboa-2.png?v=1760021442",
            description = "Edición especial Lisboa de las Air Max Plus.",
            sizes = listOf(40, 41, 42, 43, 44, 45)
        ),
        Product(
            id = 5,
            name = "Nike Shox R4",
            price = 129990.0,
            imageUrl = "https://static.nike.com/a/images/w_1280,q_auto,f_auto/7885e6d8-4906-4268-b3fc-759671cf1d59/shox-r4-racer-blue-and-metallic-silver-hj7303-445-release-date.jpg",
            description = "Shox R4 modelo Racer Blue.",
            sizes = listOf(40, 41, 42, 43, 44, 45)
        ),
        Product(
            id = 6,
            name = "NOCTA x Nike Hot Step 2",
            price = 169990.0,
            imageUrl = "https://highxtar.com/wp-content/uploads/2024/03/thumb-nike-nocta-hot-step-2-1440x1080.jpg",
            description = "Colaboración NOCTA x Nike en su segunda edición.",
            sizes = listOf(40, 41, 42, 43, 44, 45)
        )
    )
}

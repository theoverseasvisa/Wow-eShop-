package com.example.data

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Float,
    val reviewCount: Int,
    val description: String,
    val specs: List<ProductSpec>,
    val colorHexes: List<String>,
    val sizes: List<String>,
    val imageKey: String // Used to map to customized Canvas drawing/3D graphics
)

data class ProductSpec(
    val title: String,
    val value: String
)

object DummyProductData {
    val categories = listOf(
        "Electronics",
        "Fashion",
        "Home & Kitchen",
        "Beauty",
        "Accessories",
        "Gadgets",
        "Lifestyle"
    )

    val products = listOf(
        Product(
            id = 101,
            name = "Zenith Holographic Smartwatch",
            category = "Electronics",
            price = 18999.00,
            originalPrice = 24999.00,
            rating = 4.9f,
            reviewCount = 124,
            description = "A visual and technical masterpiece. The Zenith Holographic Smartwatch features high-grade optical-prism projection that displays notifications, biometric telemetry, and holographic watchfaces directly in mid-air above the ceramic crystal lens.",
            specs = listOf(
                ProductSpec("Prism Display", "650 nits Holographic Laser"),
                ProductSpec("Body Material", "Liquid Titanium & Zirconia Ceramic"),
                ProductSpec("Battery Life", "7 Days Active Projection"),
                ProductSpec("Sensors", "9-Axis Matrix, ECG, SPO2, Biometrics")
            ),
            colorHexes = listOf("#1E56FC", "#0C2B94", "#111827", "#E2E8F0"),
            sizes = listOf("40mm", "44mm"),
            imageKey = "watch"
        ),
        Product(
            id = 102,
            name = "Obsidian Neo-Leather Jacket",
            category = "Fashion",
            price = 12499.00,
            originalPrice = 15999.00,
            rating = 4.8f,
            reviewCount = 82,
            description = "Crafted from sustainable bio-engineered premium leather, our Obsidian Classic is designed with an asymmetric futuristic cut. Includes integrated ultra-thin thermal filaments that maintain comfortable skin temperature adjusted via the Wow App.",
            specs = listOf(
                ProductSpec("Material", "Neo-Skin Vegan Bio-Leather"),
                ProductSpec("Lining", "Recycled Carbon-Weave Thermal Mesh"),
                ProductSpec("Clasp System", "Fidlock® High-Speed Magnetic Buckles"),
                ProductSpec("Resistance", "Hydrophobic Nanotech Shield")
            ),
            colorHexes = listOf("#090A0C", "#1E293B", "#1E3A8A"),
            sizes = listOf("S", "M", "L", "XL"),
            imageKey = "jacket"
        ),
        Product(
            id = 103,
            name = "Aura Wireless Sound Arc",
            category = "Accessories",
            price = 24999.00,
            originalPrice = 29999.00,
            rating = 5.0f,
            reviewCount = 143,
            description = "Ditch conventional ear canals. The Aura Sound Arc delivers personalized high-fidelity audio directly to your inner ear via advanced silver bone-conduction transducers. Sits comfortably around your occipital ridge for an ambient soundstage.",
            specs = listOf(
                ProductSpec("Transducers", "Dual Titanium Bone Conduction"),
                ProductSpec("Audio Codec", "LDAC Pro, AAC, AptX Adaptive"),
                ProductSpec("Noise Control", "Out-of-phase Sonic Cancellation"),
                ProductSpec("Weight", "28g Ultra-light feather alloy")
            ),
            colorHexes = listOf("#E2E8F0", "#1E56FC", "#090A0C"),
            sizes = listOf("Standard Flex"),
            imageKey = "headphones"
        ),
        Product(
            id = 104,
            name = "Vortex Pure-Air Humidifier",
            category = "Home & Kitchen",
            price = 9999.00,
            originalPrice = 12999.00,
            rating = 4.7f,
            reviewCount = 64,
            description = "An exquisite addition to any luxury living space. Combining premium glassmorphic chassis design with acoustic levitation elements, the Vortex purifier forces surrounding air through an intense silver-ion scrubber and displays reverse gravity levitating water droplets.",
            specs = listOf(
                ProductSpec("Purification", "H14 True HEPA with Silver-Ion Matrix"),
                ProductSpec("Water Element", "Acoustic Anti-Gravity Droplet Levitor"),
                ProductSpec("Power", "USB-C Modern High-Speed Delivery"),
                ProductSpec("Aroma Infusion", "Direct Cold-Press Diffusion Vent")
            ),
            colorHexes = listOf("#090A0C", "#D2D7DF"),
            sizes = listOf("Compact 1L", "Luxe 3L"),
            imageKey = "humidifier"
        ),
        Product(
            id = 105,
            name = "Aether Premium Luxe Perfume",
            category = "Beauty",
            price = 14500.00,
            originalPrice = 18000.00,
            rating = 4.9f,
            reviewCount = 95,
            description = "A majestic, enigmatic fragrance suspended in a faceted obsidian crystal receptacle. Formulated with top-tier natural resins, aged Himalayan Cedar oil, organic musk amber, and finished with shimmering flakes of pure Indian silver.",
            specs = listOf(
                ProductSpec("Scent Family", "Amber Woody Spicy Oriental"),
                ProductSpec("Notes", "Saffron, Himalaya Cedar, Musk, Pure Silver"),
                ProductSpec("Concentration", "Extrait de Parfum (35% Oils)"),
                ProductSpec("Casket Designer", "Milan Luxury Crystal Lab")
            ),
            colorHexes = listOf("#090A0C", "#D2D7DF"),
            sizes = listOf("50ml", "100ml"),
            imageKey = "perfume"
        ),
        Product(
            id = 106,
            name = "Chronos Titanium Mechanical skeleton",
            category = "Accessories",
            price = 29999.00,
            originalPrice = 39999.00,
            rating = 5.0f,
            reviewCount = 41,
            description = "For the pure horology connoisseur. Chronos is an automatic, self-winding mechanical wearable carved from space-grade brushed titanium. It features a fully transparent dual-sapphire crystal cage that reveals the orbiting 3D tourbillon balance wheel.",
            specs = listOf(
                ProductSpec("Movement", "Co-Axial Swiss Tourbillon Skeleton"),
                ProductSpec("Case Alloy", "Grade 5 Bead-Blasted Brushed Titanium"),
                ProductSpec("Crystal", "Curved Front-to-Back Double Sapphire"),
                ProductSpec("Power Reserve", "68 Hours Kinetic Winding")
            ),
            colorHexes = listOf("#D2D7DF", "#090A0C"),
            sizes = listOf("42mm Case"),
            imageKey = "mechanical_watch"
        ),
        Product(
            id = 107,
            name = "Phantom Quad-Core AI Drone",
            category = "Gadgets",
            price = 45999.00,
            originalPrice = 52999.00,
            rating = 4.8f,
            reviewCount = 57,
            description = "The ultimate eyes in the sky. Featuring a quad-rotor carbon composite frame, the Phantom Drone executes seamless stabilization in wind speeds up to 45 km/h. Powered by a neural processor for real-time 3D object mapping and autonomous cinematic flight path execution.",
            specs = listOf(
                ProductSpec("Camera", "1/1.2'' CMOS 48MP HDR 4K/60fps"),
                ProductSpec("AI Engine", "Edge-Processor 12-Core Spatial Neural Map"),
                ProductSpec("Range", "12km Absolute OcuSync Premium"),
                ProductSpec("Flight Duration", "46 Minutes Smart-Cell Discharge")
            ),
            colorHexes = listOf("#1E293B", "#1E56FC"),
            sizes = listOf("Standard Fly", "Fly More Creator Bundle"),
            imageKey = "drone"
        ),
        Product(
            id = 108,
            name = "Chroma Lumina Smart Coffee Table",
            category = "Lifestyle",
            price = 54999.00,
            originalPrice = 69999.00,
            rating = 4.9f,
            reviewCount = 34,
            description = "Redefine your living lounge. Crafted using solid Indian natural Walnut framing, the table features a dynamic glassmorphic interactive display underneath. Place any smartphone on the table to initiate ultra-high speed wireless magnetic charging, or customize ambient layouts dynamically.",
            specs = listOf(
                ProductSpec("Table Wood", "Premium Solid Himalayan Walnut"),
                ProductSpec("Charging", "Four Multi-Point 25W QI inductive surfaces"),
                ProductSpec("Display Surface", "Integrated Transparent OLED Ambient Glass"),
                ProductSpec("Sound System", "Built-in 360° Harman-Kardon acoustic pods")
            ),
            colorHexes = listOf("#78350F", "#090A0C"),
            sizes = listOf("Classic Square", "Extended Panoramic"),
            imageKey = "table"
        )
    )
}

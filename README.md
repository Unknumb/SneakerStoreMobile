# 👟 SneakerStoreMobile

Una aplicación móvil moderna para Android que permite a los usuarios explorar, visualizar y comprar zapatillas deportivas de marcas premium. Desarrollada con las últimas tecnologías de Android y Jetpack Compose.

## 📱 Descripción

SneakerStoreMobile es una tienda de zapatillas deportivas diseñada para ofrecer una experiencia de compra fluida y atractiva. La aplicación permite a los usuarios navegar por un catálogo de productos, ver detalles específicos de cada zapatilla, añadir productos al carrito y simular el proceso de compra.

## ✨ Características

- **Catálogo de Productos**: Visualización en cuadrícula de zapatillas con imágenes, nombres y precios
- **Detalles del Producto**: Vista detallada con descripción completa y opciones de compra
- **Carrito de Compras**: Gestión de productos seleccionados con contador de cantidades
- **Navegación Intuitiva**: Sistema de navegación con drawer lateral y barra superior
- **Diseño Responsivo**: Interfaz moderna y adaptable con Material Design 3
- **Carga de Imágenes**: Visualización eficiente de imágenes desde URLs remotas

## 🛠️ Tecnologías y Arquitectura

### Stack Tecnológico

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Gestión de Estado**: StateFlow y Compose State
- **Navegación**: Jetpack Navigation Compose
- **Carga de Imágenes**: Coil
- **Persistencia Local**: Room Database (configurado)
- **Preferencias**: DataStore
- **Compilación**: Gradle con Kotlin DSL

### Versiones

- **Compile SDK**: 36
- **Min SDK**: 31
- **Target SDK**: 36
- **Kotlin**: Compatible con Java 11
- **Compose**: 1.5.4
- **Material Design**: 3.3.0

### Arquitectura del Proyecto

```
app/
├── data/
│   └── local/
│       └── MockData.kt          # Datos simulados de productos
├── model/
│   └── Product.kt               # Modelo de datos de producto
├── navigation/
│   └── NavGraph.kt              # Configuración de navegación
├── ui/
│   ├── components/
│   │   ├── AppSneakerTopBar.kt  # Barra superior personalizada
│   │   └── AppWithSideDrawer.kt # Drawer lateral de navegación
│   ├── screens/
│   │   ├── HomeScreen.kt        # Pantalla principal con catálogo
│   │   ├── ProductDetailScreen.kt # Detalles del producto
│   │   └── CartScreen.kt        # Pantalla del carrito
│   └── theme/
│       ├── Color.kt             # Paleta de colores
│       ├── Theme.kt             # Configuración del tema
│       └── Type.kt              # Tipografía
├── viewmodel/
│   └── ProductViewModel.kt      # ViewModel para gestión de estado
└── MainActivity.kt              # Actividad principal
```

## 🚀 Instalación

### Prerrequisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK API 31 o superior
- Conexión a Internet para descargar dependencias

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Unknumb/SneakerStoreMobile.git
   cd SneakerStoreMobile
   ```

2. **Abrir el proyecto en Android Studio**
   - Abre Android Studio
   - Selecciona "Open an Existing Project"
   - Navega hasta la carpeta del proyecto y ábrela

3. **Sincronizar dependencias**
   - Android Studio sincronizará automáticamente las dependencias de Gradle
   - Si no lo hace, selecciona `File > Sync Project with Gradle Files`

4. **Compilar el proyecto**
   ```bash
   ./gradlew build
   ```

5. **Ejecutar la aplicación**
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en el botón "Run" (▶️) en Android Studio
   - O usa el comando: `./gradlew installDebug`

## 📂 Estructura del Proyecto

### Componentes Principales

#### ProductViewModel
Gestiona el estado de la aplicación incluyendo:
- Lista de productos disponibles
- Producto seleccionado actualmente
- Items en el carrito de compras
- Operaciones de agregar/eliminar del carrito

#### Screens (Pantallas)

1. **HomeScreen**: Muestra el catálogo de productos en una cuadrícula
2. **ProductDetailScreen**: Presenta información detallada de un producto específico
3. **CartScreen**: Visualiza los productos agregados al carrito y permite gestionar cantidades

#### Navigation
Sistema de navegación basado en rutas:
- `/home` - Pantalla principal
- `/detail/{productId}` - Detalles del producto
- `/cart` - Carrito de compras

## 📸 Capturas de Pantalla

_Las capturas de pantalla se agregarán próximamente_

## 💻 Uso

### Navegación Básica

1. **Ver Productos**: Al abrir la app, verás el catálogo de zapatillas
2. **Ver Detalles**: Toca cualquier producto para ver sus detalles
3. **Agregar al Carrito**: En la vista de detalles, presiona "Agregar al carrito"
4. **Acceder al Carrito**: Toca el ícono del carrito en la barra superior
5. **Gestionar Carrito**: Elimina productos con el ícono de papelera
6. **Finalizar Compra**: Presiona el botón "Comprar" para simular la compra

### Características del Drawer

- Navegación rápida entre secciones
- Acceso directo a categorías (próximamente)
- Configuración de perfil (próximamente)

## 🔧 Configuración

### Personalización de Productos

Los productos se encuentran en `MockData.kt`. Para agregar nuevos productos:

```kotlin
Product(
    id = 7,
    name = "Nombre del Producto",
    price = 99.99,
    imageUrl = "https://url-de-la-imagen.com/imagen.jpg",
    description = "Descripción del producto"
)
```

### Personalización del Tema

Modifica los colores en `ui/theme/Color.kt` y el tema en `ui/theme/Theme.kt`

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Haz un fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -m 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

### Guías de Estilo

- Sigue las convenciones de código Kotlin
- Usa nombres descriptivos para variables y funciones
- Comenta código complejo cuando sea necesario
- Mantén la consistencia con el código existente

## 📋 Roadmap

- [ ] Integración con API REST real
- [ ] Sistema de autenticación de usuarios
- [ ] Persistencia de datos con Room
- [ ] Sistema de favoritos
- [ ] Filtros y búsqueda avanzada
- [ ] Historial de compras
- [ ] Integración con pasarelas de pago
- [ ] Notificaciones push
- [ ] Modo oscuro/claro

## 🐛 Problemas Conocidos

Actualmente no hay problemas conocidos. Si encuentras alguno, por favor abre un issue en GitHub.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Autores

- **Unknumb** - [GitHub](https://github.com/Unknumb)

## 📞 Contacto

Si tienes preguntas o sugerencias sobre el proyecto, no dudes en abrir un issue en GitHub.

---

**Nota**: Esta es una aplicación de demostración con fines educativos. Las imágenes y productos mostrados son solo ejemplos. 

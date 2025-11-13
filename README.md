# 👟 SneakerStoreMobile

Una aplicación móvil moderna para Android que permite a los usuarios explorar, visualizar y comprar zapatillas deportivas de marcas premium. Desarrollada con las últimas tecnologías de Android y Jetpack Compose.

## 📱 Descripción

SneakerStoreMobile es una tienda de zapatillas deportivas diseñada para ofrecer una experiencia de compra fluida y atractiva. La aplicación permite a los usuarios navegar por un catálogo de productos, ver detalles específicos de cada zapatilla, añadir productos al carrito y simular el proceso de compra.

## ✨ Características

- **Autenticación de Usuarios**: Sistema completo de login y registro con persistencia en base de datos local
- **Gestión de Perfiles**: Los usuarios pueden crear cuentas, iniciar sesión o entrar como invitados
- **Catálogo de Productos**: Visualización en cuadrícula de zapatillas con imágenes, nombres y precios en CLP
- **Detalles del Producto**: Vista detallada con descripción completa, imágenes y opciones de compra
- **Sistema de Favoritos**: Marca y guarda tus productos favoritos para acceso rápido
- **Carrito de Compras**: Gestión de productos seleccionados con imágenes, contador de cantidades y total en pesos chilenos
- **Historial de Compras**: Registro de todas las compras realizadas por el usuario
- **Navegación Intuitiva**: Sistema de navegación con drawer lateral y barra superior
- **Diseño Responsivo**: Interfaz moderna y adaptable con Material Design 3
- **Carga de Imágenes**: Visualización eficiente de imágenes desde URLs remotas con Coil

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
│   ├── local/
│   │   ├── AppDatabase.kt       # Base de datos Room
│   │   ├── UserDao.kt           # DAO para operaciones de usuarios
│   │   └── MockData.kt          # Datos simulados de productos
│   └── repository/
│       └── UserRepository.kt    # Repositorio para gestión de usuarios
├── model/
│   ├── Product.kt               # Modelo de datos de producto
│   └── User.kt                  # Modelo de datos de usuario
├── navigation/
│   └── NavGraph.kt              # Configuración de navegación
├── ui/
│   ├── components/
│   │   ├── AppSneakerTopBar.kt  # Barra superior personalizada
│   │   └── AppWithSideDrawer.kt # Drawer lateral de navegación
│   ├── screens/
│   │   ├── LoginScreen.kt       # Pantalla de inicio de sesión
│   │   ├── RegisterScreen.kt    # Pantalla de registro de usuarios
│   │   ├── HomeScreen.kt        # Pantalla principal con catálogo
│   │   ├── ProductDetailScreen.kt # Detalles del producto
│   │   ├── CartScreen.kt        # Pantalla del carrito
│   │   ├── FavoritesScreen.kt   # Pantalla de productos favoritos
│   │   └── ProfileScreen.kt     # Pantalla de perfil de usuario
│   └── theme/
│       ├── Color.kt             # Paleta de colores
│       ├── Theme.kt             # Configuración del tema
│       └── Type.kt              # Tipografía
├── viewmodel/
│   ├── ProductViewModel.kt      # ViewModel para gestión de productos
│   └── UserViewModel.kt         # ViewModel para gestión de usuarios
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

#### ViewModels

**ProductViewModel**: Gestiona el estado de los productos incluyendo:
- Lista de productos disponibles
- Producto seleccionado actualmente
- Items en el carrito de compras con cantidades
- Operaciones de agregar/eliminar del carrito
- Cálculo del total de compra

**UserViewModel**: Gestiona el estado del usuario incluyendo:
- Autenticación (login/registro)
- Información del usuario actual
- Lista de favoritos
- Historial de compras
- Operaciones de logout

#### Screens (Pantallas)

1. **LoginScreen**: Pantalla de inicio de sesión con opción de entrar como invitado
2. **RegisterScreen**: Formulario de registro de nuevos usuarios
3. **HomeScreen**: Pantalla principal con catálogo de productos en cuadrícula
4. **ProductDetailScreen**: Vista detallada de un producto con opción de agregar a favoritos y carrito
5. **CartScreen**: Visualiza los productos con imágenes, permite gestionar cantidades y finalizar compra
6. **FavoritesScreen**: Muestra los productos marcados como favoritos
7. **ProfileScreen**: Información del perfil del usuario y configuración

#### Navigation
Sistema de navegación basado en rutas:
- `/login` - Pantalla de inicio de sesión (pantalla inicial)
- `/register` - Registro de nuevos usuarios
- `/home` - Pantalla principal del catálogo
- `/detail/{productId}` - Detalles del producto
- `/cart` - Carrito de compras
- `/favorites` - Productos favoritos
- `/profile` - Perfil del usuario

## 📸 Capturas de Pantalla

_Las capturas de pantalla se agregarán próximamente_

## 💻 Uso

### Flujo de Usuario

#### Primer Uso
1. **Iniciar la App**: Al abrir la aplicación por primera vez, verás la pantalla de login
2. **Crear Cuenta**: Presiona "CREAR CUENTA" para registrar un nuevo usuario
3. **Registro**: Completa el formulario con usuario y contraseña
4. **Iniciar Sesión**: Ingresa con tus credenciales o usa la opción "ENTRAR COMO INVITADO"

#### Navegación Principal

1. **Ver Catálogo**: Una vez en la app, verás el catálogo de zapatillas en la pantalla principal
2. **Ver Detalles**: Toca cualquier producto para ver información detallada, descripción y precio en CLP
3. **Agregar a Favoritos**: En la vista de detalles, presiona el ícono de corazón para marcar como favorito
4. **Agregar al Carrito**: Presiona "Agregar al carrito" en la vista de detalles
5. **Ver Carrito**: Toca el ícono del carrito en la barra superior para ver tus productos
6. **Gestionar Carrito**: 
   - Visualiza imágenes y detalles de cada producto
   - Ajusta cantidades con los botones + y -
   - Elimina productos con el ícono de papelera
   - Ve el total calculado en pesos chilenos
7. **Finalizar Compra**: Presiona el botón "Comprar" para completar la compra

#### Otras Funcionalidades

- **Favoritos**: Accede al menú lateral para ver todos tus productos favoritos
- **Perfil**: Visualiza tu información de usuario y el historial de compras
- **Cerrar Sesión**: Desde el perfil puedes cerrar sesión y regresar al login

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

### Implementado ✅
- [x] Sistema de autenticación de usuarios (Login/Register)
- [x] Persistencia de datos con Room Database
- [x] Sistema de favoritos
- [x] Historial de compras
- [x] Carrito con imágenes y gestión de cantidades
- [x] Formato de moneda en pesos chilenos (CLP)

### Por Implementar
- [ ] Integración con API REST real
- [ ] Filtros y búsqueda avanzada
- [ ] Categorías de productos
- [ ] Integración con pasarelas de pago
- [ ] Notificaciones push
- [ ] Modo oscuro/claro
- [ ] Sincronización de favoritos y compras con backend
- [ ] Recuperación de contraseña

## 🐛 Problemas Conocidos

Actualmente no hay problemas conocidos. Si encuentras alguno, por favor abre un issue en GitHub.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Autores

- **Alvaro Uribe**
- **Juan Toledo**
- **Nicolas Hölck**

## 📞 Contacto

Si tienes preguntas o sugerencias sobre el proyecto, no dudes en abrir un issue en GitHub.

---

**Nota**: Esta es una aplicación de demostración con fines educativos. Las imágenes y productos mostrados son solo ejemplos. 

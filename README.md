# Sneaker Store Mobile

Aplicación móvil nativa Android desarrollada en Kotlin y Jetpack Compose para la venta de zapatillas. Este proyecto consume microservicios propios y APIs externas para brindar información actualizada de precios y catálogo.

## Integrantes
* Alvaro Uribe
* Nicolas Holck
* Juan Toledo

## Funcionalidades

La aplicación cuenta con las siguientes características principales:

* **Catálogo de Productos:** Visualización de lista de zapatillas obtenidas desde el servidor remoto.
* **Buscador:** Filtrado de productos por nombre en tiempo real.
* **Detalle de Producto:** Información extendida, selección de tallas y visualización de stock.
* **Conversión de Moneda:** Visualización del precio referencial en Dólares (USD) utilizando el valor del día en tiempo real.
* **Gestión de Carrito:** Agregar productos, modificar cantidades y eliminar ítems.
* **Favoritos:** Sistema para marcar y desmarcar productos favoritos (requiere inicio de sesión).
* **Autenticación:** Login y Registro de usuarios.
* **Checkout:** Simulación de proceso de compra y confirmación de pedido.

## Endpoints Utilizados

La arquitectura de la aplicación consume dos fuentes de datos distintas:

### 1. Microservicio Propio (Backend Sneaker Store)
Encargado de la gestión del inventario y datos de los productos.
* **Base URL:** `https://backend-sneakerstore-1.onrender.com/`
* **Endpoints:**
    * `GET /api/sneakers`: Obtiene el listado completo de zapatillas.

### 2. API Externa (Indicadores Económicos)
Utilizada para obtener el valor del dólar y realizar la conversión de precios en la vista de detalle.
* **API:** Mindicador.cl
* **Base URL:** `https://mindicador.cl/`
* **Endpoints:**
    * `GET /api`: Obtiene los valores actualizados de los indicadores económicos (UF, Dólar, Euro, etc.).

## Pasos para Ejecutar el Proyecto

### Requisitos Previos
* Android Studio Koala o superior.
* JDK 11 o superior.
* Dispositivo Android físico o Emulador con API 31 (Android 12) o superior.

### Instrucciones

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/unknumb/sneakerstoremobile.git](https://github.com/unknumb/sneakerstoremobile.git)
    ```

2.  **Abrir en Android Studio:**
    * Inicia Android Studio y selecciona "Open".
    * Navega hasta la carpeta clonada y selecciona el archivo `build.gradle.kts` o la carpeta raíz del proyecto.

3.  **Sincronizar Gradle:**
    * Espera a que Android Studio descargue las dependencias necesarias. Si aparece una barra amarilla arriba, haz clic en "Sync Now".

4.  **Ejecutar la App:**
    * Selecciona tu dispositivo o emulador en la barra superior.
    * Presiona el botón **Run** (Triángulo verde) o `Shift + F10`.

### Instalación Directa (APK)
Si deseas probar la aplicación sin compilar el código, puedes instalar el archivo APK generado:
1.  Copia el archivo `app/release/app-release.apk` a tu dispositivo móvil.
2.  Habilita la instalación de fuentes desconocidas en tu dispositivo.
3.  Ejecuta el archivo APK para instalar.
## 📞 Contacto

Si tienes preguntas o sugerencias sobre el proyecto, no dudes en abrir un issue en GitHub.

---

**Nota**: Esta es una aplicación de demostración con fines educativos. Las imágenes y productos mostrados son solo ejemplos. 

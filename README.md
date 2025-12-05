# AhorrApp - Aplicación de Finanzas Personales

AhorrApp es una aplicación para Android diseñada para ayudar a los usuarios a llevar un control detallado de sus finanzas, permitiendo registrar y gestionar tanto gastos y deudas personales como compartidos.

## Características Principales

*   **Autenticación de Usuarios:** Sistema seguro de registro e inicio de sesión con correo electrónico y contraseña.
*   
*   **Gestión de Gastos:**
    *   Registro de gastos individuales con detalles como nombre, valor y prioridad.
    *   Registro de **gastos compartidos** entre múltiples usuarios de la aplicación.
*   **Gestión de Deudas:**
    *   Registro de deudas personales con campos detallados como valor, tasa de interés, cuotas y fechas de vencimiento.
    *   Registro de **deudas compartidas**, permitiendo especificar los participantes y el porcentaje de responsabilidad de cada uno.
*   **Visualización de Datos:** Listas claras y organizadas para consultar todos los gastos y deudas registrados.

*   **Interfaz Intuitiva:** Diseño limpio y moderno basado en Material Design, con una navegación fluida entre las diferentes secciones.

*   **Banners Informativos:** Sección de consejos y buenas prácticas para la gestión financiera.


## Arquitectura y Tecnologías Utilizadas

El proyecto sigue prácticas modernas de desarrollo de Android, con un enfoque en la eficiencia, escalabilidad y mantenibilidad.

*   **Lenguaje:** **Java**
*   **Arquitectura:**
    *   **Single-Activity Architecture (SAA):** La aplicación utiliza una actividad principal (`MainActivity`) que gestiona múltiples fragmentos (`Fragment`), optimizando la navegación y el rendimiento.
    *   **Android Jetpack:** Se hace un uso extensivo de los componentes de Jetpack:
        *   **Navigation Component:** Para gestionar toda la lógica de navegación entre fragmentos de forma visual y centralizada.
        *   **ViewBinding:** Para interactuar con las vistas de forma segura y eficiente, eliminando la necesidad de `findViewById`.
*   **Backend (Serverless):**
    *   **Google Firebase:** Toda la infraestructura del backend se basa en los servicios de Firebase.
        *   **Firebase Authentication:** Para la gestión completa de la identidad de los usuarios.
        *   **Firebase Realtime Database:** Como base de datos NoSQL en tiempo real para almacenar toda la información de la aplicación (usuarios, gastos, deudas, etc.).
*   **Interfaz de Usuario (UI):**
    *   **Material Design Components:** Para construir una interfaz de usuario moderna y consistente.
    *   **RecyclerView:** Para mostrar listas largas de datos de manera eficiente.
    *   **CardView:** Para presentar la información en tarjetas visualmente atractivas.
    *   **ViewPager2:** Para la navegación por pestañas en la pantalla de autenticación.

## Cómo Empezar

Para clonar y ejecutar este proyecto en tu propio entorno, sigue estos pasos:

1.  **Clonar el Repositorio**
    ```sh
    git clone https://github.com/tu-usuario/AhorrApp.git
    ```

2.  **Abrir en Android Studio**
    *   Abre Android Studio y selecciona `Open an Existing Project`.
    *   Navega hasta la carpeta donde clonaste el repositorio y selecciónala.

3.  **Configurar Firebase**
    *   Este proyecto requiere una configuración de Firebase para funcionar.
    *   Ve a la [Consola de Firebase](https://console.firebase.google.com/).
    *   Crea un nuevo proyecto.
    *   Dentro de tu nuevo proyecto, añade una nueva aplicación de Android. Asegúrate de que el **nombre del paquete** coincida con el de este proyecto (`com.example.ahorrapp`).
    *   Sigue los pasos para registrar la aplicación y **descarga el archivo `google-services.json`**.
    *   Copia el archivo `google-services.json` que acabas de descargar y pégalo en la carpeta **`app/`** de tu proyecto en Android Studio.
    *   En la consola de Firebase, ve a la sección de **Authentication**, haz clic en "Get Started" y habilita el proveedor de **Email/Password**.
    *   Ve a la sección de **Realtime Database**, haz clic en "Create Database" y créala en modo de prueba (o configura las reglas de seguridad si lo prefieres).

4.  **Sincronizar y Ejecutar**
    *   Android Studio debería detectar el nuevo archivo y sincronizar el proyecto. Si no lo hace, haz clic en `File > Sync Project with Gradle Files`.
    *   Construye y ejecuta la aplicación en un emulador o dispositivo físico.

## Estructura del Proyecto

El código fuente está organizado de la siguiente manera para facilitar su comprensión:

*   **`activities`**: Contiene las clases de las actividades principales (`AuthActivity`, `MainActivity`, `RecordSharedExpenseActivity`, etc.).
*   **`fragments`**: Contiene todos los fragmentos que representan las diferentes pantallas de la aplicación (`LoginFragment`, `ConsultDebtsFragment`, `BannerDebtsFragment`, etc.).
*   **`adapters`**: Contiene los adaptadores para los `RecyclerView` y `ViewPager` (`DebtAdapter`, `ParticipantAdapter`, `ViewPagerAdapter`). Son los responsables de vincular los datos con las vistas.
*   **`lib` o `model`**: Contiene las clases de modelo de datos (POJOs) que definen la estructura de los objetos que se guardan en Firebase (`User`, `debt`, `DebtShared`).
*   **`res/layout`**: Contiene todos los archivos de diseño XML para las actividades y fragmentos.
*   **`res/navigation`**: Contiene el gráfico de navegación (`navigation_graph.xml`) que define los flujos entre pantallas.

## Imagenes de la app
<img width="658" height="1280" alt="image" src="https://github.com/user-attachments/assets/73e88569-6a1b-4e20-99ff-afde21cce2f0" />
<img width="644" height="1280" alt="image" src="https://github.com/user-attachments/assets/281a8d5f-182f-41f2-bcd0-880f4ce97b5c" />
<img width="646" height="1280" alt="image" src="https://github.com/user-attachments/assets/ecc46451-4845-4f82-af13-888e2bf02eab" />
<img width="646" height="1280" alt="image" src="https://github.com/user-attachments/assets/64aaf9a0-0faf-4114-aa49-09ca7bb4a59b" />
<img width="649" height="1280" alt="image" src="https://github.com/user-attachments/assets/faaa873c-d255-43c2-9488-b6f16d90fb3e" />


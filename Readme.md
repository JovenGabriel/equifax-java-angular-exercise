# Prueba Técnica para la Empresa Equifax

Este proyecto es una prueba técnica diseñada para la empresa Equifax. La aplicación se ha desarrollado utilizando *
*Angular** y se encuentra preparada para ser ejecutada mediante **Docker Compose**.

## Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener los siguientes requisitos instalados en tu sistema:

- **Docker** (v20.10 o superior)
- **Docker Compose** (v2.0 o superior)

## Cómo Ejecutar la Aplicación

Sigue los siguientes pasos para iniciar la aplicación con Docker Compose:

1. **Clona el repositorio:**

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd <NOMBRE_DEL_DIRECTORIO>
   ```

2. **Construye y levanta los contenedores:**

   En la raíz del proyecto, ejecuta el siguiente comando:

   ```bash
   docker-compose up --build
   ```

   Este comando construirá las imágenes necesarias y levantará los contenedores para la aplicación.

3. **Accede a la aplicación:**

   Una vez que los contenedores estén en funcionamiento, accede a la aplicación desde el navegador usando la siguiente
   URL:

   ```
   http://localhost:4200
   ```

   *Nota:* La aplicación estará en el puerto `4200` de forma predeterminada. Si este puerto está ocupado, puedes
   cambiarlo modificando el archivo `docker-compose.yml`.

4. **Detener los contenedores:**

   Cuando desees detener la aplicación, presiona `Ctrl+C` en el terminal donde se está ejecutando Docker Compose o
   utiliza el siguiente comando:

   ```bash
   docker-compose down
   ```

## Estructura del Proyecto

- **Frontend:** La aplicación está desarrollada en Angular.
- **Docker Compose:** Configura los servicios necesarios y orquesta los contenedores para que todo funcione
  correctamente.

## Personalización del Proyecto

Si necesitas realizar modificaciones, puedes editar los archivos del código fuente, incluyendo el archivo
`docker-compose.yml`, y luego volver a construir los contenedores con:

```bash
docker-compose up --build
```
## Notas Adicionales

- Asegúrate de que ningún otro servicio esté utilizando los puertos requeridos por la aplicación.
- Si encuentras problemas, verifica los logs de los contenedores ejecutando:

  ```bash
  docker-compose logs
  ```

## Cómo Ejecutar la Aplicación de Forma Manual (Frontend y Backend por Separado)

Si prefieres o necesitas ejecutar el frontend y el backend de forma independiente, sigue estos pasos:

### 1. **Configuración de la Base de Datos**

Antes de levantar los proyectos, es necesario crear una base de datos en PostgreSQL con el nombre `equifax_users`.
Asegúrate de que el servicio de PostgreSQL esté funcionando y realiza la creación de la base de datos ejecutando:

```bash
psql -U <USUARIO> -c "CREATE DATABASE equifax_users;"
```

Luego, configura los datos de conexión en el archivo de propiedades del backend (por ejemplo, `application.properties` o
`config.yml`, dependiendo de cómo esté implementado el backend). Indica el nombre de la base de datos (`equifax_users`),
el usuario, y la contraseña. Asegúrate de que estos datos estén correctamente configurados.

### Información Adicional: Creación Automática de Tablas y Usuario Administrador

El sistema está diseñado para crear automáticamente las tablas requeridas y el usuario administrador en la base de datos
al iniciar la aplicación por primera vez. Sin embargo, si ocurre algún problema, puedes usar el DDL (Definición de Datos
SQL) proporcionado a continuación para realizar esta configuración manualmente.

#### DDL para Creación de la Tabla `users`

```postgresql
create table users
(
    id         uuid         not null
        primary key,
    created_at timestamp(6),
    email      varchar(255) not null
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    name       varchar(255) not null,
    password   varchar(255) not null,
    rut        varchar(255) not null
        constraint ukscuj1snh0iy35s195t3qff5o
            unique,
    token      varchar(255),
    updated_at timestamp(6)
);
```

Este DDL creará la tabla `users` con las restricciones necesarias.

#### Inserción del Usuario Administrador

Utiliza el siguiente comando SQL para crear el usuario administrador en la base de datos en caso de que no se haya
creado automáticamente. Este usuario tiene las siguientes credenciales predeterminadas:

- **Email:** admin@email.com
- **Password:** Admin123 (almacenado en formato hash)

```postgresql
INSERT INTO users (id, created_at, email, name, password, rut, token, updated_at)
VALUES (
    gen_random_uuid(),  -- Genera un UUID (PostgreSQL)
    NOW(),              -- Fecha de creación
    'admin@email.com',  
    'Admin',  
    '$2a$10$XjUO6A0bVldXb8zJv5s8beROU69cnTYcXQzD0FUX5jhFMx1i8zUy2',  -- Hash de "Admin123"
    '7769864-7',  
    NULL,               -- Token (puede ser NULL)
    NOW()               -- Fecha de actualización
);
```

#### Notas Importantes:

- **Creación Automática:** Si utilizas Docker Compose o ejecutas el backend directamente con Spring Boot, las tablas y
  el usuario administrador deberían crearse automáticamente al iniciar la aplicación, siempre y cuando esté habilitada
  la funcionalidad de inicialización de esquemas (por ejemplo, mediante `spring.jpa.hibernate.ddl-auto=create` en el
  archivo `application.properties`).
- **Hash de Contraseña:** La contraseña `Admin123` está previamente encriptada con BCrypt en el campo `password`.
- Verifica que la base de datos `equifax_users` exista y que los datos de conexión estén correctamente configurados en
  el archivo `application.properties` o el archivo de configuración correspondiente para evitar problemas durante la
  inicialización.

Si tienes problemas adicionales, puedes consultar los logs del backend o verificar el estado de la base de datos.
### 2. **Levantando el Backend**

Desde el directorio del backend:

1. Accede a la carpeta donde se encuentra el código fuente del backend.

   ```bash
   cd backend/
   ```

2. Instala las dependencias y ejecuta el proyecto:

   ```bash
   ./mvnw spring-boot:run
   ```

   Verifica que el backend esté corriendo en el puerto configurado (por defecto, este suele ser `8080`).

### 3. **Levantando el Frontend**

Desde el directorio del frontend:

1. Accede a la carpeta donde está ubicado el código fuente del frontend Angular.

   ```bash
   cd frontend/
   ```

2. Instala las dependencias necesarias:

   ```bash
   npm install
   ```

3. Ejecuta el servidor de desarrollo de Angular:

   ```bash
   ng serve
   ```

   Por defecto, la aplicación estará disponible en: [http://localhost:4200](http://localhost:4200).

---

### Notas Adicionales:

- Verifica que la URL base del backend esté correctamente configurada en el archivo de entorno del frontend (
  `environment.ts` o `environment.prod.ts`), para asegurar que ambos servicios se comuniquen correctamente.

- Si ambos servicios están utilizando puertos por defecto (`8080` para el backend y `4200` para el frontend), asegúrate
  de que estos puertos no estén ocupados por otros servicios.

### Cómo Usar la Aplicación (Carga de Usuarios desde un Archivo Excel)

La aplicación permite cargar usuarios a través de un archivo Excel que debe cumplir con el siguiente formato:

1. **Formato del Archivo Excel:**
   - El archivo debe tener **tres columnas** con los siguientes encabezados:
      - `name` (Nombre): Indica el nombre del usuario.
      - `rut` (RUT): Debe ser un RUT válido.
      - `email` (Correo Electrónico): Debe ser una dirección de correo válida.

2. **Pasos para Realizar la Carga:**

   1. **Iniciar Sesión:**
      - Antes de cargar el archivo, inicia sesión en la aplicación como administrador.
      - Usa las siguientes credenciales para ingresar:
         - **Email:** admin@email.com
         - **Password:** Admin123

   2. **Preparar el Archivo Excel:**
      - Crea o edita un archivo Excel con las columnas mencionadas (`name`, `rut`, `email`).
      - Asegúrate de que las filas tengan datos válidos. Las celdas vacías o datos inválidos se ignorarán durante la
        carga.
        
**Nota:** En la carpeta raíz del proyecto se encuentra un archivo de prueba llamado `test_equifax.xlsx` que puedes
utilizar para probar la funcionalidad de carga de usuarios. Este archivo contiene datos de ejemplo que cumplen con el
formato requerido (columnas `name`, `rut`, y `email`). Asegúrate de revisar y utilizar este archivo para validar la
carga en la aplicación.
   3. **Subir el Archivo:**
      - En la aplicación, haz clic en el botón **"Cargar Archivo"** (Load).
      - Selecciona tu archivo Excel y súbelo al sistema.

   4. **Validación Automática:**
      - La aplicación procesará el archivo, cargando **solo las filas que cumplan con los siguientes criterios**:
         - El campo `name` no debe estar vacío.
         - El campo `rut` debe contener un valor válido y correctamente formado, sin puntos y con guion.
         - El campo `email` debe ser un correo electrónico en formato válido.

   5. **Revisión de Resultados:**
      - Una vez procesado el archivo, se notificará sobre el éxito o posibles errores.
      **- Revisa los resultados en la interfaz después de cargar.
**Nota:** Si el archivo contiene errores o datos en formato incorrecto, verifica que cumpla con los requisitos antes de intentar nuevamente.

### Visualización de Endpoints
La lista completa de endpoints disponibles en el backend se puede consultar a través de Swagger.
Accede a la documentación interactiva de la API utilizando la siguiente URL:

```
http://localhost:8080/swagger-ui/index.html
```

Esto te permitirá explorar y probar los endpoints directamente desde la interfaz proporcionada por Swagger.

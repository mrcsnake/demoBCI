# Evaluación: API RESTful de Registro de Usuarios

Este proyecto consiste en una API RESTful desarrollada con Spring Boot y Java 17, diseñada para el registro de usuarios. Utiliza una base de datos en memoria (H2), JPA (Hibernate), y ofrece endpoints que aceptan y retornan JSON.

## Funcionalidades

* **Registro de Usuarios:** Permite registrar nuevos usuarios con información personal y números de teléfono.
* **Validación de Datos:** Valida el formato del correo electrónico y la contraseña mediante expresiones regulares configurables.
* **Manejo de Errores:** Retorna mensajes de error en formato JSON para cualquier problema durante el registro.
* **Generación de Tokens:** Genera tokens de acceso (JWT) para cada usuario registrado.
* **Persistencia de Datos:** Almacena la información de los usuarios y sus tokens en una base de datos en memoria.
* **Documentación:** La API está documentada con Swagger para facilitar su uso.

## Requisitos

* Java 17
* Maven
* Spring Boot
* H2 Database
* Hibernate (JPA)
* JWT (JSON Web Tokens)
* Swagger

## Configuración

1.  **Clonar el repositorio:**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    ```

2.  **Construir el proyecto:**

    ```bash
    mvn clean install
    ```

3.  **Ejecutar la aplicación:**

    ```bash
    mvn spring-boot:run
    ```

## Endpoints

* **Registro de Usuario (POST /api/usuarios):**
    * Acepta un JSON con los campos: `nombre`, `correo`, `contraseña`, y una lista de `teléfonos`.
    * Retorna un JSON con la información del usuario registrado, incluyendo `id`, `created`, `modified`, `last_login`, `token`, y `isactive`.
    * En caso de error, retorna un JSON con el mensaje de error correspondiente.

## Formato de Solicitud de Registro

```json
{
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "password": "hunter2",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
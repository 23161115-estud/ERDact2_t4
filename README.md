# ERDact1_t4

API REST creada con **Java y Spring Boot** para la Actividad 1 del Tema 4. Tiene dos controladores con cinco endpoints que responden texto, números, objetos y listas en JSON. Se probó primero en local y después se desplegó en un VPS de Vultr, corriendo en un puerto propio.

**Autor:** Ruiz Durán Emiliano

## Qué hace el proyecto

La aplicación levanta un servidor web embebido (Tomcat) y atiende peticiones `GET`. Cada endpoint demuestra una forma distinta de responder:

| Controlador | Endpoint | Qué devuelve | Tipo de respuesta |
|---|---|---|---|
| `ProyectoController` | `GET /api/proyecto` | Nombre del proyecto, autor y versión | Objeto JSON |
| `ProyectoController` | `GET /api/saludo/{nombre}` | Un saludo personalizado | Texto |
| `PerfilController` | `GET /api/perfil` | Datos del autor | Objeto JSON |
| `PerfilController` | `GET /api/materias` | Un listado de materias | Lista JSON |
| `PerfilController` | `GET /api/suma?a=2&b=3` | La suma de dos números | Número |

### Ejemplos de respuesta

`GET /api/proyecto`
```json
{"proyecto":"ERDact1_t4","autor":"Ruiz Durán Emiliano","version":"1.0"}
```

`GET /api/saludo/Ana`
```
Hola, Ana. Bienvenido a RDEact1_t4.
```

`GET /api/perfil`
```json
{"nombre":"Ruiz Durán Emiliano","carrera":"Ingeniería en Sistemas Computacionales","semestre":7,"activo":true}
```

`GET /api/materias`
```json
[{"nombre":"Programación Web","creditos":5},{"nombre":"Programación Lógica","creditos":4},{"nombre":"Conmutación de redes","creditos":5},{"nombre":"Taller de investigación","creditos":4}]
```

`GET /api/suma?a=2&b=3`
```
5
```

## Tecnologías y entorno

| Herramienta | Versión |
|---|---|
| Java (JDK Eclipse Temurin) | 21 |
| Spring Boot | 4.1.2 (con la dependencia Spring Web) |
| Servidor embebido | Apache Tomcat 11 |
| Gestor de construcción | Maven (con Maven Wrapper `mvnw`) |
| IDE | [IntelliJ IDEA / VS Code] |
| Servidor | VPS de Vultr con Ubuntu |

## Estructura del proyecto

```
src/main
├── java/com/example/demo
│   ├── DemoApplication.java
│   ├── controller
│   │   ├── ProyectoController.java
│   │   └── PerfilController.java
│   └── model
│       ├── ProyectoInfo.java
│       ├── Perfil.java
│       └── Materia.java
└── resources
    └── application.properties
```

- **`controller`:** clases que reciben las peticiones y definen cada ruta.
- **`model`:** clases simples (`record`) que representan los datos que se devuelven como JSON.
- **`application.properties`:** el puerto del servidor y las propiedades propias del proyecto.

## Cómo se realizó

### 1. Preparar el entorno

1. Se instaló el **JDK 21** con `winget install EclipseAdoptium.Temurin.21.JDK`.
2. Se comprobó con `java -version` y `javac -version`. La computadora tenía antes Java 8, que Spring Boot no admite, así que se ajustó `JAVA_HOME` y el `Path` para que Windows usara el 21.
3. Se instaló un IDE para abrir y ejecutar el proyecto.

### 2. Crear el proyecto base

Se generó en **start.spring.io** con estos datos:

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Group / Artifact | `com.example` / `demo` |
| Packaging | Jar |
| Java | 21 |
| Dependencies | Spring Web |

Se descomprimió, se abrió en el IDE y se ejecutó con `.\mvnw.cmd spring-boot:run`. En la consola apareció `Tomcat started on port 8080` y `Started DemoApplication`, y en el navegador salió la página "Whitelabel Error Page" (404). Eso confirmó que el servidor funcionaba y que todavía no había rutas.

### 3. Crear los controladores

1. Se crearon los modelos `ProyectoInfo`, `Perfil` y `Materia` como `record`, que Spring convierte automáticamente a JSON.
2. Se creó `ProyectoController` con `@RestController` y el prefijo `@RequestMapping("/api")`. Sus métodos usan `@GetMapping` para `/proyecto` y `/saludo/{nombre}` (con `@PathVariable`).
3. Se creó `PerfilController` con `/perfil`, `/materias` y `/suma` (con `@RequestParam` para recibir `a` y `b`).
4. En `application.properties` se definieron `app.nombre-proyecto` y `app.autor`, y `ProyectoController` los lee con `@Value`.

### 4. Probar en local

Con el proyecto corriendo en `http://localhost:8080`, se abrieron los cinco endpoints en el navegador y se comprobó que cada uno respondía lo esperado.

### 5. Empaquetar

```bash
.\mvnw.cmd clean package
```

Terminó con `BUILD SUCCESS` y generó `target/demo-0.0.1-SNAPSHOT.jar`. Se probó solo, sin el IDE:

```bash
java -jar target\demo-0.0.1-SNAPSHOT.jar --server.port=8085
```

### 6. Desplegar en el VPS de Vultr

1. Conexión al servidor: `ssh root@IP_DEL_VPS`.
2. Se verificó Java con `java -version` (se instala con `apt install openjdk-21-jre-headless` si falta).
3. Se acordó el puerto con mi compañero: el mío es **8082** y el suyo es 8081. Se evitó el 8080, y el 80 y el 443 se dejaron para el servidor web.
4. Se abrió el puerto en el firewall: `ufw allow 8082/tcp`.
5. Se subió el jar a una carpeta propia para no mezclarlo con el de mi compañero:
```bash
ssh root@IP_DEL_VPS "mkdir -p ~/TUSINICIALESact1_t4"
scp target\demo-0.0.1-SNAPSHOT.jar root@IP_DEL_VPS:~/TUSINICIALESact1_t4/
```
6. Se ejecutó con `nohup` para que siga corriendo al cerrar la sesión:
```bash
cd ~/TUSINICIALESact1_t4
nohup java -jar demo-0.0.1-SNAPSHOT.jar --server.port=[PUERTO] > app.log 2>&1 &
```
7. Se verificó el log (`tail app.log`), con `curl http://localhost:8082/api/proyecto`, y desde el navegador de mi laptop.

## Conceptos aplicados

| Anotación | Para qué se usa en el proyecto |
|---|---|
| `@SpringBootApplication` | Arranca la aplicación y activa la configuración automática |
| `@RestController` | Marca la clase como controlador que responde datos (JSON o texto) |
| `@RequestMapping("/api")` | Define el prefijo común de las rutas de una clase |
| `@GetMapping` | Asocia una petición GET con un método y una ruta |
| `@PathVariable` | Lee un valor de la ruta (`/saludo/{nombre}`) |
| `@RequestParam` | Lee un parámetro de la URL (`?a=2&b=3`) |
| `@Value` | Lee una propiedad desde `application.properties` |

## Cómo ejecutarlo

Requisitos: JDK 21 o superior.

```bash
.\mvnw.cmd spring-boot:run
```

En Linux o Mac: `./mvnw spring-boot:run`. Por defecto corre en el puerto 8080. Para usar otro puerto:

```bash
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8085"
```

## Problemas encontrados y cómo se resolvieron

| Problema | Causa | Solución |
|---|---|---|
| `java -version` seguía mostrando la versión 1.8 | El Java 8 anterior iba primero en el `Path` | Se ajustó `JAVA_HOME` y se puso `%JAVA_HOME%\bin` antes en el `Path` |
| `.\mvnw.cmd` no se reconocía | Se estaba en una carpeta sin `mvnw.cmd` | Se entró a la carpeta del proyecto generado por Spring Initializr |
| El servidor parecía "detenido" en la consola | Es normal: el servidor se queda corriendo | Se detuvo con Ctrl + C cuando ya no hacía falta |
| Puerto accesible solo en local | El firewall del VPS solo permitía 22, 80 y 443 | Se abrió el puerto propio con `ufw allow` |

## Enlaces

- Repositorio de esta actividad: [PEGAR LINK DE GITHUB]
- Proyecto corriendo en el VPS: http://216.238.69.86:8082/api/proyecto
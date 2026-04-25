# XploreNow Backend

API REST para el TPO de Desarrollo de Aplicaciones 1.

## Stack

- **Spring Boot 3.3.4** (Java 17)
- **PostgreSQL 16** en Docker
- **JPA / Hibernate** (Spring Data JPA)
- **JWT** con jjwt
- **Spring Security** (filtro JWT)

## Cómo levantar todo

### 1) Levantar la base de datos

```bash
cd xplorenow-backend
docker compose up -d
```

Esto levanta:

- **Postgres** en `localhost:5432` (DB: `xplorenow`, user: `xplorenow`, pass: `xplorenow123`)
- **pgAdmin** en `http://localhost:5050` (user: `admin@xplorenow.com`, pass: `admin`)

### 2) Levantar el backend

```bash
./mvnw spring-boot:run
```

(o desde IntelliJ: botón Play en `XploreNowApplication`)

La API queda en `http://localhost:8080`.

Al arrancar, JPA crea las tablas y carga `data.sql` con datos de prueba.

## Endpoints disponibles (Punto 3 del TPO — Catálogo)

### Login (stub)

```
POST /auth/login
Body: { "email": "test@test.com", "password": "loquesea" }
Response: { "token": "eyJhbGci..." }
```

> **Nota:** este login acepta cualquier credencial. Cuando esté listo el módulo de Usuarios (Punto 1), se reemplaza por validación real contra la BD.

### Listado paginado del catálogo

```
GET /actividades
GET /actividades?destinoId=1&categoriaId=3&precioMax=20000
GET /actividades?fecha=2026-05-15&page=0&size=20
```

Filtros (todos opcionales):
- `destinoId` — Long
- `categoriaId` — Long
- `fecha` — yyyy-MM-dd (la actividad debe estar disponible en esa fecha)
- `precioMin`, `precioMax` — BigDecimal
- `page`, `size` — paginación (default: 0, 10)

### Detalle de actividad

```
GET /actividades/{id}
```

### Destacadas / recomendadas

```
GET /actividades/destacadas?categorias=AVENTURA,CULTURA&size=10
```

Si no se mandan categorías, devuelve top N por cupos.

### Filtros disponibles

```
GET /destinos    -> lista de destinos para los selectores
GET /categorias  -> lista de categorías para los selectores
```

## Estructura del proyecto

```
src/main/java/com/xplorenow/
├── XploreNowApplication.java       Main
├── config/
│   ├── SecurityConfig.java         Qué endpoints requieren JWT
│   └── JwtAuthFilter.java          Lee Authorization: Bearer ...
├── auth/                           Login + JWT (stub)
├── actividad/                      ⭐ PUNTO 3
├── destino/
├── categoria/
└── foto/

src/main/resources/
├── application.properties          Config de DB y JWT
└── data.sql                        Seed de actividades de prueba
```

## Probar rápido con curl

```bash
# Listar todas las actividades
curl http://localhost:8080/actividades

# Filtrar por destino
curl "http://localhost:8080/actividades?destinoId=1"

# Login y guardar el token
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"a@b.c","password":"x"}' | jq -r .token)

# Pedir el detalle con el token
curl http://localhost:8080/actividades/1 \
  -H "Authorization: Bearer $TOKEN"
```

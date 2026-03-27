# Northstar Shop

Proyecto portfolio de e-commerce full stack construido desde cero con Java, Spring Boot, PostgreSQL, JWT y frontend vanilla. La idea es mostrar una primera version profesional, limpia y deployable: sin intentar resolver todos los edge cases de un e-commerce real, pero con una base seria y presentable para recruiters.

## Stack

- Backend: Java 17, Spring Boot, Spring Security, Spring Data JPA, Maven
- Base de datos: PostgreSQL
- Auth: JWT
- Frontend: HTML, CSS y JavaScript
- Deploy: Render
- Cloud database: Neon

## Features

- Registro e inicio de sesion con JWT
- Roles `USER` y `ADMIN`
- Catalogo de productos y categorias
- Detalle de producto
- Carrito persistido por usuario
- Checkout simple con generacion de orden y descuento de stock
- Historial de ordenes del usuario
- Panel admin para crear, editar, eliminar y listar productos y categorias
- Validaciones de requests
- Manejo global de errores
- Estructura por capas
- Dockerfile para backend
- Variables de entorno

## Estructura

```text
.
├── backend
│   └── src/main/java/com/portfolio/ecommerce
│       ├── config
│       ├── controller
│       ├── dto
│       ├── entity
│       ├── exception
│       ├── repository
│       ├── security
│       └── service
├── frontend
│   ├── css
│   ├── js
│   ├── index.html
│   ├── login.html
│   ├── product.html
│   ├── cart.html
│   ├── orders.html
│   └── admin.html
├── Dockerfile
├── render.yaml
└── .env.example
```

## Modelo de dominio

- `User` tiene roles, carrito y ordenes
- `Category` agrupa productos
- `Product` pertenece a una categoria y tiene stock
- `Cart` pertenece a un usuario
- `CartItem` conecta carrito con productos y cantidad
- `Order` pertenece a un usuario y guarda total, fecha y estado
- `OrderItem` guarda snapshot del producto comprado, precio y cantidad

## Reglas funcionales implementadas

- `USER` puede registrarse, iniciar sesion, listar productos, ver detalle, agregar al carrito, confirmar compra y ver sus ordenes
- `ADMIN` puede crear, editar, eliminar y listar productos y categorias
- El checkout no integra pagos reales: genera la orden, guarda los items comprados y descuenta stock

## Variables de entorno

Copiá `.env.example` como base y configurá:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `CORS_ALLOWED_ORIGINS`

## Backend local

```bash
cd backend
mvn spring-boot:run
```

La API queda en `http://localhost:8080/api`.

## Frontend local

Podés abrir `frontend/index.html` con Live Server o cualquier static server. Antes de deployar, ajustá `frontend/js/config.js` si tu backend tiene otra URL.

```js
window.APP_CONFIG = {
  API_BASE_URL: "http://localhost:8080/api"
};
```

## Usuario admin seed

Al levantar el backend por primera vez se crea:

- Email: `admin@demo.com`
- Password: `Admin123`

Tambien se seedéan categorias y productos de ejemplo para que el proyecto se vea completo desde el minuto uno.

## Endpoints principales

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`

### Publicos

- `GET /api/products`
- `GET /api/products/{id}`
- `GET /api/categories`

### Usuario autenticado

- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{itemId}`
- `DELETE /api/cart/items/{itemId}`
- `DELETE /api/cart/clear`
- `POST /api/orders/checkout`
- `GET /api/orders/me`

### Admin

- `GET /api/admin/products`
- `POST /api/admin/products`
- `PUT /api/admin/products/{id}`
- `DELETE /api/admin/products/{id}`
- `GET /api/admin/categories`
- `POST /api/admin/categories`
- `PUT /api/admin/categories/{id}`
- `DELETE /api/admin/categories/{id}`

## Deploy en Render + Neon

### Backend

1. Crear una base PostgreSQL en Neon.
2. Crear un Web Service en Render usando este repo.
3. Usar `Dockerfile` como build.
4. En `render.yaml` ya queda preconfigurado el host de Neon y el usuario.
5. Cargar en Render las variables sensibles reales:

- `DB_PASSWORD`
- `JWT_SECRET`
- `CORS_ALLOWED_ORIGINS`

6. Si cambiás de proyecto o branch de Neon, actualizá también `DB_URL`.

### Frontend

1. Crear un Static Site en Render apuntando a `frontend/`.
2. Cambiar `frontend/js/config.js` para que `API_BASE_URL` apunte a tu backend de Render.

Tambien podés usar `render.yaml` como punto de partida para los dos servicios.

## Validacion

El backend compila con:

```bash
cd backend
mvn -DskipTests compile
```

## Siguientes mejoras naturales

- Busqueda y filtros avanzados
- Paginacion
- Upload real de imagenes
- Dashboard admin con metricas
- Tests unitarios y de integracion
- Refresh tokens

## Nota

Todavia no puse credenciales reales de Neon. Cuando me las pases, el proyecto ya esta preparado para conectarlas y dejarlo listo para deploy final.

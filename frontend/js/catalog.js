async function loadCatalog() {
    try {
        const [categories, products] = await Promise.all([api("/categories"), api("/products")]);
        renderCategoryFilters(categories);
        renderProducts(products);
        document.getElementById("catalogCount").textContent = `${products.length} productos`;
    } catch (error) {
        showMessage("catalogMessage", error.message);
    }
}

function renderCategoryFilters(categories) {
    const container = document.getElementById("categoryFilters");
    container.innerHTML = `<button class="pill active" data-category="">Todo</button>` +
        categories.map(category => `<button class="pill" data-category="${category.id}">${category.name}</button>`).join("");

    container.querySelectorAll("[data-category]").forEach(button => {
        button.addEventListener("click", async () => {
            container.querySelectorAll(".pill").forEach(pill => pill.classList.remove("active"));
            button.classList.add("active");
            const categoryId = button.dataset.category;
            const products = await api(categoryId ? `/products?categoryId=${categoryId}` : "/products");
            renderProducts(products);
            document.getElementById("catalogCount").textContent = `${products.length} productos`;
        });
    });
}

function renderProducts(products) {
    const grid = document.getElementById("productGrid");
    if (!products.length) {
        grid.innerHTML = `<div class="empty-state">No encontramos productos en esta categoria.</div>`;
        return;
    }

    grid.innerHTML = products.map(product => `
        <article class="product-card">
            <img src="${product.imageUrl}" alt="${product.name}">
            <div class="product-copy">
                <span class="label">${product.category.name}</span>
                <h4>${product.name}</h4>
                <p class="meta">${product.description.slice(0, 120)}...</p>
                <div class="price">${currency(product.price)}</div>
                <div class="cta-row">
                    <a class="btn-outline" href="product.html?id=${product.id}">Ver detalle</a>
                    <button class="btn" data-add-cart="${product.id}">Agregar</button>
                </div>
            </div>
        </article>
    `).join("");

    grid.querySelectorAll("[data-add-cart]").forEach(button => {
        button.addEventListener("click", async () => {
            if (!getToken()) {
                window.location.href = "login.html";
                return;
            }
            try {
                await api("/cart/items", {
                    method: "POST",
                    body: JSON.stringify({ productId: Number(button.dataset.addCart), quantity: 1 })
                });
                showMessage("catalogMessage", "Producto agregado al carrito.", "success");
            } catch (error) {
                showMessage("catalogMessage", error.message);
            }
        });
    });
}

document.addEventListener("DOMContentLoaded", loadCatalog);

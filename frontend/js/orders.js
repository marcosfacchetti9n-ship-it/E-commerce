document.addEventListener("DOMContentLoaded", async () => {
    requireAuth();
    try {
        const orders = await api("/orders/me");
        const grid = document.getElementById("ordersGrid");
        grid.innerHTML = orders.length
            ? orders.map(order => `
                <article class="order-card">
                    <span class="label">Orden #${order.id}</span>
                    <h3>${currency(order.total)}</h3>
                    <p class="meta">${new Date(order.createdAt).toLocaleString("es-AR")}</p>
                    <p class="meta">Estado: ${order.status}</p>
                    <div class="stack">
                        ${order.items.map(item => `<div>${item.productName} x${item.quantity} <strong>${currency(item.subtotal)}</strong></div>`).join("")}
                    </div>
                </article>
            `).join("")
            : `<div class="empty-state">Todavia no tenes ordenes. Cuando confirmes una compra, aparecen aca.</div>`;
    } catch (error) {
        showMessage("ordersMessage", error.message);
    }
});

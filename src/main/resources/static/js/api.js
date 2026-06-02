const API = {
    async request(url, options = {}) {
        const headers = { 'Content-Type': 'application/json', ...options.headers };
        const token = Auth.getToken();
        if (token) headers['Authorization'] = 'Bearer ' + token;

        const response = await fetch(url, { ...options, headers });
        if (!response.ok) {
            let message = 'Ошибка запроса';
            try {
                const err = await response.json();
                message = err.detail || err.title || message;
                if (err.errors) message += ': ' + JSON.stringify(err.errors);
            } catch (_) { /* ignore */ }
            throw new Error(message);
        }
        if (response.status === 204) return null;
        return response.json();
    },

    getProducts(params = {}) {
        const q = new URLSearchParams({ size: 100, sort: 'name,asc', ...params });
        return this.request('/api/products?' + q);
    },

    getCategories() {
        return this.request('/api/categories');
    },

    register(data) {
        return this.request('/api/auth/register', { method: 'POST', body: JSON.stringify(data) });
    },

    login(data) {
        return this.request('/api/auth/login', { method: 'POST', body: JSON.stringify(data) });
    },

    getCart() {
        return this.request('/api/cart');
    },

    addToCart(productId) {
        return this.request('/api/cart/add/' + productId, { method: 'POST' });
    },

    updateCartItem(itemId, quantity) {
        return this.request('/api/cart/update/' + itemId, {
            method: 'PUT',
            body: JSON.stringify({ quantity })
        });
    },

    removeFromCart(itemId) {
        return this.request('/api/cart/remove/' + itemId, { method: 'DELETE' });
    },

    checkout(data) {
        return this.request('/api/orders/checkout', { method: 'POST', body: JSON.stringify(data) });
    },

    getMyOrders() {
        return this.request('/api/orders');
    },

    getAdminActiveOrders() {
        return this.request('/api/orders/admin/active');
    },

    getAdminOrderHistory() {
        return this.request('/api/orders/admin/history');
    },

    updateOrderStatus(id, status) {
        return this.request('/api/orders/admin/' + id + '/status', {
            method: 'PUT',
            body: JSON.stringify({ status })
        });
    },

    createProduct(data) {
        return this.request('/api/products', { method: 'POST', body: JSON.stringify(data) });
    },

    updateProduct(id, data) {
        return this.request('/api/products/' + id, { method: 'PUT', body: JSON.stringify(data) });
    },

    deleteProduct(id) {
        return this.request('/api/products/' + id, { method: 'DELETE' });
    }
};

function formatPrice(price) {
    return new Intl.NumberFormat('be-BY', { style: 'currency', currency: 'BYN' }).format(price);
}

function showToast(message, type = 'info') {
    let el = document.getElementById('toast');
    if (!el) {
        el = document.createElement('div');
        el.id = 'toast';
        el.className = 'fixed top-24 right-4 z-50 px-5 py-3 rounded-lg glass toast font-semibold';
        document.body.appendChild(el);
    }
    const colors = { success: 'text-emerald-400', error: 'text-rose-400', info: 'text-cyan-400' };
    el.className = 'fixed top-24 right-4 z-50 px-5 py-3 rounded-lg glass toast font-semibold ' + (colors[type] || colors.info);
    el.textContent = message;
    el.style.display = 'block';
    setTimeout(() => { el.style.display = 'none'; }, 3000);
}

function renderNav(activePage) {
    const user = Auth.getUser();
    const cartBadge = user ? '<span id="cart-count" class="ml-1 text-cyan-400 text-xs"></span>' : '';

    document.getElementById('main-nav').innerHTML = `
        <nav class="glass sticky top-0 z-40 border-b border-cyan-500/20">
            <div class="max-w-6xl mx-auto px-4 py-4 flex flex-wrap items-center justify-between gap-4">
                <a href="/index.html" class="font-display text-xl font-bold text-cyan-400 glow-text tracking-wider">
                    ТАБУЛКИН
                </a>
                <div class="flex flex-wrap gap-1 sm:gap-4 text-lg font-semibold">
                    <a href="/index.html" class="nav-link px-3 py-1 ${activePage === 'home' ? 'active' : 'text-slate-400 hover:text-cyan-400'}">Главная</a>
                    <a href="/catalog.html" class="nav-link px-3 py-1 ${activePage === 'catalog' ? 'active' : 'text-slate-400 hover:text-cyan-400'}">Каталог</a>
                    <a href="/cart.html" class="nav-link px-3 py-1 ${activePage === 'cart' ? 'active' : 'text-slate-400 hover:text-cyan-400'}">
                        Корзина${cartBadge}
                    </a>
                    <a href="/account.html" class="nav-link px-3 py-1 ${activePage === 'account' ? 'active' : 'text-slate-400 hover:text-cyan-400'}">Кабинет</a>
                    ${user && user.role === 'ADMIN' ? `<a href="/admin.html" class="nav-link px-3 py-1 ${activePage === 'admin' ? 'active' : 'text-fuchsia-400 hover:text-fuchsia-300'}">Админ</a>` : ''}
                </div>
                ${user ? `<span class="text-slate-400 text-sm">${user.username}</span>` : ''}
            </div>
        </nav>`;

    if (user) updateCartBadge();
}

async function updateCartBadge() {
    try {
        const cart = await API.getCart();
        const count = cart.items ? cart.items.reduce((s, i) => s + i.quantity, 0) : 0;
        const el = document.getElementById('cart-count');
        if (el) el.textContent = count > 0 ? `(${count})` : '';
    } catch (_) { /* not logged in */ }
}

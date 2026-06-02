const ORDER_STATUS_LABELS = {
    PENDING: 'Ожидает',
    CONFIRMED: 'Подтверждён',
    CANCELLED: 'Отменён'
};

function formatDate(iso) {
    if (!iso) return '—';
    return new Date(iso).toLocaleString('ru-RU');
}

function escapeHtml(text) {
    if (text == null) return '';
    const d = document.createElement('div');
    d.textContent = text;
    return d.innerHTML;
}

function renderOrderCard(order, options = {}) {
    const { showUser = false, allowStatusChange = false } = options;
    const itemsHtml = (order.items || []).map(item => `
        <li class="text-slate-400 text-sm">
            ${escapeHtml(item.productName)} — ${item.quantity} × ${formatPrice(item.price)}
        </li>
    `).join('');

    const statusSelect = allowStatusChange ? `
        <div class="flex flex-wrap items-center gap-2 mt-3">
            <select id="status-${order.id}" class="px-3 py-1 rounded-lg text-sm">
                <option value="PENDING" ${order.status === 'PENDING' ? 'selected' : ''}>Ожидает</option>
                <option value="CONFIRMED" ${order.status === 'CONFIRMED' ? 'selected' : ''}>Подтверждён</option>
                <option value="CANCELLED" ${order.status === 'CANCELLED' ? 'selected' : ''}>Отменён</option>
            </select>
            <button onclick="saveOrderStatus(${order.id})" class="text-cyan-400 text-sm font-bold hover:text-cyan-300">
                Сохранить статус
            </button>
        </div>
    ` : `<span class="inline-block mt-2 px-3 py-1 rounded-full text-sm font-semibold ${statusClass(order.status)}">${ORDER_STATUS_LABELS[order.status] || order.status}</span>`;

    return `
        <article class="glass rounded-xl p-5 mb-4">
            <div class="flex flex-wrap justify-between gap-2 mb-3">
                <h3 class="font-display text-cyan-300">Заказ #${order.id}</h3>
                <span class="text-slate-500 text-sm">${formatDate(order.createdAt)}</span>
            </div>
            ${showUser ? `<p class="text-slate-400 text-sm mb-2">Покупатель: <span class="text-fuchsia-400">${escapeHtml(order.username)}</span></p>` : ''}
            <p class="text-fuchsia-400 font-bold text-lg mb-2">${formatPrice(order.totalPrice)}</p>
            <div class="text-sm text-slate-400 space-y-1 mb-3">
                <p>📞 ${escapeHtml(order.phone || '—')}</p>
                ${order.address ? `<p>📍 ${escapeHtml(order.address)}</p>` : ''}
                ${order.comment ? `<p>💬 ${escapeHtml(order.comment)}</p>` : ''}
            </div>
            <ul class="list-disc list-inside mb-2">${itemsHtml || '<li class="text-slate-500">Нет позиций</li>'}</ul>
            ${statusSelect}
        </article>
    `;
}

function statusClass(status) {
    if (status === 'PENDING') return 'bg-amber-500/20 text-amber-300';
    if (status === 'CONFIRMED') return 'bg-emerald-500/20 text-emerald-300';
    if (status === 'CANCELLED') return 'bg-rose-500/20 text-rose-300';
    return 'bg-slate-500/20 text-slate-300';
}

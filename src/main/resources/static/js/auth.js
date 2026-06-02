const Auth = {
    TOKEN_KEY: 'ecommerce_token',
    USER_KEY: 'ecommerce_user',

    save(auth) {
        localStorage.setItem(this.TOKEN_KEY, auth.token);
        localStorage.setItem(this.USER_KEY, JSON.stringify({
            userId: auth.userId,
            username: auth.username,
            email: auth.email || null,
            role: auth.role
        }));
    },

    getToken() {
        return localStorage.getItem(this.TOKEN_KEY);
    },

    getUser() {
        const raw = localStorage.getItem(this.USER_KEY);
        return raw ? JSON.parse(raw) : null;
    },

    isLoggedIn() {
        return !!this.getToken();
    },

    isAdmin() {
        const user = this.getUser();
        return user && user.role === 'ADMIN';
    },

    logout() {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
        window.location.href = '/account.html';
    },

    requireLogin(redirectUrl) {
        if (!this.isLoggedIn()) {
            window.location.href = '/account.html?redirect=' + encodeURIComponent(redirectUrl || window.location.pathname);
            return false;
        }
        return true;
    },

    requireAdmin() {
        if (!this.isAdmin()) {
            alert('Доступ только для администратора');
            window.location.href = '/index.html';
            return false;
        }
        return true;
    }
};

// Переключение вкладок
function switchTab(tabId) {
    document.querySelectorAll('.form').forEach(form => form.classList.remove('active'));
    document.getElementById(tabId).classList.add('active');
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    event.currentTarget.classList.add('active');
}

// Показ формы подтверждения
function showConfirmForm(email) {
    document.querySelectorAll('.form').forEach(form => form.classList.remove('active'));
    document.getElementById('confirm-code').classList.add('active');
    document.getElementById('confirm-email').value = email;
}

// Сохранение токенов
function setAuthTokens(accessToken, refreshToken) {
    // Access Token сохраняем в localStorage
    localStorage.setItem('accessToken', accessToken);

    // Refresh Token устанавливаем в cookie
    document.cookie = `refreshToken=${refreshToken}; path=/; max-age=${60*60*24*7}; Secure; SameSite=Strict`;
}

// Проверка авторизации
function checkAuth() {
    return localStorage.getItem('accessToken') && document.cookie.includes('refreshToken');
}

// Выход из системы
async function logout() {
    try {
        const response = await fetch('http://localhost:9191/api/auth/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include' // Для отправки cookies
        });

        if (!response.ok) throw new Error('Ошибка выхода');

        // Очищаем токены на клиенте
        localStorage.removeItem('accessToken');
        document.cookie = 'refreshToken=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;';

        alert('Вы успешно вышли из системы');
        switchTab('login');
    } catch (error) {
        showError('global', error.message);
    }
}


// Обработка формы логина
document.getElementById('login').addEventListener('submit', async (e) => {
    e.preventDefault();
    const [username, password] = [...e.target.querySelectorAll('input')].map(input => input.value);

    try {
        const res = await fetch('http://localhost:9191/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
            credentials: 'include'
        });

        if (!res.ok) {
            const error = await res.json();
            throw new Error(error.error.message);
        }

        const { accessToken, refreshToken } = await res.json();
        setAuthTokens(accessToken, refreshToken);

        alert('Успешный вход!');
        // Перенаправление на защищенную страницу
        window.location.href = '/dashboard.html';

    } catch (err) {
        showError('login', err.message);
    }
});

// Обработка формы регистрации
document.getElementById('register').addEventListener('submit', async (e) => {
    e.preventDefault();
    const [username, email, password, phone] = [...e.target.querySelectorAll('input')].map(input => input.value);

    try {
        const res = await fetch('http://localhost:9191/api/auth/registration', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password, phoneNumber: phone })
        });

        if (!res.ok) {
            const error = await res.json();
            throw new Error(error.error.message);
        }

        showConfirmForm(email);
    } catch (err) {
        showError('register', err.message);
    }
});

// Обработка формы подтверждения
document.getElementById('confirm-code').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('confirm-email').value;
    const code = document.getElementById('confirm-code-input').value;

    try {
        const res = await fetch('http://localhost:9191/api/auth/registration/confirm', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, code }),
            credentials: 'include'
        });

        if (!res.ok) {
            const error = await res.json();
            throw new Error(error.error.message);
        }

        const { accessToken, refreshToken } = await res.json();
        setAuthTokens(accessToken, refreshToken);

        alert('Регистрация подтверждена!');
        window.location.href = '/dashboard.html';

    } catch (err) {
        showError('confirm-code', err.message);
    }
});

// Показ ошибок
function showError(formId, message) {
    const form = document.getElementById(formId);
    const errorEl = form.querySelector('.error-message');
    errorEl.textContent = message;
    errorEl.style.display = 'block';
    setTimeout(() => errorEl.style.display = 'none', 3000);
}

// Инициализация
(function init() {
    // Добавляем кнопку выхода если пользователь авторизован
    if (checkAuth()) {
        const header = document.querySelector('header');
        const logoutBtn = document.createElement('button');
        logoutBtn.textContent = 'Выйти';
        logoutBtn.onclick = logout;
        header.appendChild(logoutBtn);
    }
})();
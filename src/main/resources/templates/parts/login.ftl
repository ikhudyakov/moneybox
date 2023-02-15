<#macro login>
<div class="container">
    <form class="form-signin" method="post" action="/login">
        <h2 class="form-signin-heading">Авторизация</h2>
        <p>
            <label for="username" class="sr-only">Username</label>
            <input autocomplete="off" type="text" id="username" name="username" class="form-control" placeholder="Логин" required="" autofocus="">
        </p>
        <p>
            <label for="password" class="sr-only">Password</label>
            <input autocomplete="off" type="password" id="password" name="password" class="form-control" placeholder="Пароль" required="">
        </p>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
    </form>
</div>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Выйти</button>
    </form>
</#macro>

<#macro signin>
    <form action="/login/" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Войти</button>
    </form>
</#macro>
<#import "parts/common.ftl" as c>

<@c.page "Настройки">
<div class="container">
    <form class="form-signin" method="post" action="/settings">
        <h3 class="form-signin-heading mb-1 pb-1"">Настройки пользователя</h3>
        <p>
            <label for="email" class="sr-only">Email</label>
            <input autocomplete="off" type="email" id="email" name="email" class="form-control" placeholder="example@example.com" autofocus=""
                   value="<#if currentUser.email??>${currentUser.email}<#else></#if>">
        </p>
        <p>
            <label for="password" class="sr-only">Password</label>
            <input autocomplete="off" type="password" id="password" name="password" class="form-control" placeholder="Пароль" onkeyup='check();'>
        </p>
        <p>
            <label for="confirm_password" class="sr-only">Сonfirm_password</label>
            <input autocomplete="off" type="password" id="confirm_password" name="confirm_password" class="form-control" placeholder="Повторите пароль" onkeyup='check();'>
            <span id='message'></span>
        </p>
        <p>
            <label for="idTelegram" class="sr-only">idTelegram</label>
            <input autocomplete="off" type="text" id="idTelegram" name="idTelegram" class="form-control" placeholder="Введите MyMoney id"
                   value="<#if currentUser.idTelegram??>${currentUser.idTelegram}<#else></#if>">
        </p>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <button id='saveButton' class="btn btn-lg btn-primary btn-block" type="submit">Сохранить</button>
    </form>
</div>

<script>
$('#password, #confirm_password').on('keyup', function () {
if ($('#password').val() == "" && $('#confirm_password').val() == "") {
$('#message').html('');
}
  if ($('#password').val() == $('#confirm_password').val()) {
    $('#message').html('Пароли совпадают').css('color', 'green');
    $('#saveButton').css('class', 'disabled');
  } else
    $('#message').html('Пароли не совпадают').css('color', 'red');
    $('#saveButton').css('class', 'disabled').css('aria-disabled', 'true');
    if ($('#password').val() == "" && $('#confirm_password').val() == "") {
$('#message').html('');
$('#saveButton').css('class', '');
}
});
</script>
</@c.page>
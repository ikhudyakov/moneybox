<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">MyMoney</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/main">Домой</a>
            </li>
            <#if user??>
            <li class="nav-item">
                <a class="nav-link" href="/costs">Расходы</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/receipts">Доходы</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/settings">Настройки</a>
            </li>
            </#if>
            <#if isAdmin>
            <li class="nav-item">
                <a class="nav-link" href="/main">Администрирование</a>
            </li>
            </#if>
        </ul>

        <div class="navbar-text mr-3">${name}</div>
        <#if isActive>
            <@l.logout />
        </#if>
        <#if !isActive>
            <@l.signin />
        </#if>
    </div>
</nav>
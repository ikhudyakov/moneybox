<#import "period.ftl" as pe>
<#macro template actionType nameType>
<div class="container">
    <div align="center">
        <@pe.period actionType startDate endDate/>
        <h1 <#if nameType == "Расходы"> class="text-danger mb-3 pb-3" <#else> class="text-success mb-3 pb-3"</#if> class="form-signin-heading">${nameType}</h1>
        <button type="button" class="btn btn-primary btn-lg mb-1" data-toggle="modal" data-target="#myModal">
            Добавить запись
        </button>
    </div>
    <script>
        $('#myModal').on('shown.bs.modal', function () {
        $('#amount').focus()
        })
    </script>
    <!-- Модальное окно -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel">Добавление записи</h4>
                </div>
                <form method="post" action="${actionType}">
                    <div class="modal-body">
                        <p>
                        <div class="form-group">
                            <select class="form-control form-control-new" id="category" name="category">
                                <#list categories as category>
                                <option value="${category.id}">${category.name}</option>
                            </#list>
                            </select>
                        </div>
                        <p>
                        <label for="amount">Сумма</label>
                        <input autocomplete="off" type="number" id="amount" name="amount" class="form-control"
                               placeholder="0,00" required="" autofocus=""
                               step="0.01" min="0">
                        </p>
                        <p>
                            <label for="date">Дата</label>
                            <input autocomplete="off" type="date" id="date" name="date" class="form-control" placeholder=""
                                   value=${date} required="">
                        </p>
                        <p>
                            <label for="description">Описание</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"> Закрыть</button>
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button class="btn btn-primary" type="submit">Добавить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</#macro>
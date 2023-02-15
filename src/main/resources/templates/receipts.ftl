<#import "parts/common.ftl" as c>
<#import "parts/template.ftl" as t>
<#import "parts/pager.ftl" as p>


<@c.page "Доходы">
    <@t.template "/receipts" "Доходы">
    </@t.template>

    <div class="table-responsive col-xs-12 col-sm-12 col-md-6 offset-md-3 mb-1 pb-1">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col"></th>
                    <th scope="col"><p>Дата</p></th>
                    <th scope="col"><p align="right">Сумма</p></th>
                </tr>
            </thead>
            <tbody>
                <#list receipts.content as receipt>
                    <tr>
                        <td scope="row" class="clickable" data-toggle="modal" data-target="#description${receipt.id}"><img src="${receipt.category.path}"></td>
                        <td scope="row" class="clickable" data-toggle="modal" data-target="#description${receipt.id}">${receipt.date}</td>
                        <td scope="row" class="clickable" data-toggle="modal" data-target="#description${receipt.id}" align="right">+${receipt.amount}<b>₽</b></td>
                    </tr>
                <!-- Модальное окно -->
                <div class="modal fade" id="description${receipt.id}" tabindex="-1" role="dialog" aria-labelledby="descriptionLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title" id="descriptionLabel">Описание</h4>
                            </div>
                            <div class="modal-body">
                                <span>
                                    <#if receipt.description??>${receipt.description}<#else><font size="2">Описание отсутствует</font></#if>
                                </span>
                            </div>
                            <div class="modal-footer">
                                <a class="btn btn-primary" href="/receipts/${receipt.sId}?sId=${receipt.sId}">Изменить</a>
                                <a class="btn btn-danger" href="/receipts/${receipt.sId}/delete?sId=${receipt.sId}">Удалить</a>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"> Закрыть</button>
                            </div>
                        </div>
                    </div>
                </div>
                </#list>
            </tbody>
        </table>
    </div>
    <script>
        $('#description').on('shown.bs.modal', function () {
        })
    </script>
<@p.pager url receipts />
</@c.page>
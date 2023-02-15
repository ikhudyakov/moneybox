<#macro edit typeId category editType amount date description>
    <div class="container">
        <form class="form-signin" method="post" action="${editType}">
            <h2 align="center" class="form-signin-heading">Редактирование</h2>
            <p>
                <div class="form-group">
                    <select class="form-control form-control-new" id="category" name="category">
                        <option class="alert alert-success alert-primary-new" value="${typeId}"> ${category}</option>
                        <#list categories as category>
                            <option value="${category.id}">${category.name}</option>
                        </#list>
                    </select>
                </div>
                <p>
                    <label for="amount">Сумма</label>
                    <input autocomplete="off" type="number" id="amount" name="amount" class="form-control" required="" autofocus=""
                           step="0.01" min="0" value="${amount}" >
                </p>
                <p>
                    <label for="date">Дата</label>
                    <input autocomplete="off" type="date" id="date" name="date" class="form-control" placeholder="" value=${date} required="">
                </p>
                <p>
                    <label for="description">Описание</label>
                    <textarea class="form-control" id="description" name="description" rows="3" placeholder="${description}"></textarea>
                </p>
            </p>
            <input type="hidden" name="sId" value=${editType} />
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button class="btn btn-lg btn-primary btn-block" type="submit">Сохранить</button>
        </form>
    </div>
</#macro>

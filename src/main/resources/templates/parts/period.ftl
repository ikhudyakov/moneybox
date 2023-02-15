<#macro period actionType startDate endDate>
    <a class="btn btn-primary mb-2 pb-2" data-toggle="collapse" href="#period" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        <font size="2">
            ${startDate} — ${endDate} ▼
        </font>
    </a>
    <div class="collapse" id="period">
        <form class="form-signin" method="post" action="${actionType}/date">
            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 mb-1 pb-1">
                    <div class="mb-1 pb-1">
                        <input autocomplete="off" type="date" id="startDate" name="startDate" class="form-control"
                               placeholder="" value=${startDate} required="">
                    </div>
                    <div class="mb-1 pb-1">
                        <input autocomplete="off" type="date" id="endDate" name="endDate" class="form-control"
                               placeholder="" value=${endDate} required="">
                    </div>
                    <div align="center">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Обновить</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</#macro>
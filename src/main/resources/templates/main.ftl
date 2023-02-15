<#import "parts/common.ftl" as c>
<#import "parts/period.ftl" as pe>

<@c.page "MoneyBOX">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script>
    $(document).ready(function() {
    $("#reportSuccess").modal('show');
    });
    </script>

    <#if report=='true'??>
        <div class="modal fade" id="reportSuccess" tabindex="-1" role="dialog" aria-labelledby="reportSuccessLabel" aria-hidden="true">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="reportSuccessLabel">Отчет</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            Отчет отправлен успешно!
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"> Закрыть</button>
                    </div>
                </div>
            </div>
        </div>
    <#else></#if>
    <div align="center">
        <@pe.period actionType startDate endDate/>
    </div>
    <h2><p data-toggle="modal" data-target="#report" class="text-info clickable">Кошелек: ${allBalance} ₽</p></h2>
    <h2><p data-toggle="modal" data-target="#report" class="text-info clickable">Баланс: ${balance} ₽</p></h2>
    <h2><p><a class="text-danger" class="nav-link" href="/costs">Расходы: ${costs} ₽</a></p></h2>
    <h2><p><a class="text-success" class="nav-link" href="/receipts">Доходы: ${receipts} ₽</a></p></h2>
    <script src="https://www.google.com/jsapi"></script>
    <script>
       google.load("visualization", "1", {packages:["corechart"]});
       google.setOnLoadCallback(drawChart);
       function drawChart() {
        var data = google.visualization.arrayToDataTable([
         ['Тип', 'Сумма'],
         ['Расходы', parseFloat(${costs})],
         ['Доходы', parseFloat(${receipts})]
        ]);
        var options = {
         is3D: true,
         backgroundColor: { fill: '#eee' },
         slices: {
            0: { color: '#dc3545' },
            1: { color: '#28a745' }
         }

        };
        var chart = new google.visualization.PieChart(document.getElementById('piechart'));
         chart.draw(data, options);
       }
    </script>
    <div id="piechart" style="width: 350px; height: 350px;"></div>

    <!-- Модальное окно -->
    <div class="modal fade" id="report" tabindex="-1" role="dialog" aria-labelledby="reportLabel" aria-hidden="true">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <form method="post" action="/report">
                    <div class="modal-header">
                        <h4 class="modal-title" id="reportLabel">Отчет</h4>
                    </div>
                    <div class="modal-body">
                        <div class="col-xs-12 col-sm-12 col-md-12 mb-1 pb-1">
                            <div class="mb-1 pb-1">
                                <input autocomplete="off" type="date" id="startDate" name="startDate" class="form-control"
                                       placeholder="" value=${startDate} required="">
                            </div>
                            <div class="mb-1 pb-1">
                                <input autocomplete="off" type="date" id="endDate" name="endDate" class="form-control"
                                       placeholder="" value=${endDate} required="">
                            </div>
                            <div>
                                <label for="email">Email</label>
                                <input autocomplete="off" type="email" id="email" name="email" class="form-control"
                                       placeholder="example@example.com" value="<#if email??>${email}<#else></#if>" required="" autofocus="">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"> Закрыть</button>
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <button class="btn btn-primary" type="submit">Отправить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>



    <!--<script>
       google.load("visualization", "1", {packages:["corechart"]});
       google.setOnLoadCallback(chartByType);
       var array = [];
       categories.forEach(function(element) {
            console.log(element);

        });
        array.push(categories(0));
        array.push(categories(1));
        array.push(categories(2));
       function chartByType() {
        var data = google.visualization.arrayToDataTable([categories[0],categories[1],categories[2]]);
        var options = {
         is3D: true,
         backgroundColor: { fill: '#eee' },
        };
        var chart = new google.visualization.PieChart(document.getElementById('pieChartByType'));
         chart.draw(data, options);
       }
    </script>
    <div id="pieChartByType" style="width: 400px; height: 400px;"></div>-->

</@c.page>


<#import "parts/common.ftl" as c>
<#import "parts/edit.ftl" as e>

<@c.page "Редактирование">
    <@e.edit "${cost.category.id}" "${cost.category.name}" "${cost.sId}" "${amount}" "${date}" "${description}">
    </@e.edit>
</@c.page>
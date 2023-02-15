<#import "parts/common.ftl" as c>
<#import "parts/edit.ftl" as e>

<@c.page "Редактирование">
    <@e.edit "${receipt.category.id}" "${receipt.category.name}" "${receipt.sId}" "${amount}" "${date}" "${description}">
    </@e.edit>
</@c.page>
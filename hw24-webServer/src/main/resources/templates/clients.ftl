<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Пользователи</title>
    <script>
        function getClientById() {
            const clientIdTextBox = document.getElementById('clientIdTextBox');
            const clientDataContainer = document.getElementById('clientDataContainer');
            const id = clientIdTextBox.value;
            fetch('api/clients/' + id)
                .then(response => response.json())
                .then(user => clientDataContainer.innerHTML = JSON.stringify(user));
        }
    </script>
</head>

<body>
<h4>Получить клиента по id</h4>
<input type="text" id = "clientIdTextBox" placeholder="Введите id клиента">
<button onclick="getClientById()">Получить</button>
<pre id = "clientDataContainer"></pre>

<h4>Добавить клиента</h4>
<form action="clients" method="post">
    <label>
        Имя <br/>
        <input type="text" name="name" placeholder="Имя клиента">
    </label>
    <br/>
    <label>
        Адрес<br/>
        <input type="text" name="address" placeholder="Адрес клиента">
    </label>
    <br/>
    <label>
        Телефон<br/>
        <input type="text" name="phone" placeholder="Номер телефона">
    </label>
    <br/><br/>
    <input type="submit" value="Добавить клиента">
</form>

<h4>Список пользователей</h4>
<table style="width: 400px">
    <thead>
    <tr>
        <td style="width: 50px">Id</td>
        <td style="width: 150px">Имя</td>
        <td style="width: 250px">Адрес</td>
        <td style="width: 600px">Телефон</td>
    </tr>
    </thead>
    <tbody>
    <#list clientsList as client>
        <tr>
            <td>${client.id}</td>
            <td>${client.name}</td>
            <td>${client.address}</td>
            <td>${client.phoneNumber}</td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>

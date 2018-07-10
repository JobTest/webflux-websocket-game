
Игра - угадай цифру
-------------------

Сервер генерирует число от 1 до 10, и ждет варианты в течении 10 секунд.
Тому кто угадал сервер шлет ответ “You win”, тому кто проиграл “You lose”

Сервер генерирует свою цифру от 1 до 10
Клиент подключается к серверу через веб сокет.
После подключения клиент отсылает случайную цифру от 1 до 10.
Через 10 секунд сервер проверяет присланые варианты и отсылает ответы.
После того как ответы высланы сервер отсылает сообещние “Start round”.
Клиенты должны снова прислать свои варианты.

Должна быть реализована логика на сервере и код должен быть покрыт тестами.
Должен быть integration test который может запустить определеное кол-во клиентов.

Stack:
Java 8+
Spring Webflux
Spring Boot 2
Gradle



#Note

В браузере клиента специальный объект 'EventSource' обеспечивает соединение с сервером и работает по обычному протоколу HTTP.
Объект 'EventSource' умеет работать со стримами которые бросает ему сервер (сервер должен ответить с заголовком Content-Type: text/event-stream)

############################################################

Цели:
---
    1. Создать отдельный `queue` канал веб-сокета для общения между сервером и клиентом (не в топик-канале)




#Web-Client

* Запуск приложения
![sTyj5n](tutorial/sTyj5n.jpg)

* Подключение к игре
![yZ5bX2](tutorial/yZ5bX2.jpg)
![q4LxD3](tutorial/q4LxD3.jpg)
![iUpfoH](tutorial/iUpfoH.jpg)

* Новая ставка
![cH1mC8](tutorial/cH1mC8.jpg)
![hZ1FgQ](tutorial/hZ1FgQ.jpg)

* Сервер разыгривает число между игроками
![mydYQp](tutorial/mydYQp.jpg)
![QlvOjk](tutorial/QlvOjk.jpg)

* Игрок пытается сделать несколько ставок в одном раунде
![pjMCLF](tutorial/pjMCLF.jpg)
![SjiGpB](tutorial/SjiGpB.jpg)


#Test

* для 1-раунда создается 15-игроков
    ![1](tutorial/1.jpg)

* для 1-раунда создается 150-игроков
    ![2](tutorial/2.jpg)

* для 1-раунда создается 1000-игроков
    ![3](tutorial/3.jpg)

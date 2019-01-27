
Игра - угадай цифру
-------------------

* [JBreak 2018: прямая трансляция из главного зала](https://www.youtube.com/watch?v=VBrZ_nyjBaQ&feature=youtu.be&t=29605)
* [Spring 5 WebClient and WebTestClient Tutorial with Examples](https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples)


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

![s7Oelm](tutorial/s7Oelm.jpg)

* [Mongo Plugin](https://plugins.jetbrains.com/plugin/7141-mongo-plugin)
  ![Mongo Plugin](https://plugins.jetbrains.com/files/7141/screenshot_16492.png)

`spring.data.mongodb.host` and `spring.data.mongodb.port` are not supported if you’re using the **Mongo 3.0 Java driver**.
In such cases, spring.data.mongodb.uri should be used to provide all of the configuration, like this:
```properties
spring:
    data:
        mongodb:
#            uri: mongodb://admin:1978@localhost:27017/test?authSource=test&authMechanism=SCRAM-SHA-1
            uri: mongodb://admin:1978@localhost:27017/test?authMechanism=SCRAM-SHA-1
```

* [Support for Query-by-Example](https://github.com/spring-projects/spring-data-examples/tree/master/jpa/query-by-example)
* [Reactive Spring Data MongoDB](http://javasampleapproach.com/reactive-programming/angular-4-spring-webflux-spring-data-reactive-mongodb-example-full-reactive-angular-4-http-client-spring-boot-restapi-server)
  ![angular-4-spring-webflux-reactive-mongodb-architecture](tutorial/angular-4-spring-webflux-reactive-mongodb-architecture.png)
* [a-quick-look-into-reactive-streams-with-spring-data-and-mongodb](https://lankydanblog.com/2017/07/16/a-quick-look-into-reactive-streams-with-spring-data-and-mongodb)
  [spring-data-reactive-mongodb](https://github.com/lankydan/spring-data-reactive-mongodb)
  [spring-data-mongodb-with-reactive-mongodb](https://dzone.com/articles/spring-data-mongodb-with-reactive-mongodb)
  [Reactive Streams With Spring Data and MongoDB](https://dzone.com/articles/reactive-streams-with-spring-data-and-mongodb)
  [Spring Data MongoDB Example](https://www.journaldev.com/4144/spring-data-mongodb-example)
* [Ошибки, связанные с откатом транзакций](https://www.ibm.com/developerworks/ru/library/j-ts1/index.html)

[Why do I get Gson builder error when starting a Spring Boot application?](https://stackoverflow.com/questions/50031381/why-do-i-get-gson-builder-error-when-starting-a-spring-boot-application)

############################################################

Цели:
---
* Проблема №1 - между каждым игроком и сервером создать отдельный канал `queue` веб-сокета
* Проблема №2 - каждый такой отдельный канал `queue` веб-сокета должен создаваться динамически (по запросу)

        Для этого я создал свой бин 'HandlerMapping' со скоупом = prototype.
        И при каждом реквесте клиента - будет создаваться новый бин.
        И в этот момент бин инициализируется новыми данными которые переданы в реквесте + добавлены в постоянный список.
        --------------------
        Вот таким хитрым способом создается отдельный канал `queue` веб-сокета.

* Проблема №3 - это заставляет использовать HTTP-протокол (не канал веб-сокета)

        Одно из решений - можно в клиентском браузере просто отдельной логикой подписаться на этот канал веб-сокета.
        --------------------
        Другой вариант:
            На сервере создать WebSocketClient-а, который в том-же REST-методе будет подписывать на этот канал.
            Дальше получать данные из канала и перебрасывать их на HTTP-протокол - в этом случае Flux умеет выплевывать стрим.
            А на стороне клиента в браузере объект 'EventSource' - умеет работать со стримами и только по HTTP-протоколу.

* 

- - Когда спринг подымает свой контекс - создается мой бин со скоупом prototype.
- - (Создавать бин можно по разному, например: или объявлять компонент, или серез бин-аннотацию)
- - В момент создания инстанца-бина - выполняет иннициализация данных.
- - При этом создаются новые каналы, внутри канала открывается сессия и вещаются туда данные.
- - Дальше WebSocketClient подписывается на этот канал и принимает данные из сессии.
- - Потом эти принятые данные Flux бросает по HTTP-протоколу REST-метода (перебрасывает).
- - В браузере клиент EventSource открывает соединение и получает стрим и работает с ним (у него есть слушатели-событий).
- - Как только пришли данные об завершении работы - EventSource закрывает соединение.


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

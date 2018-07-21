--
-- http://qaru.site/questions/2736/retrieving-the-last-record-in-each-group
-- ( http://sqlfiddle.com/#!9/e71b72/7 )

DROP TABLE IF EXISTS messages;

CREATE TABLE messages(
  id int,
  #   time timestamp default current_timestamp ,
  time timestamp,
  name varchar(1),
  other varchar(8)
);

INSERT INTO messages(id, name, other, time)
VALUES
  (1, 'A', 'A_data_1', '2018-06-21 21:58:50'),
  (2, 'A', 'A_data_2', '2018-06-21 21:58:51'),
  (3, 'A', 'A_data_3', '2018-06-21 21:58:52'),
  (4, 'B', 'B_data_1', '2018-06-21 21:58:53'),
  (5, 'B', 'B_data_2', '2018-06-21 21:58:54'),
  (6, 'C', 'C_data_1', '2018-06-21 21:58:55');

--
-- это агрегаторная функция, она возвращает максимальное значение как число
--
SELECT MAX(id) AS id FROM messages;
-- 6

--
-- сначала здесь группируем записи по имени
-- потом по каждой сгруппированной записи возвращаем запись с максимальным значением-ID
-- в результате будет 3-записи с максимальным-ID
--
SELECT MAX(id) AS id FROM messages GROUP BY name;
-- 3
-- 5
-- 6

--
-- здесь весь фокус во внутреннем запросе:
-- внутренний запрос получает 3-записи с максимальным-ID и приводит ее к переменной 't', то есть создается такая себе промежуточная-временная таблица
-- и 'INNER JOIN' работает с 't' уже как с таблицей: обычно 'messages' - это первая таблица; 't' - это вторая таблица; (сравниваем ID из двух таблиц)
-- еще в добавок можно ограничить количество результатов записей
--
-- INNER JOIN (SELECT MAX(id) AS id FROM messages GROUP BY name) AS t

SELECT m.id, m.name, m.other, m.time FROM messages m
  INNER JOIN (SELECT MAX(time) AS time FROM messages GROUP BY name) AS t
  ON (m.time=t.time)
  LIMIT 2;

-- 3	A	A_data_3	2018-06-21 21:58:52
-- 5	B	B_data_2	2018-06-21 21:58:54


DROP TABLE messages;


-- ( http://sqlfiddle.com/#!9/ab6c62/3 )
#
# CREATE TABLE IF EXISTS user_test(
#   id int,
#   name varchar(255),
#   city varchar(255)
# );
#
# INSERT INTO user_test
# VALUES
#   (1,'A','A_data_1'),
#   (2,'A','A_data_2'),
#   (3,'A','A_data_3'),
#   (4,'B','B_data_1'),
#   (5,'B','B_data_2'),
#   (6,'C','C_data_1');
#
# SELECT u.id, name, city FROM user_test u
#   INNER JOIN (SELECT MAX(id) AS id FROM user_test GROUP BY name) AS t
#   ON (u.id=t.id);
#
# DROP TABLE user_test;
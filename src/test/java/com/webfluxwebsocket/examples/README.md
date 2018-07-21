
#1. Inheritance for Abstract class

```java
class My {
    public void func1() {
        System.out.println("My.func1()");
    }
}

abstract class AMy extends My {
    public abstract void func1();
    public abstract void func2();
}

class MyClazz extends AMy {
    @Override
    public void func1() {
        System.out.println("MyClazz.func1()");
    }

    @Override
    public void func2() {
        System.out.println("MyClazz.func2()");
    }
}

//
MyClazz myClazz = new MyClazz();
myClazz.func1(); // MyClazz.func1()
myClazz.func2(); // MyClazz.func2()
```


#2. enum to convert another enum

```java
enum ColorTitle {
    A("one"), B("two"), C("three"),;

    private String title;

    ColorTitle(String title) {
        this.title = title;
    }
}

enum ColorRGB {
    A(1), B(2), C(3),;

    private int value;

    ColorRGB(int value) {
        this.value = value;
    }
}

static ColorRGB toRGB(ColorTitle title) {
    return ColorRGB.valueOf(title.name());
}

static ColorTitle toTitle(ColorRGB rgb) {
    return ColorTitle.valueOf(rgb.name());
}

//
title = ColorTitle.A;
rgb = toRGB(title); // title = one; rgb = 1

rgb = ColorRGB.B;
title = toTitle(rgb); // rgb = 2; title = two;

List<ColorRGB> rgbs = titles.stream()
        .map(t -> toRGB(t))
        .collect(Collectors.toList()); // rgb = 1; rgb = 2; rgb = 3; 
```


#3. sql get last records to and group they

```sql
CREATE TABLE users(
  id int,
  name varchar(255),
  time timestamp
);

INSERT INTO users(id, name, time)
VALUES
  (1, 'A', '2018-06-21 21:58:50'),
  (2, 'A', '2018-06-21 21:58:51'),
  (3, 'A', '2018-06-21 21:58:52'),
  (4, 'B', '2018-06-21 21:58:53'),
  (5, 'B', '2018-06-21 21:58:54'),
  (6, 'C', '2018-06-21 21:58:55');

SELECT u.id, u.name, u.time FROM users u
  INNER JOIN (SELECT MAX(time) AS time FROM users GROUP BY name) AS t
  ON (m.time=t.time)
  LIMIT 2;

--
-- 3	A	2018-06-21 21:58:52
-- 5	B	2018-06-21 21:58:54
```

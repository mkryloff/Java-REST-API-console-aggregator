# 🌐 Data Aggregator | Многопоточный агрегатор данных из REST API

Консольное Java-приложение для многопоточной агрегации данных из различных открытых REST API с сохранением результатов в JSON и CSV форматах.

---

## Описание

**Data Aggregator** — это курсовая работа в рамках ООП, в которой были поддержаны:

- **многопоточность**: параллельный опрос множественных API с управлением пулом потоков
- **архитектурные паттерны**: Strategy, Factory, Adapter для расширяемости
- **разделение ответственности**: отделение логики CLI, агрегации и хранения данных
- **контроль ресурсов**: потокобезопасное сохранение данных и корректное завершение работы
- **полное покрытие тестами**: >70% line coverage с использованием JUnit 5 и Mockito

Приложение поддерживает три встроенных API:
- **Weather API** (Open-Meteo) - текущие условия погоды
- **Random Users API** - данные случайных пользователей
- **Meal API** - рецепты случайных блюд

Также система расширяема: можно добавлять новые API.

---

## Функциональность

### Режимы запуска

#### Интерактивный режим
```bash
java -jar target/cw-1.0-SNAPSHOT.jar
```

Интерактивное меню с опциями
1. **Fetch data** - единовременный запрос к выбранным API
2. **Print data** - вывод содержимого сохранённого файла
3. **Start polling** - периодический опрос с заданным интервалом
4. **Stop polling** - остановка опроса
5. **Exit** - выход из приложения

#### Автоматический режим
```bash
java -jar target/cw-1.0-SNAPSHOT.jar <api_ids> <format> [append] [n] [t]
```

**Параметры**
- `<api_ids>` - номера API через пробел (1=Weather, 2=Users, 3=Meals)
- `<format>` - json или csv
- `[append]` - true/false (дозапись в файл, default: false)
- `[n]` - max concurrent tasks (default: 1)
- `[t]` - polling interval в секундах (default: 10)

**Примеры**
```bash
# Получить данные из всех API в JSON
java -jar target/cw-1.0-SNAPSHOT.jar 1 2 3 json

# Периодический опрос (5 потоков, каждые 30 сек)
java -jar target/cw-1.0-SNAPSHOT.jar 1 2 csv true 5 30

# Заданный интервал вполовину от default
java -jar target/cw-1.0-SNAPSHOT.jar 1 json false 2 5
```

### Пример преобразования данных

**Исходные данные из API**
```json
{
  "meals": [
    {"name": "Pasta", "id": 1},
    {"name": "Soup", "id": 2}
  ]
}
```

**В JSON сохраняется как:**
```json
{
  "id": "uuid-1",
  "source": "meals",
  "timestamp": "2026-02-04T12:00:00Z",
  "data": { ... }
}
```

**В CSV денормализуется в:**
```
id,source,timestamp,data.meals.name,data.meals.id
uuid-1,meals,2026-02-04T12:00:00Z,Pasta,1
uuid-1,meals,2026-02-04T12:00:00Z,Soup,2
```

---

### Основные библиотеки

| Библиотека | Версия | Назначение |
|-----------|--------|-----------|
| **OkHttp** | 4.12.0 | HTTP-клиент для REST API |
| **Jackson** | 2.21.2 | Парсинг и сериализация JSON |
| **JUnit Jupiter** | 5.11.0 | Unit-тестирование |
| **Mockito** | 5.12.0 | Мокирование зависимостей |
| **JaCoCo** | 0.8.11 | Анализ покрытия тестами |

### Требования

- **Java**: 21+
- **Maven**: 3.8+
- **ОС**: Linux, macOS, Windows

---

## Сборка и запуск

### Предварительная подготовка

```bash
# Клонирование репозитория
git clone <repository-url>
cd data-aggregator

# Проверка версии Java
java -version  # Должна быть 21+
```

### Сборка проекта

```bash
# Скачать зависимости и собрать JAR
mvn clean package

# Пропустить тесты (при необходимости)
mvn clean package -DskipTests
```

После успешной сборки JAR находится в `target/cw-1.0-SNAPSHOT.jar`

### Запуск приложения

#### Интерактивный режим
```bash
cd target
java -jar cw-1.0-SNAPSHOT.jar
```

#### Автоматический режим с примерами
```bash
# Пример 1: Получить данные из Weather и Users в JSON
java -jar cw-1.0-SNAPSHOT.jar 1 2 json

# Пример 2: Включить все API, CSV формат, дозапись, 3 потока, интервал 20 сек
java -jar cw-1.0-SNAPSHOT.jar 1 2 3 csv true 3 20

# Пример 3: Непрерывный опрос Meals API каждые 5 сек
java -jar cw-1.0-SNAPSHOT.jar 3 json false 1 5
```

### Запуск тестов

```bash
# Запустить все тесты
mvn test

# Запустить конкретный тест-класс
mvn test -Dtest=DataAggregatorTest

# Генерировать отчёт покрытия JaCoCo
mvn clean package
# Отчёт будет в target/site/jacoco/index.html
```

---

## Покрытие тестами

Проект имеет **>70% line coverage** согласно JaCoCo.

**Всего:** 112 unit-тестов

### Просмотр отчёта о покрытии

```bash
mvn clean package
open target/site/jacoco/index.html  # macOS
# или используйте браузер для открытия этого файла на Windows/Linux
```

---

## Примеры использования

### Сценарий 1: единовременный запрос

**Интерактивно:**
```
Запуск: java -jar cw-1.0-SNAPSHOT.jar
Выбрать опцию: 1
Выбрать API: 1 2
Формат: json
Режим: 1 (новый файл)

Результат: output.json содержит данные Weather и Users
```

**Автоматически:**
```bash
java -jar cw-1.0-SNAPSHOT.jar 1 2 json
```

### Сценарий 2: мониторинг с периодическим опросом

**Интерактивно:**
```
Выбрать опцию: 3 (Start polling)
Выбрать API: 1
Формат: json
Режим: 1 (новый)
Max concurrent: 2
Interval: 30

# Опрос начнётся и будет повторяться каждые 30 сек
# Нажать Enter для остановки
```

**Автоматически:**
```bash
java -jar cw-1.0-SNAPSHOT.jar 1 json false 2 30
# Опрос Weather API каждые 30 сек до нажатия Enter
```

### Сценарий 3: работа с CSV

```bash
# Экспортировать все API в CSV с дозаписью
java -jar cw-1.0-SNAPSHOT.jar 1 2 3 csv true 1 0

# Последующие запросы будут добавлять строки в output.csv
java -jar cw-1.0-SNAPSHOT.jar 1 csv true 1 0
```

---

## Форматы выходных данных

### JSON Формат

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "source": "weather",
    "timestamp": "2026-02-04T14:23:45.123456Z",
    "data": {
      "latitude": 59.9311,
      "longitude": 30.3609,
      "current_weather": {
        "temperature": -2.5,
        "wind_speed": 12.3
      }
    }
  }
]
```

### CSV Формат

```csv
id,source,timestamp,data.latitude,data.longitude,data.current_weather.temperature,data.current_weather.wind_speed
550e8400-e29b-41d4-a716-446655440000,weather,2026-02-04T14:23:45.123456Z,59.9311,30.3609,-2.5,12.3
```

---

## Интеграция с API

### Поддерживаемые API

#### 1. Open-Meteo Weather API
```
URL: https://api.open-meteo.com/v1/forecast
Параметры: latitude, longitude, current_weather
Возвращает: Текущие условия погоды
```

#### 2. Random User Generator API
```
URL: https://randomuser.me/api/
Параметры: results
Возвращает: Случайные данные пользователя
```

#### 3. TheMealDB API
```
URL: https://www.themealdb.com/api/json/v1/1/random.php
Параметры: (none)
Возвращает: Случайный рецепт блюда
```

### Добавление новых API

Создайте класс, реализующий `ApiProvider`:

```java
public class MyCustomApi implements ApiProvider {
    @Override
    public String getName() {
        return "my-api";
    }
    
    @Override
    public String getBaseUrl() {
        return "https://api.example.com/v1/data";
    }
    
    @Override
    public Map<String, String> getDefaultParams() {
        return Map.of("key", "value");
    }
}
```

Затем зарегистрируйте (добавьте) его в `ApiRegistry`:
```java
providers.put("4", new MyCustomApi());
```

## Важные замечания

1. **Лимиты API**: некоторые публичные API могут иметь rate limits. Используйте интервал ≥5 сек для `t`.

2. **Память**: при большом количестве записей рассмотрите использование streaming вместо загрузки всех данных в память.

3. **Корректное завершение**: всегда нажимайте Enter в polling режиме для остановки перед завершением приложения.

4. **Кодировка**: файлы сохраняются в UTF-8. Убедитесь, что ваш редактор это поддерживает.

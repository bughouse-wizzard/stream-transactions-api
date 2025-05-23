stream-transactions-api

Java 21 микросервис для просмотра истории транзакций из CSV-файлов с поддержкой фильтрации, сортировки и пагинации. Оптимизирован для обработки больших файлов с минимальным использованием памяти.

⸻

Возможности
	•	Чтение двух CSV-файлов: incomes.csv и outcomes.csv
	•	Объединение и сортировка по дате на лету
	•	Фильтрация по счёту (accountId)
	•	Фильтрация по дате (только конкретный день)
	•	Пагинация через offset и limit
	•	Многопоточность на виртуальных потоках Java 21
	•	Возврат в формате JSON
	•	Полностью без Spring, только Java + HTTP Server

⸻

Технологии
	•	Java 21 (Virtual Threads)
	•	Maven
	•	Jackson (для JSON)
	•	Docker

⸻

Структура проекта

stream-transactions-api/
├── incomes.csv
├── outcomes.csv
├── src/
│   └── main/java/com/dimk00/streamtransactions/
│       ├── Main.java
│       ├── model/Transaction.java
│       ├── io/TransactionSource.java
│       ├── service/TransactionService.java
│       ├── http/TransactionHandler.java
│       └── util/QueryUtils.java
├── Dockerfile
├── pom.xml
└── README.md


⸻

Сборка и запуск (Docker)

# Сборка
docker build -t stream-transactions-api .

# Запуск на порту 8080
docker run -p 8080:8080 stream-transactions-api


⸻

Примеры запросов

GET /transactions?offset=0&limit=20

GET /transactions?accountId=12345

GET /transactions?date=2023-06-19

GET /transactions?accountId=12345&date=2023-06-19&offset=40&limit=20


⸻

Формат ответа

[
  {
    "transactionId": "968953739",
    "customerId": "45475",
    "accountId": "1561887",
    "amount": 602871.09,
    "dateTime": "2022-12-11T01:41:17",
    "type": "INCOME"
  },
  ...
]


⸻

Ограничения
	•	Поддерживается только фильтрация по одной дате (а не диапазону)
	•	CSV-файлы должны быть в UTF-8 и с заголовками
	•	Обработка двух файлов происходит в одном потоке, но запросы обрабатываются параллельно

⸻

Почему так?

Тестовое задание требовало работы с большими, неотсортированными файлами без полной загрузки в память. Это решение:
	•	обходит эту проблему через построчное чтение
	•	реализует ленивую сортировку (наподобие merge-сортировки)
	•	показывает архитектурный подход к построению лёгкого API без фреймворков

⸻

Планируемое улучшение
	•	Поддержка диапазона дат
	•	Асинхронное чтение CSV из обоих источников
	•	Интеграционные тесты
	•	docker-compose с nginx или API gateway

⸻

# Parallel Mergesort Client-Server Application

Това е Java проект, реализиращ клиент-сървърна архитектура за паралелно сортиране на данни. Приложението използван **Java Sockets**, **Object Serialization** и **Fork/Join Framework** за многонишково програмиране.

## Функционалности

1.  **Multithreaded Server:** Сървърът може да обслужва множество клиенти едновременно.
2.  **Parallel Merge Sort:** Използва `RecursiveAction` и `ForkJoinPool` за разпределяне на сортирането между всички налични процесорни ядра.
3.  **Сравнителен анализ:** Клиентът автоматично измерва и сравнява времето за изпълнение при една нишка (sequential) спрямо много нишки (parallel).
4.  **TCP Комуникация:** Обмен на данни чрез сериализирани Java обекти (`SortRequest`, `SortResponse`).
5.  **Thread Safety:** Напълно thread-safe архитектура без споделено състояние (shared mutable state).

## Технически Изисквания

* **Java JDK:** версия 24 или по-нова.
* **Apache Maven:** за управление на зависимостите и компилация.

## Структура на Проекта

```text
project-root/
├── pom.xml                   # Maven конфигурация
├── src/
│   ├── main/java/
│   │   ├── client/           # Клиентска логика
│   │   │   └── Client.java
│   │   ├── server/           # Сървърна логика
│   │   │   ├── Server.java
│   │   │   ├── ClientHandler.java
│   │   │   └── ParallelMergeSorter.java
│   │   └── common/           # Общи DTO обекти
│   │       ├── SortRequest.java
│   │       └── SortResponse.java
│   └── test/java/            # Unit и Integration тестове
│       ├── server/
│       ├── client/
│       └── common/

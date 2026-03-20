Feature: Успешный импорт
  Необходимо импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Используются тестовые учетные данные Jira
    And Задан вид ключа возвращаемый от Jira

  Scenario: Успешный импорт одной задачи
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "TEST"
      summary: "some summary for test"
      type: "Task"
      priority: "High"
      description: "some description for test"
    """

    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 1
    And Jira возвращает ключ задачи вида "TEST-*"

  Scenario: Успешный импорт 3 задач
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "TEST"
      summary: "first summary for test"
      type: "Task"
    - project: "TEST"
      summary: "second summary for test"
      type: "Bug"
    - project: "TEST"
      summary: "third summary for test"
      type: "Feature"
    """

    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 3
    And Jira возвращает ключи задачи вида "TEST-*"

  Scenario: Импорт задачи из пустого файла
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues: []
    """

    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Ни одной задачи не должно быть создано

  Scenario: Импорт задачи с неизвестными полями
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "TEST"
      summary: "Task with unknown field"
      type: "Task"
      priority: "Low"
      description: "This task has some unknown fields and program should create task and skip unknown fields"
      creator: "John"
      reader: "Walter White"
    """

    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 1
    And Jira возвращает ключ задачи вида "TEST-*"

  Scenario: Импорт задачи с пустыми опциональными полями
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "TEST"
      summary: "Task with empty optional fields"
      type: "Task"
      priority: ""
      description: ""
    """

    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 1
    And Jira возвращает ключ задачи вида "TEST-*"
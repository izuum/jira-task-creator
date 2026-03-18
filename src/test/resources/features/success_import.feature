Feature: Успешный импорт
  Как пользователь
  Я хочу импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Jira API настроена на "https://dstepds.atlassian.net"

  Scenario: Успешный импорт одной задачи
    Given YAML файл с задачами
    """
    issues:
    - project: "TEST"
      summary: "some summary for test"
      type: "Task"
      priority: "High"
      description: "some description for test"
    """

    When Я запускаю программу
    Then Программа должна завершиться успешно
    And В Jira должна создаться одна задача
    And У задачи должен быть ключ вида "TEST-*"

  Scenario: Успешный импорт 3 задач
    Given YAML файл с задачами
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

    When Я запускаю программу
    Then Программа должна завершиться успешно
    And В Jira должны создаться три задачи
    And У задач должны быть ключи вида "TEST-*"

  Scenario: Импорт задачи из пустого файла
    Given YAML файл с задачами
    """
    issues: []
    """

    When Я запускаю программу
    Then Программа должна завершиться успешно
    And Ни одной задачи не должно быть создано

  Scenario: Импорт задачи с неизвестными полями
    Given YAML файл с задачами
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

    When Я запускаю программу
    Then Программа должна завершиться успешно
    And В Jira должна создаться одна задача
    And У задачи должен быть ключ вида "TEST-*"

  Scenario: Импорт задачи с пустыми опциональными полями
    Given YAML файл с задачами
    """
    issues:
    - project: "TEST"
      summary: "Task with empty optional fields"
      type: "Task"
      priority: ""
      description: ""
    """

    When Я запускаю программу
    Then Программа должна завершиться успешно
    And В Jira должна создаться одна задача
    And У задачи должен быть ключ вида "TEST-*"
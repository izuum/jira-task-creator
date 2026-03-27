Feature: Импорт задач - позитивные сценарий
  Необходимо импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Используются тестовые учетные данные Jira
    And Задан вид ключа возвращаемый от Jira

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
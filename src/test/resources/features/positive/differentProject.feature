Feature: Импорт задач - позитивные сценарий
  Необходимо импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Используются тестовые учетные данные Jira
    And Задан вид ключа возвращаемый от Jira

  Scenario: Импорт задач в разные проекты из одного файла
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "PROD"
      summary: "Summary for project TEST"
      type: "Баг"
    - project: "TEST"
      summary: "Summary for project PROD"
      type: "Task"
    - project: "DEV"
      summary: "Summary for project DEV"
      type: "Feature"
    """
    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 3
    And Jira возвращает ключ задачи вида "*-*"
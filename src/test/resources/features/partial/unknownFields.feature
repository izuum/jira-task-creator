Feature: Импорт с частичным успехом
  Необходимо импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Используются тестовые учетные данные Jira
    And Задан вид ключа возвращаемый от Jira

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
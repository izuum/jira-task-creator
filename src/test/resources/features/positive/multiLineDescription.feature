Feature: Импорт задач - позитивные сценарий
  Необходимо импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Используются тестовые учетные данные Jira
    And Задан вид ключа возвращаемый от Jira

  Scenario: Импорт задачи с многострочным описанием
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "TEST"
      summary: "Some summary for test with multi-line description"
      type: "Task"
      description: "This multi-line description
                    shouldn't break 
                    this program,
                    and test will successful"
    """
    When Запустить программу JiraTaskCreator
    Then Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 1
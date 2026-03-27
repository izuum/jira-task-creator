Feature: Валидация обязательных полей
  Программа должна проверять наличие всех обязательных полей
  Чтобы не отправлять некорректные данные в Jira

  Background:
    Given Используются тестовые учетные данные Jira
    And Задан вид ключа возвращаемый от Jira

  Scenario: Импорт нескольких задач с отсутствующим полем в одной из них
    Given Подготовлен YAML файл с задачами по пути C:/Temp
    """
    issues:
    - project: "TEST"
      summary: "Correct task"
      type: "Feature"
    - project: "TEST"
      type: "Bug"
    - project: "TEST"
      summary: "Second correct task"
      type: "Feature"
    """
    When Запустить программу JiraTaskCreator
    Then Программа должна выбросить ошибку с сообщением об отсутствии поля "summary"
    And Программа должна завершиться успешно
    And Кол-во созданных в Jira задач: 2
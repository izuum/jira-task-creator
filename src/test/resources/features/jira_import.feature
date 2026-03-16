Feature: Jira Import
  Как пользователь
  Я хочу импортировать задачи из YAML файла
  Чтобы создавать сразу много задач

  Background:
    Given Jira API настроена на "https://dstepds.atlassian.net"

  Scenario: Успешный импорт одной задачи
    Given YAML файл с одной задачей:
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
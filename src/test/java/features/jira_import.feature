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
            summary: "some summaru for test"
            type: "Task"
            priority: "High"
            description: "some description for test"
        """

      When Я запускаю импорт
      Then Импорт должен быть успешен
      And Необходимо создать 1 задачу
      And В ответ Jira должна прислать ключ задачи "TEST-123"
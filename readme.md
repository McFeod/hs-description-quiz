Тема проекта: квиз по текстовым описаниям карт из Hearthstone.
==========================================================================================================

Презентация [здесь](https://docs.google.com/presentation/d/1V6LXYAKI27W-meHAw6yRXpG4bKCej17eKLo-qk6_5RE)
--------------------------------------------------------------

За это можно получить пожизненный бан от гугла, но курс, кажется, не подразумевает публикацию приложения на маркете.

Экраны:
 - настройки (локаль, число вопросов)
 - список вопросов (описание карт)
 - ответ (изображение карты)
 
Запросы в сеть:
 - получение идентификаторов карт (апи)
 - получение информации о карте по идентификатору (апи)
 - получение картинки по урлу
 
Локальное хранение данных (1 таблица?):
  - идентификатор карты
  - название
  - вопрос уже задан
  - описание (загружается по требованию)
  - урл картинки (загружается по требованию)
  - путь к файлу картинки (генерируется по требованию)
  
Состояние:
  - сгенерированный список вопросов
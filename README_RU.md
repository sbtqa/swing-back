# Swing-back
[![Build Status](https://travis-ci.org/sbtqa/swing-back.svg?branch=master)](https://travis-ci.org/sbtqa/swing-back) [![GitHub release](https://img.shields.io/github/release/sbtqa/swing-back.svg?style=flat-square)](https://github.com/sbtqa/swing-back/releases) [![Maven Central](https://img.shields.io/maven-central/v/ru.sbtqa.tag/swing-back.svg)](https://mvnrepository.com/artifact/ru.sbtqa.tag/swing-back)

Swing-back это opensource java framework для автоматизированного тестирования приложений написаных на [swing framework](https://ru.wikipedia.org/wiki/Swing), используя паттерн [PageObject](https://martinfowler.com/bliki/PageObject.html).


## Документация
### Примеры
Подготовленый проект с примерами использования swing-back [здесь](https://github.com/sbtqa/swingback-test).

### Начало тестирования

Есть различные способы запустить тестируемое приложение с использованием swing-back.
Swing-back поддерживает загрузку через файловую систему, для этого требуется добавить несколько свойст в файл **application.properties** который должен находится в *classpath/config*. 
* swingback.app.startclass = полное имя main класса вашего прилодения **(обязательное)** 
* swingback.forms.package = полный путь до пакета с формами **(обязательное)**
* swingback.jvm.prop.systemProperty1 = value1 **(опционально)**
  swingback.jvm.prop.systemProperty2 = value2
  …
  swingback.jvm.prop.systemPropertyN = valueN
  
В данном примере systemProperty1 ... systemPropertyN устанавливают JVM контекст в котором будет запущено приложение.
С этими параметрами требуется чтобы jar файлы ваше приложения хранились по следующему пути *classpath/app/jars*.  
Все необходимые ресурсы для тестового приложения должны находится по следующему пути *classpath/app/resources*

Если вы хотите использовать другую папку с jar файлами вы должны добавить следующие параметры:  
* swingback.app.jars.path.abs = абсолютный путь до папки с jar файлами
* swingback.app.resources.path.abs = абсолютный путь до папки с ресурсами


### Поиск элементов на форме

Для поиска элементов на форме используются методы-селекторы. Ниже приведен список существующих метов в порядке желательного приоритета использования:
* ComponentChooserByName (ищет элемент по имени swing элемента)
* ComponentChooserByToolTip (ищет элемент по механизму подсказок swing компонента)
* ComponentChooserByText (ищет элемент по тексту swing компонента)
* ComponentChooserByIndex (ищет элемент по индексу среди других компонентов с тем же типом)
* ComponentChooserByCoordinates (ищет элемент по координатам компонента)

По аналогии вы можете написать свой метод для поиска

## Контакты
Нашли ошибку или появились вопросы? [Проверьте](https://github.com/sbtqa/swing-back/issues) нет ли уже созданных issue, если нет то создайте [новое](https://github.com/sbtqa/swing-back/issues/new)!

## Лицензия 
Swing-back выпущен под лицензией Apache 2.0. [Подробности](https://github.com/sbtqa/swing-back/blob/master/LICENSE).
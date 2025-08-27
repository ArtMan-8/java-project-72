lint: # Проверить кодстайл
	make -C app lint

clean: # Очистить дистрибутив
	make -C app clean

build: clean # Установить зависимости и собрать дистрибутив
	make -C app build

test: build # Собрать дистрибутив и запустить тесты
	make -C app test

test-report: test # Подготовить покрытие тестов
	make -C app test-report

sonar: test-report
	make -C app sonar

run: build # Запустить дистрибутив
	make -C app run

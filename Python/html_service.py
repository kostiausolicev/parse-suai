def convert_json_to_html(json_file_path, html_file_path):
    import json

    # Чтение JSON из файла
    with open(json_file_path, 'r') as json_file:
        data = json.load(json_file)

    # Открытие HTML-файла для записи
    with open(html_file_path, 'w') as file:
        # Запись заголовка HTML-файла
        file.write('<html><head><title>Vector Data</title></head><body>')

        # Запись даты обновления в начало страницы
        file.write(f'<h1>Дата обновления: {data["update"]}</h1>')

        # Запись таблицы с данными
        file.write('<table>')
        file.write(
            '<tr><th>№</th><th>Снилс</th><th>БВИ</th><th>Дополнительные баллы</th><th>Баллы за экзамены</th>'
            '<th>Предметы сдачи</th><th>Оригиналы документов</th><th>Согласие на зачисление</th></tr>')
        for i, item in enumerate(data['list']):

            # Запись данных в строку таблицы с указанием цвета
            file.write(f'<tr>')
            file.write(f'<td>{i + 1}</td>')
            file.write(f'<td>{item["snils"]}</td>')
            file.write(f'<td>{item["bvi"]}</td>')
            file.write(f'<td>{item["additionalPoints"]}</td>')
            file.write(f'<td>{item["points"]}</td>')
            file.write(f'<td>{item["namesProfile"]}</td>')
            file.write(f'<td>{item["originalDocuments"]}</td>')
            file.write(f'<td>{item["consent"]}</td>')
            file.write('</tr>')
        file.write('</table>')

        # Завершение HTML-файла
        file.write('</body></html>')

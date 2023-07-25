def convert_json_to_html(json_file_path, html_file_path):
    import json
    with open(json_file_path, 'r', encoding="utf-8") as json_file:
        data = json.load(json_file)
    with open(html_file_path, 'w', encoding="utf-8") as file:
        file.write('<html><head><title>Vector Data</title><meta charset="utf-8"></head><body>')
        file.write(f'<h1>Дата обновления: {data["update"]}</h1>')
        file.write('<table>')
        file.write('<tr><td>' + '</td><td>'.join(
            [head for head in data['headers']] + ["БВИ", "Согласие", "Баллы по предметам",
                                                  "Предметы ВИ"]) + '</td></tr>')
        for i, item in enumerate(data['list']):
            file.write(f'<tr>')
            file.write(f'<td>{i + 1}</td>')
            file.write(f'<td>{item["snils"]}</td>')
            file.write(f'<td>{item["priority"]}</td>')
            file.write(f'<td>{item["allPoints"]}</td>')
            file.write(f'<td>{item["examsPoints"]}</td>')
            file.write(f'<td>{item["additionalPoints"]}</td>')
            file.write(f'<td>{item["originalDocuments"]}</td>')
            file.write(f'<td>{item["bvi"]}</td>')
            file.write(f'<td>{item["consent"]}</td>')
            file.write(f'<td>{item["points"]}</td>')
            file.write(f'<td>{item["namesProfile"]}</td>')
            file.write('</tr>')
        file.write('</table>')
        file.write('</body></html>')

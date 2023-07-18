import csv
import json


def convert_json_to_csv(json_file_path, csv_file_path):
    russian_columns = ["Снилс", "БВИ", "Дополнительные баллы", "Баллы за экзамены", "Предметы сдачи",
                       "Оригиналы документов", "Согласие на зачисление"]
    with open(json_file_path, 'r') as json_file:
        data = json.load(json_file)
    with open(csv_file_path, 'w', newline='', encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)
        writer.writerow(["Дата актуализации (Мск)"])
        writer.writerow([data["update"]])

        for i, item in enumerate(data["list"]):
            if i == 0:
                writer.writerow(["№"] + russian_columns)
            writer.writerow([str(i + 1)] + list(data["list"][i].values()))
